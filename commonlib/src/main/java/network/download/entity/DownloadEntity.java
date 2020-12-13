package network.download.entity;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.List;
import util.CacheUtils;
import util.LogUtil;

/************************************************
 * Function： 下载实体
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
@Entity
public class DownloadEntity   {
    @Id(autoincrement = true)
    private Long id;
    private String fileUrl;
    private String filePath; // 文件本地存储路径
    private long fileLength;
    private long downloadedSize;
    @Convert(converter = Converter.class, columnType = String.class)
    private List<DownloadTask> taskList; // 子下载任务
    private int status; // 0、初始状态  1、正在下载 2、下载完成

    @Generated(hash = 1448436215)
    public DownloadEntity(Long id, String fileUrl, String filePath, long fileLength,
            long downloadedSize, List<DownloadTask> taskList, int status) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.filePath = filePath;
        this.fileLength = fileLength;
        this.downloadedSize = downloadedSize;
        this.taskList = taskList;
        this.status = status;
    }

    @Generated(hash = 1671715506)
    public DownloadEntity() {
    }


    public static class Converter implements PropertyConverter<List<DownloadTask>, String> {

        @Override
        public List<DownloadTask> convertToEntityProperty(String databaseValue) {
            if (TextUtils.isEmpty(databaseValue)) {
                return null;
            }
            return JSON.parseObject(databaseValue, List.class);
        }

        @Override
        public String convertToDatabaseValue(List<DownloadTask> entityProperty) {
            if(entityProperty == null){
                return null;
            }
            return JSON.toJSONString(entityProperty);
        }
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }


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

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public List<DownloadTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<DownloadTask> taskList) {
        this.taskList = taskList;
    }

    public void syncToDb() {
        CacheUtils.setBean(fileUrl, this);
    }

    public void deleteFromDb() {
        CacheUtils.remove(fileUrl);
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "fileUrl='" + fileUrl + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileLength=" + fileLength +
                ", downloadedSize=" + downloadedSize +
                ", taskList=" + taskList +
                ", status=" + status +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDownloadFinish() {
        long taskDownloadedSize = 0;
        for (DownloadTask task : taskList) {
            taskDownloadedSize += task.getDownloadedSize();
        }
        if (taskDownloadedSize >= fileLength) {
            return true;
        }
        return false;
    }

    public long getTaskDownloadLen() {
        long taskDownloadedSize = 0;
        for (DownloadTask task : taskList) {
            taskDownloadedSize += task.getDownloadedSize();
        }
        return taskDownloadedSize;
    }

    public void printTaskInfo() {
        for (DownloadTask task : taskList) {
            LogUtil.d("Downloader", "isFinish=" + task.isFinish() + " downloadSize=" + task.getDownloadedSize()
                    + " startPos=" + task.getStartPos() + " endPos=" + task.getEndPos());
        }
    }

    public int getUnFinishTask() {
        int num = 0;
        for (DownloadTask task : taskList) {
            if (!task.isFinish()) {
                num++;
            }
        }
        return num;
    }
}
