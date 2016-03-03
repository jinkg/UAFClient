/*
 * Copyright 2016 YaLin Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yalin.fidoclient.op;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yalin.fidoclient.api.UAFClientError;
import com.yalin.fidoclient.api.UAFIntent;
import com.yalin.fidoclient.asm.api.ASMApi;
import com.yalin.fidoclient.asm.msg.ASMRequest;
import com.yalin.fidoclient.asm.msg.Request;
import com.yalin.fidoclient.asm.msg.obj.DeregisterIn;
import com.yalin.fidoclient.constants.Constants;
import com.yalin.fidoclient.msg.DeregResponse;
import com.yalin.fidoclient.msg.DeregisterAuthenticator;
import com.yalin.fidoclient.msg.DeregistrationRequest;
import com.yalin.fidoclient.msg.Version;
import com.yalin.fidoclient.msg.client.UAFMessage;
import com.yalin.fidoclient.op.traffic.Traffic;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;
import com.yalin.fidoclient.utils.StatLog;


public class Dereg extends ASMMessageHandler {
    private static final String TAG = Dereg.class.getSimpleName();

    private Gson gson = new Gson();

    private final DeregistrationRequest deRegistrationRequest;

    public Dereg(AuthenticatorListFragment fragment, String message, int callerUid) {
        super(fragment, callerUid);
        updateState(Traffic.OpStat.PREPARE);
        this.deRegistrationRequest = getDeRegistrationRequest(message);
    }

    @Override
    public void start() {
        if (deRegistrationRequest == null) {
            error(UAFClientError.PROTOCOL_ERROR);
            return;
        }
        getTrustFacetId(deRegistrationRequest.header.appID, facetId);
    }

    @Override
    public void trafficStart() {
        switch (mCurrentState) {
            case PREPARE:
                String deRegMsg = deReg();
                ASMApi.doOperation(fragment, REQUEST_ASM_OPERATION, deRegMsg, asmPackage);
                updateState(Traffic.OpStat.DEREG_PENDING);
                break;
            default:
                error(UAFClientError.PROTOCOL_ERROR);
                break;
        }
    }

    @Override
    public void traffic(String asmResponseMsg) {
        switch (mCurrentState) {
            case DEREG_PENDING:
                String deRegMsg = gson.toJson(new DeregResponse((short) 0));
                handleDeRegOut(deRegMsg);
                updateState(Traffic.OpStat.PREPARE);
                break;
            default:
                error(UAFClientError.PROTOCOL_ERROR);
                break;
        }
    }

    private String deReg() {
        DeregisterIn deregisterIn = new DeregisterIn(deRegistrationRequest.header.appID, deRegistrationRequest.authenticators[0].keyID);

        ASMRequest<DeregisterIn> asmRequest = new ASMRequest<>();
        asmRequest.requestType = Request.Deregister;
        asmRequest.args = deregisterIn;
        asmRequest.asmVersion = deRegistrationRequest.header.upv;
        String asmRequestMsg = gson.toJson(asmRequest);
        StatLog.printLog(TAG, "asm request: " + asmRequestMsg);
        return asmRequestMsg;
    }

    private void handleDeRegOut(String msg) {
        StatLog.printLog(TAG, "client deReg result:" + msg);
        if (fragment.getActivity() != null) {
            Intent intent = UAFIntent.getUAFOperationResultIntent(fragment.getActivity().getComponentName().flattenToString(), new UAFMessage(msg).toJson());
            fragment.getActivity().setResult(Activity.RESULT_OK, intent);
            fragment.getActivity().finish();
        }
    }

    private DeregistrationRequest getDeRegistrationRequest(String uafMsg) {
        DeregistrationRequest[] requests;
        try {
            requests = gson.fromJson(uafMsg, DeregistrationRequest[].class);
        } catch (Exception e) {
            requests = null;
        }
        DeregistrationRequest result;
        if (requests == null || requests.length == 0) {
            return null;
        }
        DeregistrationRequest temp = null;
        for (DeregistrationRequest request : requests) {
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

    private boolean checkRequest(DeregistrationRequest request) {
        if (request == null) {
            return false;
        }
        if (!checkHeader(request.header)) {
            return false;
        }
        if (!checkAuthenticators(request.authenticators)) {
            return false;
        }
        return true;
    }

    private boolean checkAuthenticators(DeregisterAuthenticator[] authenticators) {
        if (authenticators == null) {
            return false;
        }
        DeregisterAuthenticator authenticator = authenticators[0];
        if (authenticator == null) {
            return false;
        }
        if (TextUtils.isEmpty(authenticator.aaid) || TextUtils.isEmpty(authenticator.keyID)) {
            return false;
        }
        if (authenticator.aaid.length() != 9 || authenticator.keyID.length() < Constants.KEY_ID_MIN_LEN || authenticator.keyID.length() > Constants.KEY_ID_MAX_LEN) {
            return false;
        }
        if (!authenticator.keyID.trim().matches(Constants.BASE64_REGULAR)) {
            return false;
        }
        if (authenticator.keyID.contains("=")) {
            return false;
        }
        return true;
    }
}
