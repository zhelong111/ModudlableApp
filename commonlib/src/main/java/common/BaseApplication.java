package common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import util.LogUtil;

/************************************************
 * Function： 
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public abstract class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getName();
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!hasInit()) {
            context = this;
            init();
        }
    }

    public abstract void init();

    public static Context getContext() {
        return context;
    }


    /**
     * Init once
     */
    public boolean hasInit() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        String pkgName = getPackageName();
        LogUtil.d(TAG, "processAppName---" + processAppName);
        //默认的app会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就return掉
        if (processAppName == null ||!processAppName.equalsIgnoreCase(pkgName)) {
            LogUtil.d(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return true;
        }
        return false;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
