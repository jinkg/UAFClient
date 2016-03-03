package com.yalin.fidoclient.asm.msg.obj;

/**
 * Created by YaLin on 2016/1/13.
 */
public class DeregisterIn {
    public String appID;
    public String keyID;

    public DeregisterIn(String appID, String keyID) {
        this.appID = appID;
        this.keyID = keyID;
    }
}
