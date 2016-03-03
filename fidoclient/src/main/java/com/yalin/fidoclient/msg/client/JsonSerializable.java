package com.yalin.fidoclient.msg.client;

import com.google.gson.Gson;

/**
 * Created by YaLin on 2016/1/11.
 */
public abstract class JsonSerializable {
    protected transient Gson gson = new Gson();

    public abstract String toJson();

    public abstract void loadFromJson(String json);
}
