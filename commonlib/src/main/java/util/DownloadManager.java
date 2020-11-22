package util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import network.httpclient.ThreadPoolManager;

/**
 * Created by Administrator on 2018/4/23.
 */

public class DownloadManager {
    private static DownloadManager instance;
    private static final int MAX_THREAD_NUM = 5;
    private int threadNum = 0;
    private static final int ERROR_INVALID_URL = 0x301;


    protected DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    public interface DownloadCallback {
        void onStart(String fileUrl);
        void onProgress(String fileUrl, float progress);
        void onSuccess(String fileUrl, String localPath);
        void onFail(String fileUrl, int errorCode, String errorMsg);
    }

    @SuppressLint("StaticFieldLeak")
    public void download(final String fileUrl, final String saveFileName, final DownloadCallback callback) {
        callback.onStart(fileUrl);
        if (TextUtils.isEmpty(fileUrl) || !fileUrl.startsWith("http")) {
            callback.onFail(fileUrl, ERROR_INVALID_URL, "下载连接不正确");
            return;
        }
        try {
            new AsyncTask<String, Float, String>() {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected String doInBackground(String... strings) {
                    String localPath = getLocalPath(saveFileName);
                    try {
                        URL url = new URL(fileUrl);
                        URLConnection connection = url.openConnection();
                        connection.setConnectTimeout(1000 * 10);
                        connection.setReadTimeout(1000 * 30);
                        InputStream is = connection.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(is);
                        int len = 0;
                        int currLen = 0;
                        int totalLen = connection.getContentLength();
                        byte[] buffer = new byte[2048];
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localPath));
                        while ((len = bis.read(buffer)) != -1) {
                            currLen += len;
                            publishProgress(currLen * 1.0f/totalLen);
                            bos.write(buffer, 0, len);
                        }
                        bos.flush();
                        bos.close();
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return localPath;
                }

                @Override
                protected void onProgressUpdate(Float... values) {
                    LogUtil.d("download", values[0] + "");
                    callback.onProgress(fileUrl, values[0]);
                }

                @Override
                protected void onPostExecute(String localPath) {
                    callback.onSuccess(fileUrl, localPath);
                }
            }.executeOnExecutor(ThreadPoolManager.getInstance().getThreadPool());
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(fileUrl, 333, e.getMessage());
        }
    }

    public String getLocalPath(String fileName) {
        String localDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = new File(localDirPath, "imgs" + "/" + fileName);
        file.getParentFile().mkdirs();
        return file.getAbsolutePath();
    }

    // ---------------------------------------- Fast download module ----------------------------------------

//    class CutFileElement {
//        int startPos;
//        int len;
//        int currPos;
//
//        public CutFileElement(int startPos, int len) {
//            this.startPos = startPos;
//            this.currPos = startPos;
//            this.len = len;
//        }
//    }
//
//    /**
//     * 切割片段
//     * @param contentLength
//     * @return
//     */
//    public List<CutFileElement> getCuts(int contentLength) {
//        int cutLen = contentLength / MAX_THREAD_NUM;
//        List<CutFileElement> cutList = new ArrayList<>();
//        for (int i = 0; i < MAX_THREAD_NUM; i++) {
//            CutFileElement cut;
//            if (i < MAX_THREAD_NUM - 1) {
//                cut = new CutFileElement(i * cutLen, cutLen);
//            } else {
//                cut = new CutFileElement(i * cutLen, cutLen + contentLength % cutLen);
//            }
//            cutList.add(cut);
//        }
//        return cutList;
//    }
//
//    class DownloadTask {
//        float progress;
//        List<CutFileElement> cutList;
//        String url;
//        String localPath;
//
//        public DownloadTask(String url) {
//            this.url = url;
//            this.localPath = getLocalPath(url);
//        }
//    }
//
//    public interface DownloadTaskCallback {
//        void onTaskStart(String fileUrl);
//        void onTaskProgress(String fileUrl, float progress);
//        void onTaskSuccess(String fileUrl, String localPath);
//        void onTaskFail(String fileUrl, int errorCode, String errorMsg);
//    }
//
//    public void download(String fileUrl, DownloadTaskCallback taskCallback) {
//        try {
//            URL url = new URL(fileUrl);
//            URLConnection connection = url.openConnection();
//            connection.setConnectTimeout(1000 * 10);
//            connection.setReadTimeout(1000 * 30);
//            InputStream is = connection.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//            int contentLength = connection.getContentLength();
//            List<CutFileElement> cutList = getCuts(contentLength);
//            DownloadTask task = new DownloadTask(fileUrl);
//            task.cutList = cutList;
//            for (int i = 0; i < cutList.size(); i++) {
//                CutFileElement cut = cutList.get(i);
//
//            }
//        } catch (IOException e) {
//
//        }
//    }
//
//    private void downloadCut(String url, CutFileElement cut, BufferedInputStream bis) {
//        try {
//            bis.skip(cut.startPos);
//            int len = 0;
////            bis.read
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
