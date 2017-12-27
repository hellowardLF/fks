package tv.qishi.milian.utils;

import android.util.Log;

/**
 * Log tools
 * Created by fengjh on 2015/6/8.
 */
public class MyLogUtils {
    private static final String TAG = "hxj";

    private static final boolean IS_VERBOSE = true;
    private static final boolean IS_INFO = true;
    private static final boolean IS_WARN = true;
    private static final boolean IS_DEBUG = true;
    private static final boolean IS_ERROR = true;
    private static final boolean IS_PRINT = true;

    public static final void v(String msg) {
        if (IS_VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    public static final void i(String msg) {
        if (IS_INFO) {
            Log.i(TAG, msg);
        }
    }

    public static final void w(String msg) {
        if (IS_WARN) {
            Log.w(TAG, msg);
        }
    }

    public static final void d(String msg) {
        if (IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static final void e(String msg) {
        if (IS_ERROR) {
            Log.e(TAG, msg);
        }
    }

    public static final void v(String tag, String msg) {
        if (IS_VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static final void i(String tag, String msg) {
        if (IS_INFO) {
            Log.i(tag, msg);
        }
    }

    public static final void w(String tag, String msg) {
        if (IS_WARN) {
            Log.w(tag, msg);
        }
    }

    public static final void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static final void e(String tag, String msg) {
        if (IS_ERROR) {
            Log.e(tag, msg);
        }
    }

    public static final void e(String tag, String msg, Throwable tr) {
        if (IS_ERROR) {
            Log.e(tag, msg, tr);
        }
    }

    public static final void print(String msg) {
        if (IS_PRINT) {
            System.out.println(msg);
        }
    }
}
