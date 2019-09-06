package com.mdove.dependent.common.gson

import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * Created by MDove on 2019/9/6.
 */
open interface IGsonCallBack {
    fun afterDeserialize()
    fun tag(): String {
        return "IGsonCallBack"
    }
}

fun <T> Gson.fromJsonWithCallback(str: String, clazz: Class<T>): T? where T : IGsonCallBack {
    var result: T? = null
    try {
        result = this.fromJson<T>(str, clazz)
        result.afterDeserialize()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun <T> Gson.fromJsonsWithCallback(str: String, clazz: Class<T>): ArrayList<T> where T : IGsonCallBack {
    var resultList: ArrayList<T> = ArrayList()
    try {
        //这样写可以避免泛型编译期擦除
        //http://lmh19941113.github.io/2015/12/24/GSON%E8%A7%A3%E6%9E%90Json/
        val array = JsonParser().parse(str).asJsonArray
        var result: T
        for (elem in array) {
            result = Gson().fromJson(elem, clazz)
            result.afterDeserialize()
            resultList.add(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return resultList
}