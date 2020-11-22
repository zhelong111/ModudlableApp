package com.bruce.commonservice.module_service;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Home模块对其他没有依赖关系模块提供的接口服务
 */
public interface HomeExportService extends IProvider {
    String sayHi(String msg);
}
