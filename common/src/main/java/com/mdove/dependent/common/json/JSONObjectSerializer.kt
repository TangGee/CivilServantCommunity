package com.mdove.dependent.common.json

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * Created by MDove on 2019/9/3.
 */
class JSONObjectSerializer : JsonSerializer<JSONObject> {
    override fun serialize(
        src: JSONObject?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return src?.toGson() ?: JsonObject()
    }
}