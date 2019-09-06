package com.mdove.dependent.common.network

import com.google.gson.JsonElement

class LocalRespException(val errorCode: String, message: String? = null, val resp: JsonElement? = null, val exception : Exception? = null) : RuntimeException(message, exception)
