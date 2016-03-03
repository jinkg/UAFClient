package com.yalin.fidoclient.net;


import com.android.volley.Request;
import com.android.volley.Response;
import com.yalin.fidoclient.net.response.BaseResponse;

import java.util.Map;

/**
 * Created by 雅麟 on 2015/3/21.
 */
public class GetRequest<T extends BaseResponse> extends BaseRequest<T> {

    public GetRequest(String url, Class<T> cls, Map<String, String> header, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, url, cls, header, listener, errorListener);
    }

    public GetRequest(String url, Class<T> cls, Response.Listener listener, Response.ErrorListener errorListener) {
        this(url, cls, null, listener, errorListener);
    }

}