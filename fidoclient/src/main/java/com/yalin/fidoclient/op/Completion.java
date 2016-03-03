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
