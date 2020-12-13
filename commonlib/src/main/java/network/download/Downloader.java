package network.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.download.entity.DownloadEntity;
import network.download.entity.DownloadStatus;
import network.download.entity.DownloadTask;
import network.download.listener.DownloadListener;
import util.CacheUtils;
import util.FileUtil;
import util.LogUtil;

/************************************************
 * Function：任务下载器
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class Downloader {

    private static final String TAG = Downloader.class.getName();
    private DownloadEntity entity;
    private int status = 0;
    private DownloadListener listener;
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    // 事件标志位
    private volatile int cancelNum = 0;
    private volatile int stopNum = 0;
    private volatile int completeNum = 0;
    private volatile int failNum = 0; // 异常导致失败的线程数
    private volatile boolean isDownloading; // 是否正在下载
    private volatile boolean isCancel; // 是否取消下载
    private volatile boolean isStop; // 是否暂停
    private volatile boolean isFail; // 是否失败
    private int unFinishNum = DownloadUtil.threadNum;

    public Downloader(DownloadEntity entity, DownloadListener listener) {
        this.entity = entity;
        this.listener = listener;
    }

    private class DownloadThread implements Runnable {

        private DownloadTask task;

        public DownloadThread(DownloadTask task) {
            this.task = task;
        }

        @Override
        public void run() {
            LogUtil.d(TAG, Thread.currentThread() + " is started");
            RandomAccessFile file = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                URL netUrl = new URL(task.getFileUrl());
                conn = (HttpURLConnection)netUrl.openConnection();
                //在头里面请求下载开始位置和结束位置
                conn.setRequestProperty("Range", "bytes=" + (task.getStartPos() + task.getDownloadedSize()) + "-" + task.getEndPos());
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setConnectTimeout(60 * 1000);
                conn.setReadTimeout(15 * 1000);  //设置读取流的等待时间,必须设置该参数
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                //创建可设置位置的文件
                file = new RandomAccessFile(task.getFilePath(), "rwd");
                is = conn.getInputStream();
                //设置每条线程写入文件的位置
                file.seek(task.getStartPos() + task.getDownloadedSize());
                byte[] buffer = new byte[2048];
                int len;
                while ((len = is.read(buffer)) != -1 && !isFail) {
                    if (task.getDownloadedSize() == (task.getEndPos() - task.getStartPos() + 1)) { // 当前任务完成
                        break;
                    }
                    if (isCancel) {
                        LogUtil.d(TAG, Thread.currentThread() + " is canceled");
                        break;
                    }
                    if (isStop) {
                        LogUtil.d(TAG, Thread.currentThread() + " is stopped");
                        break;
                    }

                    file.write(buffer, 0, len);
                    task.setDownloadedSize(task.getDownloadedSize() + len);
                    synchronized (entity) {
                        entity.setDownloadedSize(entity.getDownloadedSize() + len);
                        entity.syncToDb();
                        listener.onProgress(entity.getFileUrl(), entity.getDownloadedSize(), entity.getFileLength());
                    }
//                    LogUtil.d(TAG, "isDownloading -->" + isDownloading + " -> " + entity.getDownloadedSize());
                } // while
                file.close();
                is.close();

                if (!isFail && !isStop & !isCancel) {
                    // 当前任务完成
                    task.setFinish(true);
                    entity.syncToDb();
                    LogUtil.d(TAG, "当前任务完成 -->" + isDownloading + " -> " + task.getDownloadedSize());

                    // 检查下载完成
                    synchronized (entity) {
                        completeNum++;
                        if (completeNum == unFinishNum) {
                            LogUtil.d(TAG, "下载完成" + entity.getDownloadedSize() + "/"
                                    + entity.getFileLength() + "-->task total len=" + entity.getTaskDownloadLen());
                            reset();
                            DownloadUtil.downloaderMap.remove(entity.getFileUrl());
                            entity.deleteFromDb();
                            listener.onFinish(entity.getFileUrl(), entity.getFilePath());
                        }
                    }
                } else {
                    LogUtil.d(TAG, "isFail=" + isFail + " isStop=" + isStop + " isCancel=" + isCancel);
                }

                // 处理总体的取消和暂停事件
                if (isCancel) {
                    synchronized (entity) {
                        cancelNum++;
                        if (cancelNum == unFinishNum) {
                            reset();
                            DownloadUtil.downloaderMap.remove(entity.getFileUrl());
                            entity.deleteFromDb();
                            FileUtil.deleteFile(entity.getFilePath());
                            listener.onCancel(entity.getFileUrl());
                        }
                    }
                }
                if (isStop) {
                    synchronized (entity) {
                        stopNum++;
                        LogUtil.d(TAG, task.getDownloadedSize() + "bytes --> stop task " + stopNum);
                        if (stopNum == unFinishNum) {
                            LogUtil.d(TAG, " all stoped");
                            reset();
                            entity.setStatus(DownloadStatus.STOPED);
                            entity.syncToDb();
                            listener.onStop(entity.getFileUrl(), entity.getDownloadedSize());
                        }
                    }
                }

            } catch (IOException e) {
                isDownloading = false;
                isFail = true;
                DownloadUtil.downloaderMap.remove(entity.getFileUrl());
                entity.setStatus(DownloadStatus.STOPED);
                entity.syncToDb();
                listener.onFail(entity.getFileUrl(), entity.getDownloadedSize() + "/" + entity.getFileLength());
                synchronized (entity) {
                    failNum++;
                    LogUtil.e("Downloader", Thread.currentThread().getName() + " IOException" + failNum);
                }
                if (entity.isDownloadFinish()) {
                    onFinish();
                }
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } // run
    }

    public void start() {
        isDownloading = true;
        entity.setStatus(DownloadStatus.DOWNLOADING);
        unFinishNum = entity.getUnFinishTask();
        entity.printTaskInfo();
        for (DownloadTask task : entity.getTaskList()) {
            if (!task.isFinish()) {
                threadPool.execute(new DownloadThread(task));
            }
        }
    }


    /**
     * 取消下载删除已下载文件
     */
    public void cancel() {
        isCancel = true;
        isDownloading = false;
    }

    public void stop() {
        isStop = true;
        isDownloading = false;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    private void reset() {
        isDownloading = false;
        isStop = false;
        isCancel = false;
        stopNum = 0;
        cancelNum = 0;
        failNum = 0;
    }

    private void onFinish() {
        reset();
        DownloadUtil.downloaderMap.remove(entity.getFileUrl());
        entity.deleteFromDb();
        listener.onFinish(entity.getFileUrl(), entity.getFilePath());
    }
}
