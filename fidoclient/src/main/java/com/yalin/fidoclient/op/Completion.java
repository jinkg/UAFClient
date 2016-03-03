package com.yalin.fidoclient.op;


import com.yalin.fidoclient.asm.exceptions.ASMException;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;

/**
 * Created by YaLin on 2016/1/22.
 */
public class Completion extends ASMMessageHandler {
    public Completion(AuthenticatorListFragment fragment, int callerUid) {
        super(fragment, callerUid);
    }

    @Override
    public void start() {
        if (fragment.getActivity() != null) {
            fragment.getActivity().finish();
        }
    }

    @Override
    public void trafficStart() {

    }

    @Override
    public void traffic(String asmResponseMsg) throws ASMException {
    }
}
