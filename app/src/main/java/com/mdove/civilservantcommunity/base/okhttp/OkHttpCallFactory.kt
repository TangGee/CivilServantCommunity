package com.mdove.civilservantcommunity.base.okhttp

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by MDove on 2019/3/25.
 */
class OkHttpCallFactory(private val client : OkHttpClient) : Call.Factory {
    override fun newCall(request: Request): Call {
        val newRequest = request.newBuilder().build()
        return client.newCall(newRequest)
    }
}