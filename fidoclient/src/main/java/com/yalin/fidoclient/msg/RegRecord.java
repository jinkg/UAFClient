package com.yalin.fidoclient.msg;

import com.google.gson.Gson;

/**
 * Created by YaLin on 2016/1/13.
 */
public class RegRecord {
    public int id;
    public String type;
    public String biometricsId;
    public String aaid;
    public String keyId;
    public String appId;
    public String username;
    public String userPrivateKey;
    public String userPublicKey;

    public RegRecord id(int id) {
        this.id = id;
        return this;
    }

    public RegRecord type(String type) {
        this.type = type;
        return this;
    }

    public RegRecord biometricsId(String biometricsId) {
        this.biometricsId = biometricsId;
        return this;
    }

    public RegRecord aaid(String aaid) {
        this.aaid = aaid;
        return this;
    }

    public RegRecord keyId(String keyId) {
        this.keyId = keyId;
        return this;
    }

    public RegRecord appId(String appId) {
        this.appId = appId;
        return this;
    }

    public RegRecord username(String username) {
        this.username = username;
        return this;
    }

    public RegRecord userPrivateKey(String userPrivateKey) {
        this.userPrivateKey = userPrivateKey;
        return this;
    }

    public RegRecord userPublicKey(String userPublicKey) {
        this.userPublicKey = userPublicKey;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
