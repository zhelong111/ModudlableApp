package com.bruce.modulableapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bruce.moduleapp.chat.ChatUtil;

import java.io.File;

import common.BaseApplication;
import network.download.DownloadUtil;
import network.download.listener.DownloadListener;
import network.download.service.DownloadService;
import network.retrofit.api.RetroUtil;
import network.retrofit.downloader.FileTransferListener;
import ui.tip.TipUtil;
import util.FileUtil;
import util.LogUtil;
import util.SystemUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRetroDownload;
    private Button btnFastDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_to_chat).setOnClickListener(this);
        findViewById(R.id.btn_to_home).setOnClickListener(this);
        btnRetroDownload = findViewById(R.id.btn_retro_download);
        btnFastDownload = findViewById(R.id.btn_fast_download);
        btnRetroDownload.setOnClickListener(this);
        btnFastDownload.setOnClickListener(this);
        // 调用chat模块的功能
        ChatUtil.sendMsg("hallo world");



//        CacheUtils.setLong(fileUrl3, 0);


    }

    private void startService() {
        Intent i = new Intent();
        i.setClass(this, DownloadService.class);
        startService(i);
    }

    private void bindService() {
        Intent i = new Intent();
        i.setClass(this, DownloadService.class);
        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.d("bindservice", "onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.d("bindservice", "onServiceDisconnected " + name);
            }
        };
        bindService(i, conn, BIND_AUTO_CREATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_chat:
//                ARouter.getInstance().build("/chat/chat").navigation();
                startService();
//                bindService();
                break;
            case R.id.btn_retro_download:
                fastDownload(fileUrl3);
                break;
            case R.id.btn_fast_download:
                if (DownloadUtil.isDownloading(fileUrl3)) {
                    DownloadUtil.stop(fileUrl3);
                } else {
                    DownloadUtil.resume(fileUrl3);
                }
                break;
        }
    }

    String fileUrl = "http://wap.apk.anzhi.com/data5/apk/201912/30/a143440fffcb748ac0e05033604dce94_76222300.apk";
    String fileUrl2 = "http://wap.apk.anzhi.com/data5/apk/202012/10/e45a741e2cb7d09a8c73ea37105e1fe9_26628800.apk";
    String imgUrl2 = "https://timgsa.baidu.com/timg?image&quality=100&size=b9999_10000&sec=1607662751843&di=5d18abdbae225963df9b1dcb39cd7583&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1309%2F15%2Fc9%2F25722310_1379252549079.jpg";
    String fileUrl3 = "http://qd.shouji.qihucdn.com/nqapk/sjzs2_100000003_5f1ab75c42fd778920/201012/d1208503899d47cab3251dfaa06aaf95/appstore-300090091.apk";
    private void retroDownload() {
        RetroUtil.downloadFile(fileUrl3, new FileTransferListener() {
            @Override
            public void onStart() {
                TipUtil.show(BaseApplication.getContext(), "start download..");
            }

            @Override
            public void onProgress(long progress, long total) {
//                TipUtil.show(MainActivity.this, progress + "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnRetroDownload.setText(progress + " bytes");
                    }
                });

            }

            @Override
            public void onFinish(String filePath) {
                LogUtil.d("filepath", filePath);
                TipUtil.show(BaseApplication.getContext(), filePath);
                btnRetroDownload.setText("开始下载");
            }

            @Override
            public void onFail(String reason) {
                TipUtil.show(BaseApplication.getContext(), reason);
                btnRetroDownload.setText("下载失败");
            }
        });
    }

    private long startTime;

    private void fastDownload(String fileUrl) {
//        DownloadUtil.download(imgUrl2, new DownloadListenerAdapter());
        DownloadUtil.download(fileUrl, new DownloadListener() {
            @Override
            public void onStart() {
                startTime = System.currentTimeMillis();
                TipUtil.show(MainActivity.this, "onstart");
            }

            @Override
            public void onProgress(String fileUrl, long downloadedSize, long totalSize) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnFastDownload.setText("下载:" + downloadedSize + "/" + totalSize);
                    }
                });
            }

            @Override
            public void onFinish(String fileUrl, String filePath) {
                String time = ((System.currentTimeMillis() - startTime)/1000f) + "s";
                TipUtil.show(MainActivity.this, "下载完成:" + time);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnFastDownload.setText(btnFastDownload.getText().toString() + " 使用时间" + time);
                    }
                });
//                FileUtil.deleteFile(filePath);
                SystemUtil.installApp(MainActivity.this, filePath);
            }

            @Override
            public void onStop(String fileUrl, long downloadedSize) {
                TipUtil.show(MainActivity.this, "onStop:" + downloadedSize);
            }

            @Override
            public void onCancel(String fileUrl) {
                TipUtil.show(MainActivity.this, "onCancel");
            }

            @Override
            public void onFail(String fileUrl, String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnFastDownload.setText("下载失败" + errorMsg);
                    }
                });
            }
        });

    }
}