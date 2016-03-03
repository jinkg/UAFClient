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
