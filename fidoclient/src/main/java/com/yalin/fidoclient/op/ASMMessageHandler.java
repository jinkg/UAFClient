package com.yalin.fidoclient.op;


import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yalin.fidoclient.api.UAFClientError;
import com.yalin.fidoclient.asm.api.StatusCode;
import com.yalin.fidoclient.asm.exceptions.ASMException;
import com.yalin.fidoclient.asm.msg.ASMRequest;
import com.yalin.fidoclient.asm.msg.ASMResponse;
import com.yalin.fidoclient.asm.msg.Request;
import com.yalin.fidoclient.asm.msg.obj.AuthenticatorInfo;
import com.yalin.fidoclient.asm.msg.obj.GetInfoOut;
import com.yalin.fidoclient.asm.msg.obj.RegisterIn;
import com.yalin.fidoclient.constants.Constants;
import com.yalin.fidoclient.msg.MatchCriteria;
import com.yalin.fidoclient.msg.OperationHeader;
import com.yalin.fidoclient.msg.Policy;
import com.yalin.fidoclient.msg.Version;
import com.yalin.fidoclient.msg.client.UAFIntentType;
import com.yalin.fidoclient.net.GetRequest;
import com.yalin.fidoclient.net.RequestQueueHelper;
import com.yalin.fidoclient.net.response.FacetIdList;
import com.yalin.fidoclient.net.response.FacetIdListResponse;
import com.yalin.fidoclient.op.traffic.Traffic;
import com.yalin.fidoclient.ui.AuthenticatorAdapter;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;
import com.yalin.fidoclient.utils.StatLog;
import com.yalin.fidoclient.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YaLin on 2016/1/11.
 */
public abstract class ASMMessageHandler {

    public interface StateChangeListener {
        void onStateChange(Traffic.OpStat newState, Traffic.OpStat oldState);
    }

    public interface UAFErrorCallback {
        void onError(short errorCode);
    }

    public static final String TAG = ASMMessageHandler.class.getSimpleName();
    public static final int REQUEST_ASM_OPERATION = 1;

    public static final String REG_TAG = "\"Reg\"";
    public static final String AUTH_TAG = "\"Auth\"";
    public static final String DEREG_TAG = "\"Dereg\"";

    private static final String UPV_TAG = "\"upv\"";
    private static final String MAJOR_TAG = "major";
    private static final String MINOR_TAG = "minor";

    protected final Gson gson = new Gson();

    protected Traffic.OpStat mCurrentState = Traffic.OpStat.PREPARE;

    protected final AuthenticatorListFragment fragment;
    protected final String facetId;

    protected StateChangeListener stateChangeListener;

    private UAFErrorCallback uafErrorCallback;

    protected String asmPackage;

    public static ASMMessageHandler parseMessage(AuthenticatorListFragment fragment, String intentType, String uafMessage, String channelBinding, int callerUid) {
        if (UAFIntentType.UAF_OPERATION.name().equals(intentType)) {
            boolean versionLegal = false;
            try {
                String upvSub = uafMessage.substring(uafMessage.indexOf(UPV_TAG));
                String upv = upvSub.substring(upvSub.indexOf("{"), upvSub.indexOf("}") + 1);
                JSONObject jsonObject = new JSONObject(upv);
                if (jsonObject.getInt(MAJOR_TAG) == 1 && jsonObject.getInt(MINOR_TAG) == 0) {
                    versionLegal = true;
                }
            } catch (Exception ignored) {
            }
            if (versionLegal) {
                if (uafMessage.contains(REG_TAG)) {
                    return new Reg(fragment, uafMessage, channelBinding, callerUid);
                } else if (uafMessage.contains(AUTH_TAG)) {
                    return new Auth(fragment, uafMessage, channelBinding, callerUid);
                } else if (uafMessage.contains(DEREG_TAG)) {
                    return new Dereg(fragment, uafMessage, callerUid);
                }
            }
        } else if (UAFIntentType.CHECK_POLICY.name().equals(intentType)) {
            return new CheckPolicy(fragment, uafMessage, callerUid);
        } else if (UAFIntentType.DISCOVER.name().equals(intentType)) {
            return new Discover(fragment, callerUid);
        } else if (UAFIntentType.UAF_OPERATION_COMPLETION_STATUS.name().equals(intentType)) {
            return new Completion(fragment, callerUid);
        }
        return new ASMMessageHandler(fragment, callerUid) {
            @Override
            public void start() {
                error(UAFClientError.PROTOCOL_ERROR);
            }

            @Override
            public void trafficStart() {
                error(UAFClientError.PROTOCOL_ERROR);
            }

            @Override
            public void traffic(String asmResponseMsg) {
                error(UAFClientError.PROTOCOL_ERROR);
            }
        };
    }

    public void setStateChangeListener(StateChangeListener listener) {
        stateChangeListener = listener;
    }

    public void setUafErrorCallback(UAFErrorCallback callback) {
        uafErrorCallback = callback;
    }

    public ASMMessageHandler(AuthenticatorListFragment fragment, int callerUid) {
        this.fragment = fragment;
        this.facetId = Utils.getFacetId(fragment.getActivity().getApplication(), callerUid);
    }

    public abstract void start();

    public abstract void trafficStart();

    public abstract void traffic(String asmResponseMsg) throws ASMException;

    public String getCurrentOpDescription() {
        return null;
    }

    public void setAsmPackage(String asmPackage) {
        this.asmPackage = asmPackage;
    }

    public Policy getPolicy() {
        return null;
    }

    protected boolean checkHeader(OperationHeader header) {
        if (header == null) {
            return false;
        }
        if (header.appID == null || header.appID.length() > Constants.APP_ID_MAX_LEN) {
            return false;
        }
        if (header.appID.length() > 0 && !header.appID.contains(Constants.APP_ID_PREFIX) && !header.appID.equals(facetId)) {
            return false;
        }
        return true;
    }

    protected boolean checkChallenge(String challenge) {
        if (TextUtils.isEmpty(challenge)) {
            return false;
        }
        if (challenge.length() < Constants.CHALLENGE_MIN_LEN || challenge.length() > Constants.CHALLENGE_MAX_LEN) {
            return false;
        }
        if (!challenge.matches(Constants.BASE64_REGULAR)) {
            return false;
        }
        return true;
    }

    protected boolean checkPolicy(Policy policy) {
        if (policy == null || policy.accepted == null) {
            return false;
        }
        return true;
    }

    protected void getTrustFacetId(String appId, final String facetId) {
        if (appId.contains(Constants.APP_ID_PREFIX)) {
            fragment.loading();
            RequestQueue requestQueue = RequestQueueHelper.getInstance(fragment.getActivity().getApplicationContext());
            GetRequest<FacetIdListResponse> request = new GetRequest<>(appId, FacetIdListResponse.class,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            fragment.loadingComplete();
                            if (checkFacetId(facetId, ((FacetIdListResponse) response).trustedFacets)) {
                                trafficStart();
                            } else {
                                error(UAFClientError.UNTRUSTED_FACET_ID);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            fragment.loadingComplete();
                            getTrustFacetIdError();
                        }
                    }
            );
            request.setTag(TAG);
            requestQueue.add(request);
        } else {
            if (TextUtils.isEmpty(appId) || appId.equals(facetId)) {
                trafficStart();
            }
        }
    }

    protected void getTrustFacetIdError() {
        error(UAFClientError.UNTRUSTED_FACET_ID);
    }

    protected void updateState(Traffic.OpStat newState) {
        StatLog.printLog(TAG, "update op state:" + mCurrentState.name() + "->" + newState.name());
        if (stateChangeListener != null) {
            stateChangeListener.onStateChange(newState, mCurrentState);
        }
        mCurrentState = newState;
    }

    protected short handleGetInfo(String msg, AuthenticatorAdapter.OnAuthenticatorClickCallback callback) {
        StatLog.printLog(TAG, "get info :" + msg);
        ASMResponse asmResponse = ASMResponse.fromJson(msg, GetInfoOut.class);
        if (asmResponse.statusCode != StatusCode.UAF_ASM_STATUS_OK) {
            return UAFClientError.PROTOCOL_ERROR;
        }
        GetInfoOut getInfoOut = (GetInfoOut) asmResponse.responseData;
        AuthenticatorInfo[] authenticatorInfos = getInfoOut.Authenticators;
        StatLog.printLog(TAG, "client reg parse policy: " + gson.toJson(authenticatorInfos));
        if (authenticatorInfos == null) {
            return UAFClientError.NO_SUITABLE_AUTHENTICATOR;
        }
        List<AuthenticatorInfo> parseResult = parsePolicy(getPolicy(), authenticatorInfos);
        if (parseResult == null || parseResult.size() == 0) {
            return UAFClientError.NO_SUITABLE_AUTHENTICATOR;
        }
        fragment.showAuthenticator(parseResult, callback);
        return UAFClientError.NO_ERROR;
    }


    protected void error(short errorCode) {
        if (uafErrorCallback != null) {
            uafErrorCallback.onError(errorCode);
        }
    }

    public static String getInfoRequest(Version version) {
        StatLog.printLog(TAG, "asm request get info");
        ASMRequest<RegisterIn> asmRequest = new ASMRequest<>();
        asmRequest.requestType = Request.GetInfo;
        asmRequest.asmVersion = version;
        Gson gson = new GsonBuilder().setExclusionStrategies(getExclusionStrategy())
                .create();
        String asmRequestMsg = gson.toJson(asmRequest);
        StatLog.printLog(TAG, "asm request: " + asmRequestMsg);
        return asmRequestMsg;
    }

    private static ExclusionStrategy getExclusionStrategy() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equals(ASMRequest.authenticatorIndexName);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
    }

    public static boolean checkFacetId(String facetId, ArrayList<FacetIdList> facetIdList) {
        if (facetIdList != null && facetIdList.size() > 0) {
            FacetIdList facetIdList1 = facetIdList.get(0);
            if (facetIdList1.ids != null) {
                for (String id : facetIdList1.ids) {
                    if (id.equals(facetId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected static List<AuthenticatorInfo> parsePolicy(Policy policy, AuthenticatorInfo[] authenticatorInfos) {
        if (policy == null) {
            return Arrays.asList(authenticatorInfos);
        }
        List<AuthenticatorInfo> authenticatorInfoList = new ArrayList<>();
        for (AuthenticatorInfo info : authenticatorInfos) {
            for (MatchCriteria[] criterias : policy.accepted) {
                boolean setMatch = true;
                for (MatchCriteria criteria : criterias) {
                    if (!criteria.isMatch(info)) {
                        setMatch = false;
                        break;
                    }
                }
                if (setMatch) {
                    authenticatorInfoList.add(info);
                    break;
                }
            }
        }

        if (policy.disallowed != null) {
            for (AuthenticatorInfo info : authenticatorInfoList) {
                for (MatchCriteria matchCriteria : policy.disallowed) {
                    if (matchCriteria.isMatch(info)) {
                        authenticatorInfoList.remove(info);
                        break;
                    }
                }
            }
        }
        return authenticatorInfoList;
    }

}
