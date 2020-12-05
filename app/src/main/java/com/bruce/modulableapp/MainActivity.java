package com.bruce.modulableapp;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bruce.moduleapp.chat.ChatUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_to_chat).setOnClickListener(this);
        findViewById(R.id.btn_to_home).setOnClickListener(this);
        // 调用chat模块的功能
        ChatUtil.sendMsg("hallo world");



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_chat:
                ARouter.getInstance().build("/chat/chat").navigation();
                break;
            case R.id.btn_to_home:

                break;
        }
    }
}