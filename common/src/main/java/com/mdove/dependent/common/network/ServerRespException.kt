package com.mdove.dependent.common.network

import com.google.gson.JsonElement

class ServerRespException(
    val errorCode: String,
    message: String? = null,
    val resp: JsonElement? = null,
    val exception: Exception? = null
) : RuntimeException("resp: $resp\nmessage: $message\nerror_code: $errorCode", exception)
