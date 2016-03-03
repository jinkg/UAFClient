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


import com.yalin.fidoclient.asm.exceptions.ASMException;
import com.yalin.fidoclient.asm.msg.ASMRequest;
import com.yalin.fidoclient.asm.msg.Request;
import com.yalin.fidoclient.msg.Policy;
import com.yalin.fidoclient.msg.RegistrationRequest;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;

/**
 * Created by YaLin on 2016/1/18.
 */
public class CheckPolicy extends ASMMessageHandler {
    private Policy policy;

    public CheckPolicy(AuthenticatorListFragment fragment, String message, int callerUid) {
        super(fragment, callerUid);
        try {
            RegistrationRequest registrationRequest = getRegistrationRequest(message);
            policy = registrationRequest.policy;
        } catch (Exception e) {
            throw new IllegalStateException("register message error");
        }
    }

    @Override
    public void start() {
        ASMRequest asmRequest = new ASMRequest();
        asmRequest.requestType = Request.GetInfo;
        gson.toJson(asmRequest);
    }

    @Override
    public void trafficStart() {

    }

    @Override
    public void traffic(String asmResponseMsg) throws ASMException {
    }

    private RegistrationRequest getRegistrationRequest(String uafMsg) {
        RegistrationRequest[] requests = gson.fromJson(uafMsg, RegistrationRequest[].class);
        return requests[0];
    }
}
