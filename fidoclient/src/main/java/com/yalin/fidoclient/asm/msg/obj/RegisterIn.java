package com.yalin.fidoclient.asm.msg.obj;

/**
 * Created by YaLin on 2016/1/13.
 */
public class RegisterIn {
    public String appID;
    public String username;
    public String finalChallenge;
    public int attestationType;

    public RegisterIn(String appID, String username, String finalChallenge, int attestationType) {
        this.appID = appID;
        this.username = username;
        this.finalChallenge = finalChallenge;
        this.attestationType = attestationType;
    }
}
