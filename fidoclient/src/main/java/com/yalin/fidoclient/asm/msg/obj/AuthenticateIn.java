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

package com.yalin.fidoclient.asm.msg.obj;

import com.yalin.fidoclient.msg.Transaction;

/**
 * Created by YaLin on 2016/1/13.
 */
public class AuthenticateIn {
    public String appID;
    public String[] keyIDs;
    public String finalChallenge;
    public Transaction[] transaction;

    public AuthenticateIn(String appID, String[] keyIDs, String finalChallenge) {
        this.appID = appID;
        this.keyIDs = keyIDs;
        this.finalChallenge = finalChallenge;
    }

    public void setTransaction(Transaction[] transaction) {
        this.transaction = transaction;
    }
}
