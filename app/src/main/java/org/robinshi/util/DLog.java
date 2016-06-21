package org.robinshi.util;

import android.util.Log;

public class DLog {
    public static boolean DEBUG = true;

    public static void d(String tag, String msg) {
        if (DEBUG) Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, Exception e) {
        e(tag, e.getMessage(), e);
    }

    public static void e(String tag, Throwable throwable) {
        e(tag, throwable.getMessage(), throwable);
    }


    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
    }

    public static void i(String tag, String msg) {
        if (DEBUG) Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, Exception e) {
        w(tag, e.getMessage(), e);
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (DEBUG) Log.w(tag, msg, e);
    }

}
