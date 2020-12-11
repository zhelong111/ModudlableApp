package network.retrofit.util;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/************************************************
 * Function：
 * Author: Bruce.Zhou
 * Date: 2020/12/10
 * Email: zhoul5@bngrp.com
 *************************************************/
public class RetroParamUtil {
    public static MultipartBody.Part createMultiPart(String key, File file) {
        // 创建 RequestBody，用于封装构建Part
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));

        // MultipartBody.Part 和后端约定好Key
        MultipartBody.Part body = MultipartBody.Part.createFormData(key, file.getName(), requestFile);

        return body;
    }




}
