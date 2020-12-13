package network.download.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import network.download.DownloadUtil;
import network.download.listener.DownloadListener;
import ui.tip.TipUtil;
import util.CacheUtils;
import util.LogUtil;

/**
 * 队列下载service
 */
public class DownloadService extends Service implements DownloadListener {

    private static String TAG = DownloadService.class.getName();

    private List<String> urlList = new ArrayList<>(); // 下载序列
    private boolean isDownloading = false; // 是否正在下载当前文件
    private boolean isLooping = false; // 是否继续顺序执行下载任务，如：网络断开可停止下载任务
    private static final String cacheKey = "downloadUrlList";
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        String imgUrl2 = "https://timgsa.baidu.com/timg?image&quality=100&size=b9999_10000&sec=1607662751843&di=5d18abdbae225963df9b1dcb39cd7583&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1309%2F15%2Fc9%2F25722310_1379252549079.jpg";
        urlList.add(imgUrl2);
        LogUtil.d(TAG, "onCreate");

        // TODO 从数据库初始化任务列表
        List<String> dbList = CacheUtils.getBean(cacheKey, ArrayList.class);
        if (dbList != null && dbList.size() > 0) {
            urlList.addAll(dbList);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "onStartCommand");

        if (intent.getExtras() != null) {
            String fileUrl = intent.getExtras().getString("fileUrl");
            LogUtil.d(TAG, fileUrl);
            urlList.add(fileUrl);
            CacheUtils.setBean(cacheKey, urlList);
        }

        startRun();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
    }

    private void startRun() {
        if (isLooping || urlList.size() == 0) {
            return;
        }
        isLooping = true;
        new Thread() {
            @Override
            public void run() {
                while (isLooping && urlList.size() != 0) {
                    if (!isDownloading) {
                        isDownloading = true;
                        String fileUrl = urlList.get(0);
                        DownloadUtil.download(fileUrl, DownloadService.this);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isLooping = false;
                LogUtil.d(TAG, "service loop stop");
            }
        }.start();
    } // startRun



    @Override
    public void onStart() {
//        DownloadDispatcher.Companion.getInstance().dispatchStart();
        toast("onStart in service");
    }

    @Override
    public void onProgress(String fileUrl, long downloadedSize, long totalSize) {
//        DownloadDispatcher.Companion.getInstance().dispatchProgress(fileUrl, downloadedSize, totalSize);
    }

    @Override
    public void onFinish(String fileUrl, String filePath) {
        urlList.remove(fileUrl);
        isDownloading = false;

        LogUtil.d(TAG, "onFinish");
//        DownloadDispatcher.Companion.getInstance().dispatchFinish(fileUrl, filePath);

    }

    @Override
    public void onStop(String fileUrl, long downloadedSize) {
        isDownloading = false;
//        DownloadDispatcher.Companion.getInstance().dispatchStop(fileUrl, downloadedSize);
        LogUtil.d(TAG, "onStop");
    }

    @Override
    public void onCancel(String fileUrl) {
        urlList.remove(fileUrl);
        isDownloading = false;
//        DownloadDispatcher.Companion.getInstance().dispatchCancel(fileUrl);
        LogUtil.d(TAG, "onCancel");
    }

    @Override
    public void onFail(String fileUrl, String errorMsg) {
        urlList.remove(fileUrl);
        isDownloading = false;
        LogUtil.d(TAG, "onFail");
//        DownloadDispatcher.Companion.getInstance().dispatchFail(fileUrl, errorMsg);
    }

    private void toast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TipUtil.show(DownloadService.this, msg);
            }
        });
    }
}
