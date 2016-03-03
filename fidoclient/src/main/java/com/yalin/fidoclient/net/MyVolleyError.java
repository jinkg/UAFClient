package com.yalin.fidoclient.net;

import com.android.volley.VolleyError;

/**
 * Created by YaLin on 2015/7/20.
 */
public class MyVolleyError extends VolleyError {

    public MyVolleyError( ErrorCodeConstants errorConstant) {
        super(errorConstant.msg);
    }
}
