package network.retrofit.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.Map;

import common.BaseApplication;
import kotlin.random.URandomKt;
import network.retrofit.RetrofitManager;
import network.retrofit.comm.DownloadResponseBody;
import network.retrofit.downloader.FileTransferListener;
import network.retrofit.downloader.RetroDownloadUtil;
import network.retrofit.util.RetroParamUtil;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import util.CacheUtils;
import util.FileUtil;
import util.LogUtil;

/************************************************
 * Function：Retrofit外调接口
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class RetroUtil {

    private static class Proxy {
        private static BaseApi baseApi = RetrofitManager.getInstance().create(BaseApi.class);
    }

    private static BaseApi getBaseApi() {
        return Proxy.baseApi;
    }

    /**
     * 异步Get请求
     * @param url
     * @param res
     * @param <T>
     */
    public static <T> void getAsync(final String url, final IResponse<T> res) {
        Call<ResponseBody> call = getBaseApi().executeGet(url);
        executeRequest(call, "executeGet", new Class[] {String.class}, res);
    }

    public static <T> void getAsync(final String url, final Map<String, Object> params, final IResponse<T> res) {
        Call<ResponseBody> call = getBaseApi().executeGet(url, params);
        executeRequest(call, "executeGet", new Class[] {String.class, Map.class}, res);
    }

    public static <T> void postAsync(final String url, final IResponse<T> res) {
        Call<ResponseBody> call = getBaseApi().executePost(url);
        executeRequest(call, "executePost", new Class[] {String.class}, res);
    }

    public static <T> void postAsync(final String url, final Map<String, Object> params, final IResponse<T> res) {
        Call<ResponseBody> call = getBaseApi().executePost(url, params);
        executeRequest(call, "executePost", new Class[] {String.class, Map.class}, res);
    }


    private static <T> void executeRequest(Call<ResponseBody> call, String apiMethod, Class[] methodParamTypes, IResponse<T> res) {
        try {
            //异步请求
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (res == null) {
                        return;
                    }
                    try {
                        Class<?> clazz = getBaseApi().getClass();
                        Method method = clazz.getMethod(apiMethod, methodParamTypes);
                        //根据resonse的getDataType来决定返回的类型
                        Converter converter = RetrofitManager.getInstance().useDefault().responseBodyConverter(res.getDataType(), method.getAnnotations());
                        if (converter != null) {
                            T data = (T) converter.convert(response.body());
                            res.success(data);
                        }
                    } catch (Exception e) {
                        res.failure(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (res != null) res.failure(t);
                }
            });

        } catch (Exception e) {
            if (res != null) res.failure(e);
        }
    }

    // ----------------------------------------------- File upload and download -------------------------------------------

    /**
     * 下载文件
     * @param url
     * @param listener
     */
    public static void downloadFile(String url, FileTransferListener listener) {
        Call<ResponseBody> call = getBaseApi().download(url);
        if (listener != null) {
            listener.onStart();
        }
        long startTime = System.currentTimeMillis();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LogUtil.d("cost time", "time=" + (System.currentTimeMillis() - startTime) + "ms");
                if (response.code() == 200) {
                    RetroDownloadUtil.download(url, response.body(), listener);
                } else {
                    if (listener != null) {
                        listener.onFail(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onFail(t.toString());
                }
            }
        });
    }


//    /**
//     * 单文件上传
//     * @param url
//     * @param filePath
//     * @param key
//     * @param listener
//     */
//    public static void upload(String url, String filePath, String key, FileTransferListener listener) {
//        MultipartBody.Part part = RetroParamUtil.createMultiPart(key, new File(filePath));
//        Call<ResponseBody> call = getBaseApi().uploadFile(url, part);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() == 200) {
//                    if (listener != null) {
//                        listener.onFinish(filePath);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                if (listener != null) {
//                    listener.onFail(t.toString());
//                }
//            }
//        });
//    }



}
