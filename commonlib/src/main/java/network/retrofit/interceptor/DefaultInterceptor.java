package network.retrofit.interceptor;

import java.util.HashMap;
import java.util.Map;
import common.BaseApplication;
import util.AppUtil;

/************************************************
 * Function： 
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class DefaultInterceptor extends BaseInterceptor {
    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("mac", AppUtil.getMacAddress());
        params.put("versionCode", AppUtil.getVersionCode(BaseApplication.getContext())+"");
        return params;
    }
}
