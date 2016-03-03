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
import android.util.Base64;

import com.yalin.fidoclient.api.UAFClientError;
import com.yalin.fidoclient.api.UAFIntent;
import com.yalin.fidoclient.asm.api.ASMApi;
import com.yalin.fidoclient.asm.api.StatusCode;
import com.yalin.fidoclient.asm.exceptions.ASMException;
import com.yalin.fidoclient.asm.msg.ASMRequest;
import com.yalin.fidoclient.asm.msg.ASMResponse;
import com.yalin.fidoclient.asm.msg.Request;
import com.yalin.fidoclient.asm.msg.obj.AuthenticatorInfo;
import com.yalin.fidoclient.asm.msg.obj.RegisterIn;
import com.yalin.fidoclient.asm.msg.obj.RegisterOut;
import com.yalin.fidoclient.constants.Constants;
import com.yalin.fidoclient.msg.AuthenticatorRegistrationAssertion;
import com.yalin.fidoclient.msg.ChannelBinding;
import com.yalin.fidoclient.msg.FinalChallengeParams;
import com.yalin.fidoclient.msg.OperationHeader;
import com.yalin.fidoclient.msg.Policy;
import com.yalin.fidoclient.msg.RegistrationRequest;
import com.yalin.fidoclient.msg.RegistrationResponse;
import com.yalin.fidoclient.msg.Version;
import com.yalin.fidoclient.msg.client.UAFMessage;
import com.yalin.fidoclient.op.traffic.Traffic;
import com.yalin.fidoclient.ui.AuthenticatorAdapter;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;
import com.yalin.fidoclient.utils.StatLog;

/**
 * Created by YaLin on 2016/1/21.
 */
public class Reg extends ASMMessageHandler implements AuthenticatorAdapter.OnAuthenticatorClickCallback {
    private static final String TAG = Reg.class.getSimpleName();
    private final RegistrationRequest registrationRequest;
    private final ChannelBinding channelBinding;

    private String finalChallenge;

    public Reg(AuthenticatorListFragment fragment, String message, String channelBinding, int callerUid) {
        super(fragment, callerUid);
        if (TextUtils.isEmpty(message)) {
            throw new IllegalArgumentException();
        }
        this.registrationRequest = getRegistrationRequest(message);
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
        if (registrationRequest == null || fragment == null) {
            error(UAFClientError.PROTOCOL_ERROR);
            return;
        }
        getTrustFacetId(registrationRequest.header.appID, facetId);
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
                    updateState(Traffic.OpStat.REG_PENDING);
                } else {
                    error(errorCode);
                }
                break;
            case REG_PENDING:
                handleRegOut(asmResponseMsg);
                updateState(Traffic.OpStat.PREPARE);
                break;
            default:
                error(UAFClientError.PROTOCOL_ERROR);
                break;
        }
    }

    @Override
    public Policy getPolicy() {
        return registrationRequest.policy;
    }

    private void handleRegOut(String msg) throws ASMException {
        ASMResponse asmResponse = ASMResponse.fromJson(msg, RegisterOut.class);
        if (asmResponse.statusCode != StatusCode.UAF_ASM_STATUS_OK) {
            throw new ASMException(asmResponse.statusCode);
        }
        String response;
        if (asmResponse.responseData instanceof RegisterOut) {
            RegisterOut registerOut = (RegisterOut) asmResponse.responseData;
            RegistrationResponse[] responses = new RegistrationResponse[1];
            responses[0] = wrapResponse(registerOut);
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

    private RegistrationResponse wrapResponse(RegisterOut registerOut) {
        RegistrationResponse response = new RegistrationResponse();
        response.header = new OperationHeader();
        response.header.serverData = registrationRequest.header.serverData;
        response.header.appID = registrationRequest.header.appID;
        response.header.op = registrationRequest.header.op;
        response.header.upv = registrationRequest.header.upv;

        response.fcParams = finalChallenge;

        response.assertions = new AuthenticatorRegistrationAssertion[1];
        response.assertions[0] = new AuthenticatorRegistrationAssertion();
        response.assertions[0].assertion = registerOut.assertion;
        response.assertions[0].assertionScheme = registerOut.assertionScheme;
        return response;
    }

    private RegistrationRequest getRegistrationRequest(String uafMsg) {
        RegistrationRequest[] requests;
        try {
            requests = gson.fromJson(uafMsg, RegistrationRequest[].class);
        } catch (Exception e) {
            requests = null;
        }
        RegistrationRequest result;
        if (requests == null || requests.length == 0) {
            return null;
        }
        RegistrationRequest temp = null;
        for (RegistrationRequest request : requests) {
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

    @Override
    protected boolean checkHeader(OperationHeader header) {
        if (TextUtils.isEmpty(header.serverData) || header.serverData.length() > Constants.SERVER_DATA_MAX_LEN) {
            return false;
        }
        return super.checkHeader(header);
    }


    private boolean checkRequest(RegistrationRequest registrationRequest) {
        if (registrationRequest == null) {
            return false;
        }
        if (!checkChallenge(registrationRequest.challenge)) {
            return false;
        }
        if (!checkHeader(registrationRequest.header)) {
            return false;
        }
        if (TextUtils.isEmpty(registrationRequest.username)) {
            return false;
        }
        if (registrationRequest.username.length() > Constants.USERNAME_MAX_LEN) {
            return false;
        }
        if (!checkPolicy(registrationRequest.policy)) {
            return false;
        }
        return true;
    }

    @Override
    public void onAuthenticatorClick(AuthenticatorInfo info) {
        FinalChallengeParams fcParams = new FinalChallengeParams();
        if (TextUtils.isEmpty(registrationRequest.header.appID)) {
            fcParams.appID = facetId;
        } else {
            fcParams.appID = registrationRequest.header.appID;
        }
        fcParams.challenge = registrationRequest.challenge;
        fcParams.facetID = facetId;
        fcParams.channelBinding = channelBinding;

        finalChallenge = Base64.encodeToString(gson.toJson(fcParams).getBytes(), Base64.URL_SAFE);
        RegisterIn registerIn = new RegisterIn(registrationRequest.header.appID, registrationRequest.username, finalChallenge, info.attestationTypes[0]);

        ASMRequest<RegisterIn> asmRequest = new ASMRequest<>();
        asmRequest.requestType = Request.Register;
        asmRequest.args = registerIn;
        asmRequest.asmVersion = registrationRequest.header.upv;
        asmRequest.authenticatorIndex = info.authenticatorIndex;
        String asmRequestMsg = gson.toJson(asmRequest);
        StatLog.printLog(TAG, "asm request: " + asmRequestMsg);
        ASMApi.doOperation(fragment, REQUEST_ASM_OPERATION, asmRequestMsg, asmPackage);
    }
}
