package com.bruce.moduleapp.home.service;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bruce.commonservice.module_service.HomeExportService;

@Route(path = "/homeService/service") // service的group和activity的group不能相同，否则可能找不到组件
public class HomeService implements HomeExportService {
    @Override
    public String sayHi(String msg) {
        Log.d("homeService", msg);
        return msg;
    }

    @Override
    public void init(Context context) {

    }
}
