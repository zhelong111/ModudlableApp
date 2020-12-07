package com.bruce.commonservice.net_api;

import com.bruce.commonservice.bean.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("")
    Call<User> getUser();
}
