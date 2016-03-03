/*
 * Copyright 2016 YaLin Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
