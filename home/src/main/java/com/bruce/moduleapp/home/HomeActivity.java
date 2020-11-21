package com.bruce.moduleapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.greenrobot.eventbus.EventBus;

import bean.EventMsg;

@Route(path = "/home/home")
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Autowired
    String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ARouter.getInstance().inject(this); // inject后才能自动装配field，否则为空

        Log.d("home", msg);

        TextView tvHome = findViewById(R.id.tv_home);
        tvHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new EventMsg(11, "Msg from home"));
    }
}