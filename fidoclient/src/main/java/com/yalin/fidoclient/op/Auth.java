package com.yalin.fidoclient.op;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;

import com.yalin.fidoclient.api.UAFClientError;
import com.yalin.fidoclient.api.UAFIntent;
import com.yalin.fidoclient.asm.api.ASMApi;
import com.yalin.fidoclient.asm.api.StatusCode;
import com.yalin.fidoclient.asm.exceptions.ASMException;
import com.yalin.fidoclient.asm.msg.ASMRequest;
import com.yalin.fidoclient.asm.msg.ASMResponse;
import com.yalin.fidoclient.asm.msg.Request;
import com.yalin.fidoclient.asm.msg.obj.AuthenticateIn;
import com.yalin.fidoclient.asm.msg.obj.AuthenticateOut;
import com.yalin.fidoclient.asm.msg.obj.AuthenticatorInfo;
import com.yalin.fidoclient.constants.Constants;
import com.yalin.fidoclient.msg.AuthenticationRequest;
import com.yalin.fidoclient.msg.AuthenticationResponse;
import com.yalin.fidoclient.msg.AuthenticatorSignAssertion;
import com.yalin.fidoclient.msg.ChannelBinding;
import com.yalin.fidoclient.msg.FinalChallengeParams;
import com.yalin.fidoclient.msg.OperationHeader;
import com.yalin.fidoclient.msg.Policy;
import com.yalin.fidoclient.msg.Version;
import com.yalin.fidoclient.msg.client.UAFMessage;
import com.yalin.fidoclient.op.traffic.Traffic;
import com.yalin.fidoclient.ui.AuthenticatorAdapter;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;
import com.yalin.fidoclient.utils.StatLog;

/**
 * Created by YaLin on 2016/1/21.
 */
public class Auth extends ASMMessageHandler implements AuthenticatorAdapter.OnAuthenticatorClickCallback {
    private static final String TAG = Auth.class.getSimpleName();
    private final AuthenticationRequest authenticationRequest;
    private final ChannelBinding channelBinding;

    private String finalChallenge;

    public Auth(AuthenticatorListFragment fragment, String message, String channelBinding, int callerUid) {
        super(fragment, callerUid);
        try {
            authenticationRequest = getAuthRequest(message);
        } catch (Exception e) {
            throw new IllegalStateException("Authentication message error");
        }
        ChannelBinding cb;
        try {
            cb = gson.fromJson(channelBinding, ChannelBinding.class);
        } catch (Exception e) {
            cb = null;
        }
        this.channelBinding = cb;
        updateState(Traffic.OpStat.PREPARE);
    }


    @Override
    public void start() {
        if (authenticationRequest == null || fragment == null) {
            error(UAFClientError.PROTOCOL_ERROR);
            return;
        }
        getTrustFacetId(authenticationRequest.header.appID, facetId);
    }

    @Override
    public void trafficStart() {
        switch (mCurrentState) {
            case PREPARE:
                String getInfoMessage = getInfoRequest(new Version(1, 0));
                ASMApi.doOperation(fragment, REQUEST_ASM_OPERATION, getInfoMessage, asmPackage);
                updateState(Traffic.OpStat.GET_INFO_PENDING);
                break;
            default:
                error(UAFClientError.PROTOCOL_ERROR);
                break;
        }
    }

    @Override
    public void traffic(String asmResponseMsg) throws ASMException {
        StatLog.printLog(TAG, "asm response: " + asmResponseMsg);
        switch (mCurrentState) {
            case GET_INFO_PENDING:
                short errorCode = handleGetInfo(asmResponseMsg, this);
                if (UAFClientError.NO_ERROR == errorCode) {
                    updateState(Traffic.OpStat.AUTH_PENDING);
                } else {
                    error(errorCode);
                }
                break;
            case AUTH_PENDING:
                handleAuthOut(asmResponseMsg);
                updateState(Traffic.OpStat.PREPARE);
                break;
            default:
                error(UAFClientError.PROTOCOL_ERROR);
                break;
        }
    }

    @Override
    public Policy getPolicy() {
        return authenticationRequest.policy;
    }

    private void handleAuthOut(String msg) throws ASMException {
        ASMResponse asmResponse = ASMResponse.fromJson(msg, AuthenticateOut.class);
        if (asmResponse.statusCode != StatusCode.UAF_ASM_STATUS_OK) {
            throw new ASMException(asmResponse.statusCode);
        }
        String response;
        if (asmResponse.responseData instanceof AuthenticateOut) {
            AuthenticateOut authenticateOut = (AuthenticateOut) asmResponse.responseData;
            AuthenticationResponse[] responses = new AuthenticationResponse[1];
            responses[0] = wrapResponse(authenticateOut);
            response = gson.toJson(responses);
        } else {
            throw new ASMException(StatusCode.UAF_ASM_STATUS_ERROR);
        }
        if (fragment.getActivity() != null) {
            Intent intent = UAFIntent.getUAFOperationResultIntent(fragment.getActivity().getComponentName().flattenToString(), new UAFMessage(response).toJson());
            fragment.getActivity().setResult(Activity.RESULT_OK, intent);
            fragment.getActivity().finish();
        }
    }

    private AuthenticationResponse wrapResponse(AuthenticateOut authenticateOut) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.header = new OperationHeader();
        response.header.serverData = authenticationRequest.header.serverData;
        response.header.appID = authenticationRequest.header.appID;
        response.header.op = authenticationRequest.header.op;
        response.header.upv = authenticationRequest.header.upv;

        response.fcParams = finalChallenge;

        response.assertions = new AuthenticatorSignAssertion[1];
        response.assertions[0] = new AuthenticatorSignAssertion();
        response.assertions[0].assertion = authenticateOut.assertion;
        response.assertions[0].assertionScheme = authenticateOut.assertionScheme;
        return response;
    }

    public AuthenticationRequest getAuthRequest(String uafMsg) {
        AuthenticationRequest[] requests;
        try {
            requests = gson.fromJson(uafMsg, AuthenticationRequest[].class);
        } catch (Exception e) {
            requests = null;
        }
        AuthenticationRequest result;
        if (requests == null || requests.length == 0) {
            return null;
        }
        AuthenticationRequest temp = null;
        for (AuthenticationRequest request : requests) {
            if (request.header == null || request.header.upv == null) {
                continue;
            }
            if (request.header.upv.equals(Version.getCurrentSupport())) {
                if (temp == null) {
                    temp = request;
                } else {
                    temp = null;
                    break;
                }
            }
        }
        result = temp;
        if (!checkRequest(result)) {
            result = null;
        }
        return result;
    }

    private boolean checkRequest(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null) {
            return false;
        }
        if (!checkChallenge(authenticationRequest.challenge)) {
            return false;
        }
        if (!checkHeader(authenticationRequest.header)) {
            return false;
        }
        if (!checkPolicy(authenticationRequest.policy)) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkHeader(OperationHeader header) {
        if (TextUtils.isEmpty(header.serverData) || header.serverData.length() > Constants.SERVER_DATA_MAX_LEN) {
            return false;
        }
        return super.checkHeader(header);
    }

    @Override
    public void onAuthenticatorClick(AuthenticatorInfo info) {
        FinalChallengeParams fcParams = new FinalChallengeParams();
        if (TextUtils.isEmpty(authenticationRequest.header.appID)) {
            fcParams.appID = facetId;
        } else {
            fcParams.appID = authenticationRequest.header.appID;
        }
        fcParams.challenge = authenticationRequest.challenge;
        fcParams.facetID = facetId;
        fcParams.channelBinding = channelBinding;

        finalChallenge = Base64.encodeToString(gson.toJson(fcParams).getBytes(), Base64.URL_SAFE);
        AuthenticateIn authenticateIn = new AuthenticateIn(authenticationRequest.header.appID, null, finalChallenge);
        authenticateIn.transaction = authenticationRequest.transaction;

        ASMRequest<AuthenticateIn> asmRequest = new ASMRequest<>();
        asmRequest.requestType = Request.Authenticate;
        asmRequest.args = authenticateIn;
        asmRequest.asmVersion = authenticationRequest.header.upv;
        asmRequest.authenticatorIndex = info.authenticatorIndex;
        String asmRequestMsg = gson.toJson(asmRequest);
        StatLog.printLog(TAG, "asm request: " + asmRequestMsg);
        ASMApi.doOperation(fragment, REQUEST_ASM_OPERATION, asmRequestMsg, asmPackage);
    }
}
