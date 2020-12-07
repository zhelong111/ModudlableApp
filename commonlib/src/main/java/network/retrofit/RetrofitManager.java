package network.retrofit;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static Retrofit instance;

    public static void init(Application context, String baseUrl) {
        instance = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpProxy.getClient(context))
                .build();
    }

    public static Retrofit getInstance() {
        return instance;
    }

//    public void useUserService() {
//        UserService userService = getInstance().create(UserService.class);
//        userService.getUser().enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                response.body().
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
//    }
}
