package com.bruce.modulableapp;

import com.alibaba.android.arouter.launcher.ARouter;

import common.BaseApplication;
import exception.AppUncaughtExceptionHandler;
import exception.SdcardConfig;
import network.retrofit.RetrofitManager;
import util.CacheUtils;

public class CustomApplication extends BaseApplication {

    @Override
    public void init() {
        initRoute();
        initNetwork();
        initCrash();
        CacheUtils.init(this);
    }


    private void initRoute() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode
        }
        ARouter.init(this);
    }

    private void initNetwork() {
        RetrofitManager.init(this);
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

}
