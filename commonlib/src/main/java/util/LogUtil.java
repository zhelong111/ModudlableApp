package util;

import android.text.TextUtils;
import android.util.Log;
import com.bruce.modulableapp.commonlib.BuildConfig;

/**
 * 自定义log日志工具类
 */
public class LogUtil {

    /****************** 日志级别 ******************/
    public static final int VERBOSE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static final int ASSERT = 7;
    /****************** 日志级别 ******************/

    /**
     * 是否是发布版本
     */
     public static final boolean ISRELEASE = "release".equals(BuildConfig.BUILD_TYPE);

    public static void v(String tag, String msg) {
        if (ISRELEASE) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (ISRELEASE) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        Log.d(tag, msg);
    }


    public static void i(String tag, String msg) {
        if (ISRELEASE) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        Log.i(tag, msg);
    }


    public static void w(String tag, String msg) {
        if (ISRELEASE) {
            return;
        }
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (ISRELEASE) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        Log.e(tag, msg);

    }
}
