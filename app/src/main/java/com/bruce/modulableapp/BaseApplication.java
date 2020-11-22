package com.bruce.modulableapp;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import network.retrofit.RetrofitManager;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
       initRoute();
       initNetwork();
    }

    private void initRoute() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this);
    }

    private void initNetwork() {
        RetrofitManager.init("https://www.google.com");
    }
}
