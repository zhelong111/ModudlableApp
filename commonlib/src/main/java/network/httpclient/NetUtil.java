package network.httpclient;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import network.httpclient.listener.NetCallback;
import network.httpclient.listener.NetListCallback;
import ui.tip.TipUtil;

/**
 * Http网络请求的封装类
 *
 * @author Bruce
 */
public class NetUtil {

    private static Gson gson = new Gson();
    private static String CODE = "code";
    private static String MSG = "message";
    private static String DATA = "data";
    private static String LIST = "list";
    private static final int CODE_PARSE_EXCEPTION = 100001;

    private static void queryObject(final Context context, String url, AsyncHttpRequest.RequestType requestType, List<BasicNameValuePair> params, final NetCallback<String> callback) {
        AsyncHttpRequest request = new AsyncHttpRequest(context, null, url, params, new AsyncHttpCallback() {

            @Override
            public void onStart() {
                if (!NetCommon.isNetworkConnected(context)) {
                    //					PromptManager.toast(context, "网络未连接");
                    TipUtil.show(context, "网络未连接");
                    return;
                }
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            public void onEnd(String result) {
                Log.e("net", result + "");
                if (callback != null) {
                    try {
                        if (EnhancedHttpHelper.Status.IO_EXCEPTION.equals(result)
                                || EnhancedHttpHelper.Status.CONNECT_TIME_OUT.equals(result)
                                || EnhancedHttpHelper.Status.REQUEST_FAIL.equals(result)
                                || EnhancedHttpHelper.Status.RESPONSE_FAIL.equals(result)) {
                            callback.onFail(Integer.valueOf(result), "IO网络异常");
                        } else if (EnhancedHttpHelper.Status.SERVER_ERROR.equals(result)) {
                            callback.onFail(Integer.valueOf(result), "镜子后台正在维护升级中，请您稍后再试吧！");
                        } else {
                            JSONObject resultObj = new JSONObject(result);
                            int code = resultObj.getInt(CODE);
                            if (code == 0) {
                                String data = resultObj.optString(DATA);
                                callback.onSuccess(data);
                            } else if (code == 9009) {
                                //							UserUtil.logout(context);
                            } else {
                                String msg = resultObj.optString(MSG);
                                callback.onFail(code, msg);
                            }
                        }
                    } catch (JSONException e) {
                        callback.onFail(CODE_PARSE_EXCEPTION, "解析异常" + result);
                    }
                }
            }
        });
        request.setRequestType(requestType);
        request.start();
    }

    private static <T> void queryList(final Context context, String url, AsyncHttpRequest.RequestType requestType,
                                      List<BasicNameValuePair> params, final NetListCallback<T> callback, final Class<T> clazz) {
        AsyncHttpRequest request = new AsyncHttpRequest(context, null, url, params, new AsyncHttpCallback() {

            @Override
            public void onStart() {
                if (!NetCommon.isNetworkConnected(context)) {
                    TipUtil.show(context, "网络未连接");
                    return;
                }
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            public void onEnd(String result) {
                if (callback != null) {
                    try {
                        if (EnhancedHttpHelper.Status.IO_EXCEPTION.equals(result)
                                || EnhancedHttpHelper.Status.CONNECT_TIME_OUT.equals(result)
                                || EnhancedHttpHelper.Status.REQUEST_FAIL.equals(result)
                                || EnhancedHttpHelper.Status.RESPONSE_FAIL.equals(result)) {
                            callback.onFail(Integer.valueOf(result), "IO网络异常");
                        } else if (EnhancedHttpHelper.Status.INTERFACE_FAIL.equals(result)) {
                            callback.onFail(Integer.valueOf(result), "服务接口异常");
                        } else if (EnhancedHttpHelper.Status.SERVER_ERROR.equals(result)) {
                            callback.onFail(Integer.valueOf(result), "镜子后台正在维护升级中，请您稍后再试吧！");
                        } else {
                            JSONObject resultObj = new JSONObject(result);
                            int code = resultObj.getInt(CODE);
                            if (code == 0) {
                                JSONObject dataObj = resultObj.getJSONObject(DATA);
                                JSONArray dataArr = dataObj.optJSONArray(LIST);
                                List<T> dataList = new ArrayList<T>();
                                if (dataArr != null) {
                                    for (int i = 0; i < dataArr.length(); i++) {
                                        T data = gson.fromJson(dataArr.getJSONObject(i).toString(), clazz);
                                        dataList.add(data);
                                    }
                                }
                                callback.onSuccess(dataList);
                            } else if (code == 9009) {
                                //							UserUtil.logout(context);
                            } else {
                                String msg = resultObj.optString(MSG);
                                callback.onFail(code, msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFail(NetCommon.CODE_PARSE_EXCEPTION, "解析异常");
                    }
                }
            }
        });
        request.setRequestType(requestType);
        request.start();
    }

    public static <T> void queryObject(final Context context, String url, AsyncHttpRequest.RequestType requestType,
                                       List<BasicNameValuePair> params, final NetCallback<T> callback, final Class<T> clazz) {
        AsyncHttpRequest request = new AsyncHttpRequest(context, null, url, params, new AsyncHttpCallback() {

            @Override
            public void onStart() {
                if (!NetCommon.isNetworkConnected(context)) {
                    TipUtil.show(context, "网络未连接");
                    return;
                }
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            public void onEnd(String result) {
                if (callback != null) {
                    try {
                        if (EnhancedHttpHelper.Status.IO_EXCEPTION.equals(result)
                                || EnhancedHttpHelper.Status.CONNECT_TIME_OUT.equals(result)
                                || EnhancedHttpHelper.Status.REQUEST_FAIL.equals(result)
                                || EnhancedHttpHelper.Status.RESPONSE_FAIL.equals(result)) {
                            callback.onFail(Integer.valueOf(result), "IO网络异常");
                        } else if (EnhancedHttpHelper.Status.SERVER_ERROR.equals(result)) {
                            callback.onFail(Integer.valueOf(result), "镜子后台正在维护升级中，请您稍后再试吧！");
                            TipUtil.showBigMsg(context, "镜子后台正在维护升级中，请您稍后再试吧！");
                        } else {
                            JSONObject resultObj = new JSONObject(result);
                            int code = resultObj.getInt(CODE);
                            if (code == 0) {
                                String dataStr = resultObj.optString(DATA);
                                if (!TextUtils.isEmpty(dataStr)) {
                                    T data = gson.fromJson(dataStr, clazz);
                                    callback.onSuccess(data);
                                } else {
                                    callback.onFail(200, "No data valuable");
                                }
                            } else if (code == 9009) {
                                //							UserUtil.logout(context);
                            } else {
                                String msg = resultObj.optString(MSG);
                                callback.onFail(code, msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        request.setRequestType(requestType);
        request.start();
    }


    /**
     * POST方式请求对象
     *
     * @param context
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口（可以为NULL）
     */
    public static void postObject(Context context, String url, List<BasicNameValuePair> params, final NetCallback<String> callback) {
        queryObject(context, url, AsyncHttpRequest.RequestType.POST, params, callback);
    }

    public static void postObject(Context context, String url, String params, final NetCallback<String> callback) {
        queryObject(context, url, AsyncHttpRequest.RequestType.POST, getParamList(params), callback);
    }

    /**
     * POST方式请求对象
     *
     * @param context
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口（可以为NULL）
     * @param clazz    要转化的类型的Class
     * @param <T>      要转化的类型
     */
    public static <T> void postObject(Context context, String url, List<BasicNameValuePair> params, final NetCallback<T> callback, final Class<T> clazz) {
        queryObject(context, url, AsyncHttpRequest.RequestType.POST, params, callback, clazz);
    }

    public static <T> void postObject(Context context, String url, String params, final NetCallback<T> callback, final Class<T> clazz) {
        queryObject(context, url, AsyncHttpRequest.RequestType.POST, getParamList(params), callback, clazz);
    }

    /**
     * GET方式请求对象
     *
     * @param context
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口（可以为NULL）
     */
    public static void getObject(Context context, String url, List<BasicNameValuePair> params, final NetCallback<String> callback) {
        queryObject(context, url, AsyncHttpRequest.RequestType.GET, params, callback);
    }

    public static void getObject(Context context, String url, String params, final NetCallback<String> callback) {
        queryObject(context, url, AsyncHttpRequest.RequestType.GET, getParamList(params), callback);
    }


    //    /**
    //     * GET方式请求对象
    //     * @param context
    //     * @param url 请求地址
    //     * @param params 请求参数
    //     * @param callback 回调接口（可以为NULL）
    //     * @param clazz 要转化的类型的Class
    //     * @param <T> 要转化的类型
    //     */
    public static <T> void getObject(Context context, String url, List<BasicNameValuePair> params, final NetCallback<T> callback, final Class<T> clazz) {
        queryObject(context, url, AsyncHttpRequest.RequestType.GET, params, callback, clazz);
    }

    public static <T> void getObject(Context context, String url, String params, final NetCallback<T> callback, final Class<T> clazz) {
        queryObject(context, url, AsyncHttpRequest.RequestType.GET, getParamList(params), callback, clazz);
    }

    /**
     * POST方式请求列表
     *
     * @param context
     * @param url
     * @param params
     * @param callback
     * @param clazz    {code:0,msg:'ok',data:{list:[{name:bruce,age:12},{name:bruce,age:12}]}}
     */
    public static <T> void postList(final Context context, String url, List<BasicNameValuePair> params, final NetListCallback<T> callback, final Class<T> clazz) {
        queryList(context, url, AsyncHttpRequest.RequestType.POST, params, callback, clazz);
    }

    /**
     * GET方式请求列表
     *
     * @param context
     * @param url
     * @param params
     * @param callback
     * @param clazz
     */
    public static <T> void getList(final Context context, String url, List<BasicNameValuePair> params, final NetListCallback<T> callback, final Class<T> clazz) {
        queryList(context, url, AsyncHttpRequest.RequestType.GET, params, callback, clazz);
    }

    public static <T> void getList(final Context context, String url, String params, final NetListCallback<T> callback, final Class<T> clazz) {
        queryList(context, url, AsyncHttpRequest.RequestType.GET, getParamList(params), callback, clazz);
    }

    private static List<BasicNameValuePair> getParamList(String params) {
        String value = "";
        List<BasicNameValuePair> paramList = new ArrayList<>();
        if (!TextUtils.isEmpty(params)) {
            String[] pairs = params.split("&");
            for (String pair : pairs) {
                String[] p = pair.split("=");
                String name = p[0];
                if (p.length >= 2) {
                    value = p[1];
                }
                paramList.add(new BasicNameValuePair(name, value));
            }
        }
        return paramList;
    }
}