package com.example.sabziwala.Utilities;


import android.util.Log;

public final class AppLogger {

    private AppLogger() {
        // This class is not publicly instantiable
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

}
