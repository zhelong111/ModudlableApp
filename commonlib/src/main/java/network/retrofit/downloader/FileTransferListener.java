package network.retrofit.downloader;

/************************************************
 * Function： 
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public interface FileTransferListener {
    void onStart();
    void onProgress(long progress, long total);
    void onFinish(String filePath);
    void onFail(String reason);
}
