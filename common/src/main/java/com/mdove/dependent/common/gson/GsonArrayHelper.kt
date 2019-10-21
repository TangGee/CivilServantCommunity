package com.mdove.dependent.common.gson

import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.mdove.dependent.common.network.NormalResp

import java.util.ArrayList

object GsonArrayHelper {
    @Throws(Exception::class)
    inline fun <reified T : Any> fromJsonArray(json: String): NormalResp<List<T>> {
        val message = JsonParser().parse(json).asJsonObject.get("msg").asString
        val status = JsonParser().parse(json).asJsonObject.get("status").asInt
        val normalResp = NormalResp<List<T>>(message = message, status = status)
        val lst = ArrayList<T>()
        val array = JsonParser().parse(json).asJsonObject.get("data").asJsonArray
        for (elem in array) {
            lst.add(GsonProvider.getDefaultGson().fromJson(elem, object : TypeToken<T>() {}.type))
        }
        normalResp.data=lst
        return normalResp
    }
}
