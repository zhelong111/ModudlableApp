package network.download.listener;

/************************************************
 * Function：下载回调
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public interface DownloadListener {
    void onStart();
    void onProgress(String fileUrl, long downloadedSize, long totalSize);
    void onFinish(String fileUrl, String filePath);
    void onStop(String fileUrl, long downloadedSize);
    void onCancel(String fileUrl);
    void onFail(String fileUrl, String errorMsg);
}
