package com.yalin.fidoclient.msg;

import android.graphics.drawable.Drawable;

/**
 * Created by YaLin on 2016/2/26.
 */
public class AsmInfo {
    public String appName;
    public String pack;
    public Drawable icon;

    public AsmInfo appName(String appName) {
        this.appName = appName;
        return this;
    }

    public AsmInfo pack(String pack) {
        this.pack = pack;
        return this;
    }

    public AsmInfo icon(Drawable icon) {
        this.icon = icon;
        return this;
    }
}
