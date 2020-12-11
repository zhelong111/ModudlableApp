package network.retrofit.interceptor;

import java.io.IOException;

import network.retrofit.comm.DownloadResponseBody;
import network.retrofit.downloader.FileTransferListener;
import okhttp3.Interceptor;
import okhttp3.Response;

/************************************************
 * Function： 
 * Author: Bruce.Zhou
 * Date: 2020/12/11
 * Email: zhoul5@bngrp.com
 *************************************************/
public class DownloadInterceptor implements Interceptor {

    private FileTransferListener listener;

    public DownloadInterceptor(FileTransferListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}