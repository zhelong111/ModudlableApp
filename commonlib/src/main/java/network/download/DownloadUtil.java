package network.download;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.BaseApplication;
import network.comm.util.NetworkUtils;
import network.download.entity.DownloadEntity;
import network.download.entity.DownloadStatus;
import network.download.entity.DownloadTask;
import network.download.listener.DownloadListener;
import network.retrofit.downloader.RetroDownloadUtil;
import ui.tip.TipUtil;
import util.CacheUtils;
import util.LogUtil;

/************************************************
 * Function： 多任务下载，多线程下载、断点续传
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class DownloadUtil {

    private static final String TAG = DownloadUtil.class.getName();
    private static final int TIME_OUT = 5000;
    public static int threadNum = 6;

    public static Map<String, Downloader> downloaderMap = new ConcurrentHashMap<>(3);

    public static void download(String fileUrl, DownloadListener listener) {
        listener.onStart();
        if (!NetworkUtils.isNetWorkAvailable(BaseApplication.getContext())) {
            listener.onFail(fileUrl, "网络未连接");
            return;
        }
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        String filePath = RetroDownloadUtil.getDownloadDir(BaseApplication.getContext()) + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(fileUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Charset", "UTF-8");
                        conn.setConnectTimeout(TIME_OUT);
                        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                        conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                        conn.connect();
                        int fileLength = conn.getContentLength();
                        if (fileLength < 0) {  //网络被劫持时会出现这个问题
                            listener.onFail(fileUrl, "网络劫持");
                            return;
                        }
                        int code = conn.getResponseCode();
                        if (code == 200) {
//                            // 建一个文件
//                            File newfile = new File(filePath);
//                            newfile.createNewFile();
                            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rwd");
                            //设置文件长度
                            randomAccessFile.setLength(fileLength);
                            DownloadEntity entity = new DownloadEntity();
                            entity.setFileUrl(fileUrl);
                            entity.setFilePath(filePath);
                            entity.setDownloadedSize(0);
                            entity.setFileLength(fileLength);
                            entity.setStatus(DownloadStatus.INIT);
                            List<DownloadTask> taskList = new ArrayList<>();
                            int blockSize = fileLength / threadNum;
                            for (int i = 0; i < threadNum; i++) {
                                DownloadTask task = new DownloadTask();
                                task.setFileUrl(fileUrl);
                                task.setFilePath(filePath);
                                task.setStartPos(i * blockSize);
                                if (i != threadNum - 1) {
                                    task.setEndPos((i + 1) * blockSize - 1);
                                } else {
                                    task.setEndPos(fileLength);
                                }
                                taskList.add(task);
                            }
                            entity.setTaskList(taskList);
                            Downloader downloader = new Downloader(entity, listener);
                            downloaderMap.remove(fileUrl);
                            downloaderMap.put(fileUrl, downloader);
                            downloader.start();
                        }

                    } catch (IOException e) {
                        listener.onFail(fileUrl, e.toString());
                        e.printStackTrace();
                    }

                }
            }.start();

        } else { // 文件存在
            DownloadEntity entity = CacheUtils.getBean(fileUrl, DownloadEntity.class);
            if(entity == null || entity.getStatus() == DownloadStatus.FINISHED || entity.isDownloadFinish()) { // 说明下载完成，任务已删除
                listener.onFinish(fileUrl, filePath);
            } else { // 未下载完成
                LogUtil.d(TAG, entity.toString());
                if (entity.getStatus() != DownloadStatus.DOWNLOADING) {
                    TipUtil.show(BaseApplication.getContext(), "继续下载1");
                    Downloader downloader = new Downloader(entity, listener);
                    downloaderMap.remove(fileUrl);
                    downloaderMap.put(fileUrl, downloader);
                    downloader.start();
                } else {
                    if (downloaderMap.get(fileUrl) != null) { // 内存中有下载任务
                        TipUtil.show(BaseApplication.getContext(), "正在下载..");
                    } else { // 进程可能被杀死后，继续开始下载
                        entity.setDownloadedSize(entity.getTaskDownloadLen());
                        TipUtil.show(BaseApplication.getContext(), "继续下载2");
                        Downloader downloader = new Downloader(entity, listener);
                        downloaderMap.put(fileUrl, downloader);
                        downloader.start();
                    }
                }
            }
        }
    }

    public static void stop(String fileUrl) {
        Downloader downloader = downloaderMap.get(fileUrl);
        if (downloader != null) {
            downloader.stop();
        }
    }


    public static void stopAll() {
        Set<String> set = downloaderMap.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            String url = it.next();
            stop(url);
        }
    }

    public static void cancel(String url) {
        Downloader downloader = downloaderMap.get(url);
        if (downloader != null) {
            downloader.cancel();
        }
    }

    /**
     * 取消下载所有任务
     */
    public static void cancelAll() {
        Set<String> set = downloaderMap.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            String url = it.next();
            cancel(url);
        }
    }

    public static void resume(String fileUrl) {
        Downloader downloader = downloaderMap.get(fileUrl);
        if (downloader != null) {
            downloader.start();
        }
    }

    /**
     * 是否正在下载此文件
     * @param fileUrl
     * @return
     */
    public static boolean isDownloading(String fileUrl) {
        if (downloaderMap.containsKey(fileUrl)) {
            return downloaderMap.get(fileUrl).isDownloading();
        }
        return false;
    }


}
