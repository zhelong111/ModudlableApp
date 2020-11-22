package network.httpclient;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.apache.http.message.BasicNameValuePair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ApiHelper {

    public static String APP_ID = "10710";
    public static String APP_KEY = "!W7iwls@B8q2RKz&CJipZNY9n9Me0H@q";

    public static String createSignUrl(String url,
                                       List<BasicNameValuePair> params, String sign) {
        String signedUrl = null;
        Collections.sort(params, asciiComparator);
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        for (int i = 0; i < params.size(); i++) {
            BasicNameValuePair p = params.get(i);
            sb.append(p.getName() + "=" + p.getValue() + "&");
        }
        signedUrl = sb.toString();
        signedUrl = signedUrl + "sign=" + sign;

        return signedUrl;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版名称
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // public static String createSign(String url, List<BasicNameValuePair>
    // params) {
    // Collections.sort(params, asciiComparator);
    // StringBuilder sb = new StringBuilder();
    // sb.append(url).append("?");
    // for (int i = 0; i < params.size(); i++) {
    // BasicNameValuePair p = params.get(i);
    // sb.append(p.getName() + "=" + p.getValue() + "&");
    // }
    // sb.append(APP_KEY);
    // String sign = MD5Util.string2MD5(sb.toString());
    // return sign;
    // }

    private static Comparator<BasicNameValuePair> asciiComparator = new Comparator<BasicNameValuePair>() {

        @Override
        public int compare(BasicNameValuePair lhs, BasicNameValuePair rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    };

    private static Map<String, String> createMapParams(
            List<BasicNameValuePair> params) {
        Map<String, String> mParams = new TreeMap<String, String>();
        for (BasicNameValuePair p : params) {
            mParams.put(p.getName(), p.getValue());
        }
        return mParams;
    }

    private static String concatParams(Map<String, String> urlParam) {
        Object[] key_arr = urlParam.keySet().toArray();
        Arrays.sort(key_arr);
        String str = "";
        for (Object key : key_arr) {
            String val = urlParam.get(key);
            str += "&" + key + "=" + val;
        }
        return str.replaceFirst("&", "");
    }

    private static String byte2hex(byte[] b) {
        StringBuffer buf = new StringBuffer();
        int i;
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    /**
     * 生成签名
     *
     * @param pathUrl
     * @param params
     * @param appKey
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String genSig(String pathUrl, Map<String, String> params,
                                 String appKey) throws NoSuchAlgorithmException {
        String str = concatParams(params);
        str = pathUrl + "?" + str + "&" + appKey;
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byte2hex(md.digest(str.getBytes()));
    }

    public static String createSign(String pathUrl,
                                    List<BasicNameValuePair> params, String appKey) {
        Map<String, String> mParams = createMapParams(params);
        try {
            return genSig(pathUrl, mParams, appKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
