package com.mdove.civilservantcommunity.base

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface IHttpInterface {
    @POST
    fun postAppLog(@Url url: String, @Body body: String, @QueryMap options: Map<String, String>): Call<String>

    @POST
    fun normalPost(@Url url: String, @Body body: String, @HeaderMap headerMap: Map<String, String>): Call<String>

    @POST
    fun normalPost(@Url url: String, @Body body: RequestBody, @HeaderMap headerMap: Map<String, String>): Call<String>

    @GET
    fun normalGet(@Url url: String, @HeaderMap headerMap: Map<String, String>): Call<String>

    @GET
    fun normalGetByte(@Url url: String, @HeaderMap headerMap: Map<String, String>): Call<ByteArray>


    @FormUrlEncoded
    @POST
    fun normalPostForm(@Url url: String, @FieldMap map: Map<String, String>): Call<String>

    /**
     * The Headers is used to avoid gzip
     *
     * @param url
     * @param multipartBody
     * @return
     */
    @Headers("Content-Encoding: none")
    @POST
    fun normalPostFile(@Url url: String, @Body multipartBody: MultipartBody): Call<String>

    @GET
    fun executeRequestLoadByteArray(@Url url: String, @HeaderMap requestHeaders: Map<String, String>): Call<ResponseBody>

    @POST
    fun executeRequestPB(@Url url: String, @Body body: RequestBody, @HeaderMap requestHeaders: Map<String, String>): Call<ResponseBody>

}
