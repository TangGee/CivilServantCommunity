package com.mdove.dependent.networkprovider.base

import okhttp3.Headers
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call

/**
 * Created by MDove on 2019-09-03.
 */
open class BaseApiClient {

    @Throws(Exception::class)
    protected fun <T> execute(
        call: Call<T>,
        list: MutableList<Headers>?
    ): T {
        try {
//            if (!NetworkUtils.isNetworkAvailable(BaseApplication.getInst())) {
//                throw CustomNetworkUnavailableException(NetworkUtils.NO_NETWORK_DESC)
//            }
            val response = call.execute()
            list?.add(response.headers().newBuilder().build())
            return getBody<T>(call, response)
        } catch (e: Exception) {
            throw e
        }
    }

    @Throws(Exception::class)
    protected fun <T> execute(
        call: Call<T>,
        list: MutableList<Headers>?,
        respHeaders: MutableMap<String, String>?
    ): T {
        try {
//            if (!NetworkUtils.isNetworkAvailable(BaseApplication.getInst())) {
//                throw CustomNetworkUnavailableException(NetworkUtils.NO_NETWORK_DESC)
//            }

            val response = call.execute()

            list?.add(response.headers().newBuilder().build())

            if (respHeaders != null) {
                val headers = response.headers()
                if (headers != null) {
                    for (name in headers.names()) {
                        respHeaders[name] = headers[name]!!
                    }
                }
            }
            return getBody(call, response)
        } catch (e: Exception) {
            throw e
        }
    }


    @Throws(JSONException::class, RuntimeException::class)
    private fun <T> getBody(call: Call<T>, response: retrofit2.Response<T>): T {
        if (response.isSuccessful) {
            return response.body()
        } else {
            val json = JSONObject()
            val headers = response.headers()
            if (headers != null) {
                for (name in headers.names()) {
                    json.put(name, headers[name])
                }
            }
            throw RuntimeException(
                "response code:$response.code()" + "\n" + call.request().url.toString() + "\n" + response.code() + "\n" + json.toString(
                    2
                ) + "\n" + response.message()
            )
        }
    }
}