package com.q42.qlassified;

import android.util.Log;

/**
 * Created by maz on 9/23/2017 AD.
 */

public class Logger {

    private static boolean isEnable = false;
    public static void setLogEnable(boolean enable) {
        isEnable = enable;
    }

    public static void d(String tag, String message) {
        if(isEnable) Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        if(isEnable) Log.e(tag, message);
    }

    public static void i(String tag, String message) {
        if(isEnable) Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        if(isEnable) Log.w(tag, message);
    }
}
