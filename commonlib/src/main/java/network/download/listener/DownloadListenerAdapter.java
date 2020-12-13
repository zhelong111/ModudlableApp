package network.download.listener;

/************************************************
 * Function：下载回调适配器
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class DownloadListenerAdapter implements DownloadListener {

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(String fileUrl, long downloadedSize, long totalSize) {

    }

    @Override
    public void onFinish(String fileUrl, String filePath) {

    }

    @Override
    public void onStop(String fileUrl, long downloadedSize) {

    }

    @Override
    public void onCancel(String fileUrl) {

    }

    @Override
    public void onFail(String fileUrl, String errorMsg) {

    }
}
