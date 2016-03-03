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
