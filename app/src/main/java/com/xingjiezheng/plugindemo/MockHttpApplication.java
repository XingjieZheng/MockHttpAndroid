package com.xingjiezheng.plugindemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by xingj
 * on 2017/4/3.
 */

public class MockHttpApplication extends Application {

    public static Context getContext() {
        return context;
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
