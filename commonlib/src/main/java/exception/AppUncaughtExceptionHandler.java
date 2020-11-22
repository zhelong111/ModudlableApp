package exception;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import data.Constants;
import util.AppUtil;
import util.FileUtil;

public class AppUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {


    //程序的Context对象
    private Context applicationContext;

    private volatile boolean crashing;

    /**
     * 日期格式器
     */
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 单例
     */
    private static AppUncaughtExceptionHandler sAppUncaughtExceptionHandler;

    public static synchronized AppUncaughtExceptionHandler getInstance() {
        if (sAppUncaughtExceptionHandler == null) {
            synchronized (AppUncaughtExceptionHandler.class) {
                if (sAppUncaughtExceptionHandler == null) {
                    sAppUncaughtExceptionHandler = new AppUncaughtExceptionHandler();
                }
            }
        }
        return sAppUncaughtExceptionHandler;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        applicationContext = context.getApplicationContext();
        crashing = false;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // TODO 上传上次的报错信息，成功后删除本地错误日志
        //删除之前的log文件
        deleteLogDir();
    }

    private void deleteLogDir() {
        FileUtil.deleteFile(new File(Constants.LOG_CATCH_DIRECTORY));
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (crashing) {
            return;
        }
        crashing = true;

        // 打印异常信息
        ex.printStackTrace();
        // 我们没有处理异常 并且默认异常处理不为空 则交给系统处理
        if (!handleException(ex) && mDefaultHandler != null) {
            // 系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
        byebye();
    }

    private void byebye() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            // 异常信息
            String crashReport = getCrashReport(applicationContext, ex);
//            //上报异常日志到钉钉
//            DingDingUtil.sendDingDingMsg(crashReport);
            // 保存到sd卡
            saveExceptionToSdcard(crashReport);
//            // 提示对话框
//            showPatchDialog();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void showPatchDialog() {
//        Intent intent = PatchDialogActivity.newIntent(applicationContext, getApplicationName(applicationContext), null);
//        applicationContext.startActivity(intent);
    }

    private String getApplicationName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        String name = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(
                    context.getApplicationInfo().packageName, 0);
            name = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (final PackageManager.NameNotFoundException e) {
            String[] packages = context.getPackageName().split(".");
            name = packages[packages.length - 1];
        }
        return name;
    }

    /**
     * 获取异常信息
     */
    private String getCrashReport(Context context, Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        PackageInfo pinfo = getLocalPackageInfo(context);
        if (pinfo != null) {
            if (ex != null) {
                //app版本信息
                exceptionStr.append("App Version：" + pinfo.versionName);
                exceptionStr.append("_" + pinfo.versionCode + "\n");

                //手机系统信息
                exceptionStr.append("OS Version：" + Build.VERSION.RELEASE);
                exceptionStr.append("_");
                exceptionStr.append(Build.VERSION.SDK_INT + "\n");

                //手机制造商
                exceptionStr.append("Vendor: " + Build.MANUFACTURER + "\n");

                //手机型号
                exceptionStr.append("Model: " + Build.MODEL + "\n");
                exceptionStr.append("Mac: " + AppUtil.getMacAddress() + "\n");

                String errorStr = ex.getLocalizedMessage();
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.getMessage();
                }
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.toString();
                }
                exceptionStr.append("Exception: " + errorStr + "\n");
                StackTraceElement[] elements = ex.getStackTrace();
                if (elements != null) {
                    for (int i = 0; i < elements.length; i++) {
                        exceptionStr.append(elements[i].toString() + "\n");
                    }
                }
            } else {
                exceptionStr.append("no exception. Throwable is null\n");
            }
            return exceptionStr.toString();
        } else {
            return "";
        }
    }

    /**
     * 获取自身App安装包信息
     */
    private PackageInfo getLocalPackageInfo(Context context) {
        return getPackageInfo(context, context.getPackageName());
    }

    /**
     * 获取App安装包信息
     */
    private PackageInfo getPackageInfo(Context context, String packageName) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }


    /**
     * 保存错误报告到sd卡
     *
     * @param errorReason
     */
    private void saveExceptionToSdcard(String errorReason) {
        try {
            Log.e("CrashReport", "AppUncaughtExceptionHandler执行了一次");
            String time = mFormatter.format(new Date());
            String fileName = "Crash-" + time + ".log";
            if (SdcardConfig.getInstance().hasSDCard()) {
                String path = Constants.LOG_CATCH_DIRECTORY;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(errorReason.getBytes());
                fos.close();
            }
        } catch (Exception e) {
            Log.e("CrashReport", "an error occured while writing file..." + e.getMessage());
        }
    }

}
