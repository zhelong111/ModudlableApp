package com.bruce.modulableapp;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

import exception.AppUncaughtExceptionHandler;
import exception.SdcardConfig;
import network.retrofit.RetrofitManager;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    private void init() {
        initRoute();
        initNetwork();
        initCrash();
    }

    private void initRoute() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode
        }
        ARouter.init(this);
    }

    private void initNetwork() {
        RetrofitManager.init(this, "https://www.google.com");
    }

    private void initCrash() {
        /**
         * 初始化异常捕获
         */
        // 初始化文件目录
        SdcardConfig.getInstance().initSdcard();
        // 捕捉异常
        AppUncaughtExceptionHandler.getInstance().init(this);
    }

    private static Context getContext() {
        return context;
    }

}
