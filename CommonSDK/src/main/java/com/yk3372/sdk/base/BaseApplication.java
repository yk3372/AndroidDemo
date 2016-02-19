package com.yk3372.sdk.base;

import android.app.Application;
import android.util.Log;

/**
 * Created by yukai on 16-2-22.
 */
public class BaseApplication extends Application {
    protected static boolean IS_DEBUG = false;
    private static final String TAG = "Demo";
    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Application getInstance() {
        return mInstance;
    }

    public static void Log(String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void Loge(String msg) {
        Log.e(TAG, msg);
    }
}
