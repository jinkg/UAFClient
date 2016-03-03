package com.yalin.fidoclient.tlv;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by YaLin on 2016/1/21.
 */
public class Tags {

    private Map<Integer, Tag> tags = new HashMap<Integer, Tag>();

    public void add(Tag t) {
        tags.put(t.id, t);
    }

    public void addAll(Tags all) {
        tags.putAll(all.getTags());
    }

    public Map<Integer, Tag> getTags() {
        return tags;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Entry<Integer, Tag> tag : tags.entrySet()) {
            res.append(", ");
            res.append(tag.getValue().toString());
        }
        if (res.length() > 0) {
            return "{" + res.substring(1) + "}";
        } else {
            return "{}";
        }

    }

    public String toUAFV1TLV() {
        return null;
    }
}
