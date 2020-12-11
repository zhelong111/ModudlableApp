package network.retrofit.api;

import java.util.Map;

import network.retrofit.comm.DownloadResponseBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

interface TransferApi {
    // ----------------------------------------------- File upload and download -------------------------------------------
    @POST()
    @Multipart
    Call<ResponseBody> uploadFile(@Url String url, @Part MultipartBody.Part file);

    @POST()
    Call<ResponseBody> uploadFiles(@Url String url, @Part("filename") String description, @PartMap Map<String, MultipartBody.Part> map);

    /**
     * 指定下载文件范围，用于断点续传
     * @param fileUrl
     * @param range
     * @return
     */
    @GET()
    @Streaming
    Call<DownloadResponseBody> download(@Url String fileUrl, @Header("Range") String range);

    /**
     * 没有指定下载文件范围
     * @param fileUrl
     * @return
     */
    @GET()
    @Streaming
    Call<ResponseBody> download(@Url String fileUrl);
}