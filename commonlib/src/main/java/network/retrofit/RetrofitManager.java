package network.retrofit;

import android.app.Application;
import android.transition.Transition;

import com.bruce.modulableapp.commonlib.BuildConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import network.constants.NetConstants;
import network.retrofit.downloader.FileTransferListener;
import network.retrofit.interceptor.DownloadInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static Map<String, Retrofit> retrofitMap = new ConcurrentHashMap<>();

    private static String defaultUrl;

    public static void init(Application context) {
        // 默认初始化一个default retrofit
        defaultUrl = NetConstants.getBaseUrl();
        Retrofit rt = new Retrofit.Builder()
                .baseUrl(defaultUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpProxy.getClient(context))
                .build();
        retrofitMap.put(defaultUrl, rt);
    }

    private static class Proxy {
        private static RetrofitManager instance = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return Proxy.instance;
    }

    /**
     * 项目主要的baseUrl
     * @return
     */
    public Retrofit useDefault() {
        return retrofitMap.get(defaultUrl);
    }

    /**
     * 删除不再使用的retrofit实例
     * @param baseUrl
     */
    public void remove(String baseUrl) {
        retrofitMap.remove(baseUrl);
    }

    /**
     * 使用默认baseUrl创建api接口服务
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return useDefault().create(service);
    }

    /**
     * 文件传输api
     * @param service
     * @param <T>
     * @return
     */
    public <T> T createTransferApi(Class<T> service, FileTransferListener listener) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(new DownloadInterceptor(listener));
        Retrofit rt = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(defaultUrl)
                .build();
        return rt.create(service);
    }


}
