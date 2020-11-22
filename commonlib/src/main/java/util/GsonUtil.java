package util;

import com.google.gson.Gson;

/**
 * Created by Yuaihen.
 * on 2019/12/18
 */
public class GsonUtil {

    private static Gson instance;

    private GsonUtil() {
    }

    public static Gson getInstance() {
        if (instance == null) {
            synchronized (GsonUtil.class) {
                if (instance == null) {
                    instance = new Gson();
                }
            }
        }
        return instance;
    }

}
