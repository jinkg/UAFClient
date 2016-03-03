package com.yalin.fidoclient.asm.msg;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by YaLin on 2016/1/13.
 */
public class ASMResponse<T> {
    public short statusCode;
    public T responseData;

    public static ASMResponse fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(ASMResponse.class, clazz);
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
