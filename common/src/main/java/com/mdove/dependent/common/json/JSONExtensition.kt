package com.mdove.dependent.common.json

import com.mdove.dependent.common.utils.forEach
import org.json.JSONObject

/**
 * Created by MDove on 2019/9/3.
 */
fun JSONObject.putAll(map: Map<String, Any>?) {
    map?.forEach {
        this.put(it.key, it.value)
    }
}

fun JSONObject.putAll(obj: JSONObject?) = apply {
    obj?.forEach { name, value ->
        put(name, value)
    }
}


fun JSONObject.putAllToMap(map: MutableMap<String, Any>) {
    this.forEach { key, value ->
        map[key] = value
    }
}