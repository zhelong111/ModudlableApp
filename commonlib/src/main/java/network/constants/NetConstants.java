package network.constants;

import util.AppUtil;

public class NetConstants {

          public static String BASE_URL = "http://10.1.30.6:9008/"; // ToB测试
//        public static String BASE_URL = "http://api.prev.xxlimageim.com/";//ToB预发布
    //      public static String BASE_URL = "http://apitob.xxlimageim.com/"; // ToB线上

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    static {
        if (AppUtil.isRelease()) {
            BASE_URL = "http://apitob.xxlimageim.com/";
        } else {
            BASE_URL = "http://10.1.30.6:9008/";
        }
    }

    /**
     * 实际URL = BASE_URL + URL.xxx
     */
    public interface URL {

        /**
         * 获取Token
         **/
        String TOKEN = "api/getToken";
    }
}
