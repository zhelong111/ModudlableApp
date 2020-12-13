package network.download.entity;

/************************************************
 * Function：
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public interface DownloadStatus {
    int INIT = 0;
    int DOWNLOADING = 1;
    int STOPED = 2;
    int FINISHED = 3;
}
