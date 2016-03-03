package com.yalin.fidoclient.tlv;


import android.util.Base64;

/**
 * Created by YaLin on 2016/1/21.
 */
public class Tag {
    public int statusId = 0x00;
    public int id;
    public int lenght;
    public byte[] value;

    public String toString() {
        String ret = "Tag id:" + id;
        ret = ret + " Tag name: " + TagsEnum.get(id);
        if (value != null) {
            ret = ret + " Tag value:" + Base64.encode(value, Base64.URL_SAFE);
        }
        return ret;
    }

}
