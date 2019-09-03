package com.mdove.dependent.networkprovider.base

import com.mdove.dependent.networkprovider.okhttp.OkHttpCallFactory
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by MDove on 2019/3/25.
 */
object FrameApiClient :BaseApiClient() {
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
        mHttpService = sStringRetrofitBuilder.build().create(
            IHttpInterface::class.java)
    }

    @Throws(Exception::class)
    fun executeGet(url: String): String {
        return executeGet(url,null)
    }

    @Throws(Exception::class)
    private fun executeGet(
        url: String,
        requestHeaders: Map<String, String>?
    ): String {
        val requestHeadersTemp = requestHeaders ?: HashMap()
        val call = mHttpService.normalGet(url, requestHeadersTemp)

        return execute(call, null)
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
    fun executePost(url: String, body: String): String {
        return executePost(url, body, null, null)
    }

    @Throws(Exception::class)
    fun executePost(url: String, body: String, headers: Map<String, String>): String {
        return executePost(url, body, headers, null)
    }

    @Throws(Exception::class)
    fun executePost(
        url: String,
        body: String,
        headers: Map<String, String>?,
        respHeaders: MutableMap<String, String>?
    ): String {
        val call = mHttpService.normalPost(url, body, headers ?: hashMapOf())
        return execute(call, null,respHeaders)
    }
}