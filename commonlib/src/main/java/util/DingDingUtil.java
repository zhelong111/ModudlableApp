package util;

import android.text.TextUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yuaihen.
 * on 2018/11/28
 * 钉钉异常上报
 */
public class DingDingUtil {

    private static final String TAG = "DingDingUtil";

    private static final String sendUrl = "https://oapi.dingtalk.com/robot/send?access_token=c34ac6dac1b545e49b804a441bcd1ffdb568635651f2b5ab104ccb43b162d47b";

    private static String converMsgToJson(String msgContent) {
        if (!TextUtils.isEmpty(msgContent)) {
            try {
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("msgtype", "text");
                JSONObject contentParam = new JSONObject();
                contentParam.put("content", "Android B端: 你的程序出bug了，" + "\n" + msgContent);
                jsonParam.put("text", contentParam);
                LogUtil.d(TAG, jsonParam.toString());

                return jsonParam.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "";
    }


    public static void sendDingDingMsg(String msgContent) {
        String json = converMsgToJson(msgContent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // HttpClient 6.0被抛弃了
                    String result = "";
                    BufferedReader reader = null;
                    try {
                        URL url = new URL(sendUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setUseCaches(false);
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Charset", "UTF-8");
                        // 设置文件类型:
                        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        // 设置接收类型否则返回415错误
                        //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
                        conn.setRequestProperty("accept", "application/json");
                        // 往服务器里面发送数据
                        if (!TextUtils.isEmpty(json)) {
                            //                            String data = URLEncoder.encode(json, "utf-8");
                            byte[] writebytes = json.getBytes();
                            // 设置文件长度
                            conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                            OutputStream outwritestream = conn.getOutputStream();
                            outwritestream.write(json.getBytes());
                            outwritestream.flush();
                            outwritestream.close();
                        }
                        if (conn.getResponseCode() == 200) {
                            reader = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                            result = reader.readLine();

                        }

                        if (conn != null) {
                            conn.disconnect();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtil.d(TAG, result);
                } catch (Exception e) {
                    LogUtil.d(TAG, e.getLocalizedMessage());
                }
            }
        }).start();
    }
}
