package network.httpclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import data.Constants;
import network.constants.NetConstants;
import util.AppUtil;
import util.LogUtil;
import util.UserUtil;

/**
 * Http网络封装最底层封装
 *
 * @author Administrator
 */
@SuppressWarnings("deprecation")
public class AsyncHttpRequest extends AsyncTask<String, Integer, String> {

    private String url;

    private Context context;

    private AsyncHttpCallback callback;

    private List<BasicNameValuePair> params;
    private String requestMethod;
    private final String POST = "Post";
    private final String GET = "Get";
    private final String PUT = "Put";
    private final String DELETE = "Delete";
    private final String OPTION = "Options";
    private final String HEAD = "Head";

    private String msg;

    private boolean isNeedWait;

    private int index; // 多个请求时，标识请求顺序

    public enum RequestType {
        POST, GET
    }

    private RequestType requestType;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public AsyncHttpRequest(Context context, String msg, String url, List<BasicNameValuePair> params, AsyncHttpCallback callback) {
        this.context = context;
        this.url = NetConstants.BASE_URL + url;
        this.callback = callback;
        this.params = params;
        this.msg = msg;
        this.isNeedWait = false;
        this.index = 0;
        this.requestType = RequestType.POST;

    }

    public AsyncHttpRequest(Context context, int index, String msg, String url, List<BasicNameValuePair> params, AsyncHttpCallback callback) {
        this.context = context;
        this.url = NetConstants.BASE_URL + url;
        this.callback = callback;
        this.params = params;
        this.msg = msg;
        this.isNeedWait = false;
        this.index = index;
        this.requestType = RequestType.POST;
    }

    public void setNeedWait(boolean isNeedWait) {
        this.isNeedWait = isNeedWait;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    protected void onPreExecute() {
        //		if (!TextUtils.isEmpty(msg))
        //			PromptManager.showLoadingDialog(context, msg);
        if (callback != null) {
            callback.onStart();
        }
    }

    @Override
    protected String doInBackground(String... arg0) {
        long startTime = System.currentTimeMillis();
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(new BasicNameValuePair(Constants.VERSION, ApiHelper.getVersionCode(context) + ""));
        params.add(new BasicNameValuePair(Constants.MAC, AppUtil.getMacAddress()));
        params.add(new BasicNameValuePair(Constants.TOKEN, UserUtil.getToken()));
        LogUtil.d("mac", AppUtil.getMacInfo() + ",," + AppUtil.getMacAddress());
        List<BasicNameValuePair> ecodeParams = new ArrayList<>();
        for (BasicNameValuePair valuePair : params) {
            try {
                String oValue = valuePair.getValue();
                if (!TextUtils.isEmpty(oValue)) {
                    String value = URLEncoder.encode(oValue, "UTF-8");
                    ecodeParams.add(new BasicNameValuePair(valuePair.getName(), value));
                } else {
                    ecodeParams.add(new BasicNameValuePair(valuePair.getName(), ""));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String signUrl = url;
        if (url != null) {
            if (url.startsWith("https://")) {
                signUrl = url.replace("https://", "");
            } else if (url.startsWith("http://")) {
                signUrl = url.replace("http://", "");
            }
        }
        String sign = ApiHelper.createSign(signUrl, ecodeParams, ApiHelper.APP_KEY);
        ecodeParams.add(new BasicNameValuePair("sign", sign));

        String result;
        if (requestType == RequestType.POST) {
            HttpPost post = EnhancedHttpHelper.getPost(url, ecodeParams);
            result = EnhancedHttpHelper.getString(post, 1);
            requestMethod = POST;
        } else {
            HttpGet get = EnhancedHttpHelper.getGet(url, ecodeParams);
            requestMethod = GET;
            result = EnhancedHttpHelper.getString(get, 1);
        }

        if (isNeedWait) {
            while (System.currentTimeMillis() - startTime < 400) {
            }
        }
                LogUtil.d("data params", params.toString());
                LogUtil.d("result", "url:" + url + "\n" + "data:" + result);
//        ViseLog.d("url --> " + url + "\nparams --> " + params.toString() + "\n data --> \n");
//        ViseLog.json(result);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        //		TipUtil.toast("requst info: " + url + "->" + JsonUtil.gson.toJson(params));
        if (callback != null && context != null) {
            callback.onEnd(result);
        }
    }

    @SuppressLint("NewApi")
    public void start() {
        this.executeOnExecutor(ThreadPoolManager.getInstance().getThreadPool());
    }

}
