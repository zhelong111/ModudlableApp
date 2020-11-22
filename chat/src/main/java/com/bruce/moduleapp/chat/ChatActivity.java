package com.bruce.moduleapp.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bruce.commonservice.module_service.HomeExportService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bean.EventMsg;


@Route(path = "/chat/chat") // 不同module的group必须不一样
public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @Autowired(name = "/homeService/service")
    public HomeExportService homeService; // 需要是public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ARouter.getInstance().inject(this); // autowired
        EventBus.getDefault().register(this);

        findViewById(R.id.tv_chat).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_chat) {
            ARouter.getInstance().build("/home/home").withString("msg", "Hi, I'm chat!").navigation();
            homeService.sayHi("Hi, This is service interface from Home Module");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMsg msg) {
        Log.d("chat", msg.getValue().toString());

        ((TextView)findViewById(R.id.tv_chat)).setText(msg.getValue().toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}