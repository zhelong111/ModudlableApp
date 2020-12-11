package com.bruce.modulableapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bruce.moduleapp.chat.ChatUtil;

import common.BaseApplication;
import network.retrofit.api.RetroUtil;
import network.retrofit.downloader.FileTransferListener;
import network.retrofit.downloader.RetroDownloadUtil;
import ui.tip.TipUtil;
import util.CacheUtils;
import util.LogUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRetroDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_to_chat).setOnClickListener(this);
        findViewById(R.id.btn_to_home).setOnClickListener(this);
        btnRetroDownload = findViewById(R.id.btn_retro_download);
        btnRetroDownload.setOnClickListener(this);
        // 调用chat模块的功能
        ChatUtil.sendMsg("hallo world");


        CacheUtils.setLong(imgUrl, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_chat:
                ARouter.getInstance().build("/chat/chat").navigation();
                break;
            case R.id.btn_retro_download:
                retroDownload();
                break;
            case R.id.btn_fast_download:

                break;
        }
    }

//    String imgUrl = "https://timgsa.baidu.com/timg?image&quality=100&size=b9999_10000&sec=1607662751843&di=5d18abdbae225963df9b1dcb39cd7583&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1309%2F15%2Fc9%2F25722310_1379252549079.jpg";
    String imgUrl = "http://qd.shouji.qihucdn.com/nqapk/sjzs2_100000003_5f1ab75c42fd778920/201012/d1208503899d47cab3251dfaa06aaf95/appstore-300090091.apk";
    private void retroDownload() {
//        RetroUtil.downloadFile(imgUrl, new FileTransferListener() {
        RetroUtil.downloadFileWithProgress(imgUrl, new FileTransferListener() {
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
}