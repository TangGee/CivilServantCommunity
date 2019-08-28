package com.mdove.civilservantcommunity.base

import com.mdove.civilservantcommunity.base.okhttp.OkHttpCallFactory
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by MDove on 2019/3/25.
 */
object FrameApiClient {
    private var mHttpService: IHttpInterface

    private val sOkHttpClient = OkHttpClient.Builder()
            .connectTimeout(NetworkConstants.CONNECT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(NetworkConstants.IO_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(NetworkConstants.IO_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .connectionPool(ConnectionPool(15, 5, TimeUnit.MINUTES))
            .build()

    private val sStringRetrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://www.ohonor.xyz/")
            .addConverterFactory(StringConverterFactory())
            .callFactory(OkHttpCallFactory(sOkHttpClient))

    init {
        mHttpService = sStringRetrofitBuilder.build().create(IHttpInterface::class.java)
    }

    @Throws(Exception::class)
    fun executeGet(url: String): String {
        val requestHeaders = HashMap<String, String>()
        val call = mHttpService.normalGet(url, requestHeaders);
        val response = call.execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val json = JSONObject()
            val headers = response.headers()
            if (headers != null) {
                for (name in headers.names()) {
                    json.put(name, headers.get(name))
                }
            }
            throw RuntimeException("response code:$response.code()" + "\n" + call.request().url.toString() + "\n" + response.code() + "\n" + json.toString(2) + "\n" + response.message())
        }
    }

    @Throws(Exception::class)
    fun executePostForm(url: String, formMap: Map<String, String>): String {
        //        Map<String, String> map = appendAndGetCommonParamMap(formMap);
        val call = mHttpService.normalPostForm(url, formMap)
        val response = call.execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            val json = JSONObject()
            val headers = response.headers()
            if (headers != null) {
                for (name in headers.names()) {
                    json.put(name, headers.get(name))
                }
            }
            throw RuntimeException("response code:$response.code()" + "\n" + call.request().url.toString() + "\n" + response.code() + "\n" + json.toString(2) + "\n" + response.message())
        }
    }

    @Throws(Exception::class)
    fun executePost(url: String, body: String, headers: Map<String, String>?): String {
        var headers = headers
        if (headers == null) {
            headers = HashMap()
        }
        val call = mHttpService.normalPost(url, body, headers)
        val response = call.execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            val json = JSONObject()
            val headers = response.headers()
            if (headers != null) {
                for (name in headers.names()) {
                    json.put(name, headers.get(name))
                }
            }
            throw RuntimeException("response code:$response.code()" + "\n" + call.request().url.toString() + "\n" + response.code() + "\n" + json.toString(2) + "\n" + response.message())
        }
    }

}