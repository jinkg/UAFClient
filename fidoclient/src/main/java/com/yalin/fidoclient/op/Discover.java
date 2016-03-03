package com.yalin.fidoclient.op;

import android.app.Activity;
import android.content.Intent;

import com.yalin.fidoclient.api.UAFClientError;
import com.yalin.fidoclient.api.UAFIntent;
import com.yalin.fidoclient.asm.api.ASMApi;
import com.yalin.fidoclient.asm.api.StatusCode;
import com.yalin.fidoclient.asm.exceptions.ASMException;
import com.yalin.fidoclient.asm.msg.ASMResponse;
import com.yalin.fidoclient.asm.msg.obj.AuthenticatorInfo;
import com.yalin.fidoclient.asm.msg.obj.GetInfoOut;
import com.yalin.fidoclient.msg.Authenticator;
import com.yalin.fidoclient.msg.DiscoverData;
import com.yalin.fidoclient.msg.Version;
import com.yalin.fidoclient.op.traffic.Traffic;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;
import com.yalin.fidoclient.utils.StatLog;

/**
 * Created by YaLin on 2016/1/21.
 */
public class Discover extends ASMMessageHandler {
    private static final String TAG = Discover.class.getSimpleName();

    public Discover(AuthenticatorListFragment fragment, int callerUid) {
        super(fragment, callerUid);
        updateState(Traffic.OpStat.PREPARE);
    }

    @Override
    public void start() {
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
    public void trafficStart() {

    }

    @Override
    public void traffic(String asmResponseMsg) throws ASMException {
        StatLog.printLog(TAG, "asm response: " + asmResponseMsg);
        switch (mCurrentState) {
            case GET_INFO_PENDING:
                if (!handleGetInfo(asmResponseMsg)) {
                    error(UAFClientError.PROTOCOL_ERROR);
                }
                updateState(Traffic.OpStat.PREPARE);
                break;
            default:
                error(UAFClientError.PROTOCOL_ERROR);
                break;
        }
    }

    private boolean handleGetInfo(String asmResponseMsg) {
        StatLog.printLog(TAG, "get info :" + asmResponseMsg);
        ASMResponse asmResponse = ASMResponse.fromJson(asmResponseMsg, GetInfoOut.class);
        if (asmResponse.statusCode != StatusCode.UAF_ASM_STATUS_OK) {
            return false;

        }
        GetInfoOut getInfoOut = (GetInfoOut) asmResponse.responseData;
        AuthenticatorInfo[] authenticatorInfos = getInfoOut.Authenticators;
        if (authenticatorInfos == null) {
            return false;
        }

        Authenticator[] authenticators = Authenticator.fromInfo(authenticatorInfos);
        if (authenticators == null) {
            return false;
        }
        DiscoverData discoverData = new DiscoverData();
        discoverData.supportedUAFVersions = authenticators[0].supportedUAFVersions;
        discoverData.availableAuthenticators = authenticators;
        discoverData.clientVendor = "yalin";
        discoverData.clientVersion = new Version(1, 0);
        String result = gson.toJson(discoverData);
        StatLog.printLog(TAG, "discover prepare result:" + result);
        if (fragment.getActivity() != null) {
            Intent intent = UAFIntent.getDiscoverResultIntent(result, fragment.getActivity().getComponentName().flattenToString(), UAFClientError.NO_ERROR);
            fragment.getActivity().setResult(Activity.RESULT_OK, intent);
            fragment.getActivity().finish();
        }
        return true;
    }
}
