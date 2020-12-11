package network.retrofit.downloader;

import okhttp3.ResponseBody;

/************************************************
 * Function： 
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class RetroDownloadTask {
    private long fileSizeDownloaded = 0;
    private ResponseBody fileBody;

    public long getFileSizeDownloaded() {
        return fileSizeDownloaded;
    }

    public void setFileSizeDownloaded(long fileSizeDownloaded) {
        this.fileSizeDownloaded = fileSizeDownloaded;
    }

    public ResponseBody getFileBody() {
        return fileBody;
    }

    public void setFileBody(ResponseBody fileBody) {
        this.fileBody = fileBody;
    }
}
