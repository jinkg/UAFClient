package com.yalin.fidoclient.asm.msg;


import com.google.gson.Gson;
import com.yalin.fidoclient.msg.Version;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by YaLin on 2016/1/13.
 */
public class ASMRequest<T> {
    public static final String authenticatorIndexName = "authenticatorIndex";

    public Request requestType;
    public Version asmVersion;
    public int authenticatorIndex;
    public T args;

    public static ASMRequest fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(ASMRequest.class, clazz);
        return gson.fromJson(json, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
