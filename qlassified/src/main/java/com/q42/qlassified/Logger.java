package com.q42.qlassified;

import android.util.Log;

/**
 * Created by maz on 9/23/2017 AD.
 */

public class Logger {

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }
}
