package com.mdove.dependent.common.network

import com.google.gson.JsonElement
import com.mdove.dependent.common.utils.fromJson
import com.mdove.dependent.common.utils.fromServerResp

class ServerRespException(
    val errorCode: String,
    message: String? = null,
    val resp: JsonElement? = null,
    val exception: Exception? = null
) : RuntimeException("resp: $resp\nmessage: $message\nerror_code: $errorCode", exception)

fun <T> ServerRespException.toNormaResp():NormalResp<T>{
    val errorResp = try {
        fromJson<NormalErrorResp>(this.resp)
    } catch (e: Exception) {
        NormalErrorResp()
    }
    return NormalResp<T>(errorResp.message,status = errorResp.status)
}