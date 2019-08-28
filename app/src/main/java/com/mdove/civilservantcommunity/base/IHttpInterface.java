package com.mdove.civilservantcommunity.base;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface IHttpInterface {
    @POST()
    Call<String> postAppLog(@Url String url, @Body String body, @QueryMap Map<String, String> options);

    @POST()
    Call<String> normalPost(@Url String url, @Body String body, @HeaderMap Map<String, String> headerMap);

    @POST()
    Call<String> normalPost(@Url String url, @Body RequestBody body, @HeaderMap Map<String, String> headerMap);

    @GET()
    Call<String> normalGet(@Url String url, @HeaderMap Map<String, String> headerMap);

    @GET()
    Call<byte[]> normalGetByte(@Url String url, @HeaderMap Map<String, String> headerMap);


    @FormUrlEncoded
    @POST()
    Call<String> normalPostForm(@Url String url, @FieldMap Map<String, String> map);

    /**
     * The Headers is used to avoid gzip
     *
     * @param url
     * @param multipartBody
     * @return
     */
    @Headers({"Content-Encoding: none"})
    @POST()
    Call<String> normalPostFile(@Url String url, @Body MultipartBody multipartBody);

    @GET()
    Call<ResponseBody> executeRequestLoadByteArray(@Url String url, @HeaderMap Map<String, String> requestHeaders);

    @POST
    Call<ResponseBody> executeRequestPB(@Url String url, @Body RequestBody body, @HeaderMap Map<String, String> requestHeaders);

}
