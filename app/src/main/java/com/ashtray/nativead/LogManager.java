package com.ashtray.nativead;

import android.util.Log;

public class LogManager {

    private static final String TAG = "[mg]";

    public static void d(String tag, String message) {
        if(BaseApplication.getInstance().enableLogPrinting()) {
            Log.d(TAG + "[" + tag + "]", message);
        }
    }

    public static void e(String tag, String message) {
        if(BaseApplication.getInstance().enableLogPrinting()) {
            Log.e(TAG + "[" + tag + "]", message);
        }
    }

}