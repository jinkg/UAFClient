package com.yalin.fidoclient.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by 雅麟 on 2015/3/27.
 */
public class RequestQueueHelper {
    private static RequestQueue sInstance;

    public static RequestQueue getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Volley.newRequestQueue(context);
        }
        return sInstance;
    }

    public static RequestQueue getInstance() {
        return sInstance;
    }
}
