package com.jin.uitoolkit.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 雅麟 on 2015/3/24.
 */
public class Utils {
    private static final int ORIENTATION_HYSTERESIS = 5;
    private static String androidId = null;

    public static boolean isPhoneNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("^1[3578]{1}[0-9]{9}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String getHidePhoneNumber(String phone) {
        return phone.replace(phone.substring(3, 8), "***");
    }

    public static void openInput(Activity activity, View view) {
        if (activity == null || view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void closeInput(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken()
                , InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static int[] getViewCenterXY(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        location[0] += v.getWidth() / 2;
        location[1] -= v.getHeight() / 2;
        return location;
    }

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verName;

    }


    public static String getAndroidId(Context context) {
        if (androidId == null) {
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = wifi.getConnectionInfo();
//            androidId = info.getMacAddress();
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }

    public static boolean copy2Clipboard(String content, Context context) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("App Password", content);
        cmb.setPrimaryClip(clipData);
        return true;
    }


    public static Camera.Size getPropPictureSize(List<Camera.Size> list) {
        Collections.sort(list, new CameraSizeComparator());
        for (Camera.Size size : list) {
            if (size.width > 640) {
                return size;
            }
        }
        return list.get(0);
    }

    public static class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }

    public static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation = false;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

}
