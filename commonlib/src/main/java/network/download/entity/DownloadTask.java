package network.download.entity;

import java.io.Serializable;

/************************************************
 * Function：
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class DownloadTask implements Serializable {

    private String fileUrl;
    private String filePath; // 本地文件路径
    public long startPos; // 读取开始位置
    public long endPos; // 读取结束位置
    public long downloadedSize; // 已下载
    private boolean isFinish; // 下载完成标志

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    @Override
    public String toString() {
        return "DownloadTask{" +
                "fileUrl='" + fileUrl + '\'' +
                ", filePath='" + filePath + '\'' +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                ", downloadedSize=" + downloadedSize +
                ", isFinish=" + isFinish +
                '}';
    }
}
