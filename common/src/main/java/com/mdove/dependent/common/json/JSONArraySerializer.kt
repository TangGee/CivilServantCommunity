package com.mdove.dependent.common.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.json.JSONArray
import java.lang.reflect.Type

class JSONArraySerializer : JsonSerializer<JSONArray> {
    override fun serialize(
        src: JSONArray?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return src?.toGson() ?: JsonArray()
    }
}