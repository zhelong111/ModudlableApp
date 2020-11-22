package network.httpclient;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ui.tip.TipUtil;


/**
 * 增强型Http辅助类
 *
 * @author Bruce Zhou
 */
public class EnhancedHttpHelper {

    private static HttpClient httpClient;
    //	private static final String TAG = "HttpUtil";

    public interface Status {

        String IO_EXCEPTION = "00";

        String RESPONSE_FAIL = "11";

        String REQUEST_FAIL = "22";

        String CONNECT_TIME_OUT = "33";

        String RESPONSE_OK = "99";

        String SERVER_ERROR = "502";

        String INTERFACE_FAIL = "-1";
    }

    // 连接超时时间
    private static final int TIME_OUT = 25000;
    private static final int CONNECT_TIME_OUT = 10000;

    /**
     * 获得线程安全的HttpClient对象，能够适应多线程环境
     *
     * @return
     */
    public static synchronized HttpClient getHttpClient() {
        if (null == httpClient) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许主机的验证

                // 设置一些基本参数
                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, "UTF-8");
                HttpProtocolParams.setUseExpectContinue(params, true);
                HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) " + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
                // 超时设置
                /* 从连接池中取连接的超时时间 */
                ConnManagerParams.setTimeout(params, TIME_OUT);

                /* 连接超时 */
                HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIME_OUT);
                /* 请求超时 */
                HttpConnectionParams.setSoTimeout(params, TIME_OUT);

                // 设置我们的HttpClient支持HTTP和HTTPS两种模式
                SchemeRegistry schReg = new SchemeRegistry();
                schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schReg.register(new Scheme("https", sf, 443));

                // 使用线程安全的连接管理来创建HttpClient
                ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
                httpClient = new DefaultHttpClient(conMgr, params);
            } catch (Exception e) {
                e.printStackTrace();
                return new DefaultHttpClient();
            }
        }
        return httpClient;
    }

    public static class SSLSocketFactoryEx extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    /**
     * 获得Post请求对象
     *
     * @param uri    请求地址，也可以带参数
     * @param params 如果为null，则不添加由BasicNameValue封装的参数
     * @return
     */
    public static HttpPost getPost(String uri, List<BasicNameValuePair> params) {
        HttpPost post = new HttpPost(uri);
        try {
            if (params != null) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(entity);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * @param uri    例如：http://localhost/xy/dd
     * @param params
     * @return
     */
    public static HttpGet getGet(String uri, List<BasicNameValuePair> params) {
        uri += "?" + URLEncodedUtils.format(params, "UTF-8");
        HttpGet get = new HttpGet(uri);
        return get;
    }

    /**
     * post请求
     *
     * @param uri
     * @param data
     * @return
     */
    public static HttpPost getBytePost(String uri, byte[] data) {
        HttpPost post = new HttpPost(uri);

        ByteArrayEntity entity = new ByteArrayEntity(data);

        //		entity.setContentEncoding(HTTP.UTF_8);

        post.setEntity(entity);

        return post;
    }


    /**
     * 用户使用的方法 功能：从服务器获得字符串
     *
     * @param post
     * @return
     */
    public static String getString(HttpPost post) {

        HttpClient httpClient = getHttpClient();
        HttpResponse response;
        try {
            response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != 502) {
                post.abort();
                return null;
            }
            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户使用的方法 功能：请求服务器，返回字符串
     *
     * @param post         post 请求对象
     * @param requestLimit 请求失败限制次数
     * @return
     */
    public static String getString(HttpPost post, int requestLimit) {

        if (requestLimit < 1) {
            return null;
        }
        HttpResponse response;
        int currCount = 0; // 当前请求次数
        String result = null;

        while (currCount < requestLimit) {

            HttpClient httpClient = getHttpClient();
            currCount++;
            try {
                post.addHeader("charset", HTTP.UTF_8);
                response = httpClient.execute(post);
                int statusCode = response.getStatusLine().getStatusCode();
                Log.i("code", "" + statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                } else if (statusCode == -1) {
                    post.abort();
                    result = Status.INTERFACE_FAIL;
                } else if (statusCode == 502) {
                    post.abort();
                    result = Status.SERVER_ERROR;
                } else {
                    post.abort();
                    result = Status.RESPONSE_FAIL;
                }
            } catch (ClientProtocolException e) {
                if (currCount > requestLimit) {
                    result = Status.REQUEST_FAIL;
                    break;
                }
            } catch (IOException e) {
                if (e instanceof ConnectTimeoutException) {
                    result = Status.CONNECT_TIME_OUT;
                } else {
                    result = Status.IO_EXCEPTION;
                }
                if (currCount > requestLimit) {
                    break;
                }
            } finally {
            }
        }
        return result;
    }

    public static String getString(HttpGet get, int requestLimit) {

        if (requestLimit < 1) {
            return null;
        }
        HttpResponse response;
        int currCount = 0; // 当前请求次数
        String result = null;

        while (currCount < requestLimit) {

            HttpClient httpClient = getHttpClient();
            currCount++;
            try {
                get.addHeader("charset", HTTP.UTF_8);
                response = httpClient.execute(get);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                } else if (statusCode == 502) {
                    get.abort();
                    result = Status.SERVER_ERROR;
                } else {
                    get.abort();
                    result = Status.RESPONSE_FAIL;
                }
            } catch (ClientProtocolException e) {
                if (currCount > requestLimit) {
                    result = Status.REQUEST_FAIL;
                    break;
                }
            } catch (IOException e) {
                if (e instanceof ConnectTimeoutException) {
                    result = Status.CONNECT_TIME_OUT;
                } else {
                    result = Status.IO_EXCEPTION;
                }
                if (currCount > requestLimit) {
                    break;
                }
            } finally {
            }
        }
        return result;
    }

    /**
     * 用户使用的方法 功能：请求服务器，返回字符串
     *
     * @param uri          字符串形式的请求地址
     * @param requestLimit 最多允许的请求失败次数
     * @return
     */
    public static String getString(String uri, int requestLimit) {

        if (requestLimit < 1) {
            return null;
        }
        HttpResponse response;
        int currCount = 0; // 当前请求次数
        String result = null;
        HttpPost post = getPost(uri, null);
        while (currCount < requestLimit) {

            HttpClient httpClient = getHttpClient();
            currCount++;
            try {
                response = httpClient.execute(post);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity());
                } else if (statusCode == 502) {
                    post.abort();
                    result = "镜子后台正在维护升级中，请您稍后再试吧！";
                } else {
                    post.abort();
                    result = "响应失败,请求终止.";
                }
            } catch (ClientProtocolException e) {
                if (currCount > requestLimit) {
                    result = "请求失败.";
                    break;
                }
            } catch (IOException e) {
                if (e instanceof ConnectTimeoutException) {
                    result = "连接超时.";
                } else {
                    result = "IO异常.";
                }
                if (currCount > requestLimit) {
                    break;
                }
            } finally {

            }
        }
        return result;
    }

    /**
     * 释放建立http请求占用的资源
     */
    public static void shutdown() {
        // 释放建立http请求占用的资源
        httpClient.getConnectionManager().shutdown();
        httpClient = null;
    }

    public static boolean isResponseOk(Context context, String result) {

        if (Status.IO_EXCEPTION.equals(result)) {

            TipUtil.show(context, "IO网络异常！");
            return false;

        } else if (Status.REQUEST_FAIL.equals(result)) {

            TipUtil.show(context, "请求失败！");
            return false;

        } else if (Status.CONNECT_TIME_OUT.equals(result)) {

            TipUtil.show(context, "链接超时！");
            return false;

        } else if (Status.RESPONSE_FAIL.equals(result)) {

            TipUtil.show(context, "响应失败！");
            return false;

        } else if (Status.SERVER_ERROR.equals(result)) {
            TipUtil.show(context, "镜子后台正在维护升级中，请您稍后再试吧！");
            return false;
        }
        return !TextUtils.isEmpty(result);
    }

    /**
     * 同步从服务端获取key对应的value
     *
     * @param key
     * @return 返回值有可能为""
     */
    public static String getValueFromNet(String key) {

        // List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        //
        // params.add(new BasicNameValuePair("key", key));
        //
        // HttpPost post = EnhancedHttpHelper.getPost(Constants.URL.getValueByKey, params);
        //
        // return EnhancedHttpHelper.getString(post, 2);
        return null;
    }
}


/**
 * 示例
 * private Button btn;
 * private EditText toast;
 * private String uri = "http://10.2.105.76:8080/json/TestJsonServlet";
 * private List<BasicNameValuePair> params;
 * private Handler handler;
 * private String showStr = "";
 *
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * setContentView(R.layout.main);
 * <p>
 * params = new ArrayList<BasicNameValuePair>();
 * <p>
 * handler = new Handler() {
 * @Override public void handleMessage(Message msg) {
 * switch (msg.what) {
 * case 0x01:
 * toast.setText(showStr);
 * break;
 * }
 * }
 * };
 * <p>
 * toast = (EditText) findViewById(R.id.editText1);
 * btn = (Button) findViewById(R.id.button1);
 * btn.setOnClickListener(new OnClickListener() {
 * @Override public void onClick(View v) {
 * new Thread(){
 * public void run() {
 * params.add(new BasicNameValuePair("username", "zhelong"));
 * params.add(new BasicNameValuePair("password", "123456"));
 * HttpPost post = HttpUtil.getPost(uri, params);
 * showStr += HttpUtil.getString(post,3);
 * handler.sendEmptyMessage(0x01);
 * };
 * }.start();
 * }
 * });
 * }
 */
