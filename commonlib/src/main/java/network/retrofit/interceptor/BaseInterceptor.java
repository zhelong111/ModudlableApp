package network.retrofit.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import util.LogUtil;

abstract class BaseInterceptor implements Interceptor {

    private static final String TAG = BaseInterceptor.class.getName();

    private Map<String, String> headers;

    public abstract Map<String, String> getHeaders();

    public BaseInterceptor() {
        this.headers = getHeaders();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }
        LogUtil.d("requestInfo", "url=" + chain.request().url().toString());

//        HttpUrl.Builder builder = request.url()
//                .newBuilder()
//                .addQueryParameter("_a",Base64.encode(appKey.getBytes());//这是在链接后面拼接公共参数
//
//        FormBody.Builder formBody = new FormBody.Builder();
//        formBody.add("_a",Base64.encode(appKey.getBytes());
//        //请求体定制
//        if(request.body() instanceof FormBody){
//            FormBody oidFormBody = (FormBody) request.body();
//            for (int i = 0;i<oidFormBody.size();i++){
//                formBody.add(oidFormBody.name(i),oidFormBody.value(i));
//            }
//        }
        //红色部分是在url中添加统一的参数，蓝色部分是在表单中添加统一参数，一个get一个post


        return chain.proceed(builder.build());
    }
}