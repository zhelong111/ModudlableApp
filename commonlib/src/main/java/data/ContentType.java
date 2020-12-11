package data;

import java.util.HashMap;
import java.util.Map;

/************************************************
 * Function：content-type和文件suffix转换
 * Author: Bruce.Zhou
 * Date: 2020/12/11
 * Email: zhoul5@bngrp.com
 *************************************************/
public class ContentType {

    private static Map<String, String> contentType2SuffixMap = new HashMap<>();

    static {
        contentType2SuffixMap.put("application/vnd.android.package-archive", ".apk");
        contentType2SuffixMap.put("image/png", ".png");
        contentType2SuffixMap.put("image/jpg", ".jpg");
        contentType2SuffixMap.put("image/jpeg", ".jpeg");
        contentType2SuffixMap.put("image/gif", ".gif");
    }

    public static String getSuffix(String contentType) {
        return contentType2SuffixMap.get(contentType);
    }
}
