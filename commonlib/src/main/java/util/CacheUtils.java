package util;

import android.content.Context;

import com.tencent.mmkv.MMKV;

/**
 * @Description 数据缓存工具类
 */
public class CacheUtils {

    private static final String TAG = CacheUtils.class.getName();

    public static void init(Context context) {
        String rootDir = MMKV.initialize(context);
        LogUtil.d(TAG, "mmkv root: " + rootDir);
    }

    /**
     * MMKV支持的数据类型
     * 支持以下 Java 语言基础类型：
     * boolean、int、long、float、double、byte[]
     * 支持以下 Java 类和容器：
     * String、Set<String>
     * 任何实现了Parcelable的类型
     */
    public static MMKV getPreferences() {
        return MMKV.defaultMMKV();
    }


    public static void clear() {
        getPreferences().clearAll();
    }

    public static void setBean(String key, Object obj) {
        String json = GsonUtil.getInstance().toJson(obj);
        getPreferences().encode(key, json);
    }

    public static <T> T getBean(String key, Class<T> clazz) {
        String json = getPreferences().decodeString(key);
        try {
            return GsonUtil.getInstance().fromJson(json, clazz);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

        return null;
    }

    public static void setBoolean(String key, boolean value) {
        getPreferences().encode(key, value);
    }

    public static boolean getBoolean(String key) {
        return getPreferences().decodeBool(key);
    }

    public static void setString(String key, String value) {
        getPreferences().encode(key, value);
    }

    public static String getString(String key) {
        return getPreferences().decodeString(key, "");
    }

    public static void setInt(String key, int value) {
        getPreferences().encode(key, value);
    }

    public static int getInt(String key) {
        return getPreferences().decodeInt(key);
    }

    public static int getInt(String key, int defValue) {
        return getPreferences().decodeInt(key, defValue);
    }

    public static void setDouble(String key, double value) {
        getPreferences().encode(key, value);
    }

    public static double getDouble(String key) {
        return getPreferences().decodeDouble(key);
    }

    public static void setLong(String key, long value) {
        getPreferences().encode(key, value);
    }

    public static long getLong(String key, Long defValue) {
        return getPreferences().decodeLong(key, defValue);
    }

    public static void setFloat(String key, float value) {
        getPreferences().encode(key, value);
    }

    public static float getFloat(String key, float defValue) {
        return getPreferences().decodeFloat(key, defValue);
    }

    //    @SuppressLint("NewApi")
    //    public static void setStringArray(Context context, String key, List<String> values) {
    //        if (mSharedPreferences == null) {
    //            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
    //        }
    //        if (values == null || values.size() == 0) {
    //            mSharedPreferences.edit().putString(key, "").apply();
    //        } else {
    //            String str = "";
    //            for (int i = 0; i < values.size(); i++) {
    //                if (values.get(i) != null) {
    //                    str += values.get(i) + "#-#";
    //                }
    //            }
    //            String subStr = str.substring(0, str.length() - 3);
    //            mSharedPreferences.edit().putString(key, subStr).apply();
    //        }
    //    }
    //
    //    @SuppressLint("NewApi")
    //    public static List<String> getStringArray(Context context, String key) {
    //        if (mSharedPreferences == null) {
    //            mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
    //        }
    //        List<String> resultList = new ArrayList<String>();
    //        String str = mSharedPreferences.getString(key, "");
    //        if ("".equals(str)) {
    //            return resultList;
    //        }
    //        String[] splitStrs = str.split("#-#");
    //        if (splitStrs != null && splitStrs.length != 0) {
    //            for (int i = 0; i < splitStrs.length; i++) {
    //                resultList.add(splitStrs[i]);
    //            }
    //        }
    //        return resultList;
    //    }


}
