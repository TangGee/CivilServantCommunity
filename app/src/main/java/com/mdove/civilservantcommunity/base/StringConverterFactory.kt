package com.mdove.civilservantcommunity.base

import com.google.gson.reflect.TypeToken

import java.io.IOException
import java.lang.reflect.Type

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

class StringConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        val token = object : TypeToken<String>() {

        }.type
        return if (type === token) {
            ResponseStringConverter()
        } else super.responseBodyConverter(type, annotations, retrofit)
    }

    private inner class ResponseStringConverter : Converter<ResponseBody, String> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            return value.string()
        }
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody> {
        return RequestStringConver()
    }

    private inner class RequestStringConver : Converter<String, RequestBody> {

        override fun convert(value: String): RequestBody {
            return RequestBody.create(MEDIA_TYPE, value)
        }
    }

    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
    }
}
