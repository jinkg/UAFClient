package com.yalin.fidoclient.msg;


/**
 * Created by YaLin on 2016/1/21.
 */
public class Version {
    public int major;
    public int minor;

    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public static Version getCurrentSupport() {
        return new Version(1, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Version) {
            if (((Version) o).major == this.major && ((Version) o).minor == this.minor) {
                return true;
            }
        }
        return false;
    }
}
