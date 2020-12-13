package network.retrofit.downloader;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import common.BaseApplication;
import okhttp3.ResponseBody;
import util.CacheUtils;
import util.LogUtil;

/**
 * 应用在Retrofit中的下载工具
 */
public class RetroDownloadUtil {

    private static final String TAG = RetroDownloadUtil.class.getName();

    public static boolean download(String url, ResponseBody body, FileTransferListener listener) {

        Log.d(TAG, "contentType:-->" + body.contentType().toString());

//        String type = body.contentType().toString();
//        String fileSuffix = ContentType.getSuffix(type);
        // 其他类型同上 自己判断加入.....
//        String path = getDownloadDir(context) + File.separator + System.currentTimeMillis() + fileSuffix;
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        String path = getDownloadDir(BaseApplication.getContext()) + File.separator + fileName;

        Log.d(TAG, "path:-->" + path);

        try {
            File file = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileBuffer = new byte[4096];
                long fileSize = body.contentLength();
                long downloadedSize = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileBuffer);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileBuffer, 0, read);
                    downloadedSize += read;
                    CacheUtils.setLong(url, downloadedSize);
                    LogUtil.d(TAG, "file download: " + downloadedSize + " of " + fileSize);
                    if (listener != null) listener.onProgress(downloadedSize, fileSize);
                }
                outputStream.flush();
                if (listener != null) listener.onFinish(path);
                return true;
            } catch (IOException e) {
                if (listener != null) listener.onFail(e.toString());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            if (listener != null) listener.onFail(e.toString());
            return false;
        }
    }

    public static void downloadWithRange(String url, long range, File file, ResponseBody responseBody, FileTransferListener listener) {
        InputStream inputStream;
        RandomAccessFile randomAccessFile = null;
        long downloadedSize = range;
        long savedLength;
        byte[] buffer = new byte[4096];
        int len;
        savedLength = responseBody.contentLength();
        inputStream = responseBody.byteStream();
        try {
            randomAccessFile = new RandomAccessFile(file, "rwd");
            if (range == 0) {
                randomAccessFile.setLength(savedLength);
            }
            randomAccessFile.seek(range);
            while ((len = inputStream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                downloadedSize += len;
                CacheUtils.setLong(url, downloadedSize);
                if (listener != null) {
                    listener.onProgress(downloadedSize, randomAccessFile.length());
                }
            }
            if (listener != null) {
                listener.onFinish(file.getAbsolutePath());
            }
        } catch (IOException e) {
            if (listener != null) listener.onFail(e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 应用卸载也会删掉该目录
     * @param context
     * @return
     */
    public static String getDownloadDir(Context context) {
        String dir = context.getExternalFilesDir(null).getAbsolutePath();
//        String dir = context.getFilesDir().getAbsolutePath();
        LogUtil.d(TAG, dir);
        return dir;
    }
}