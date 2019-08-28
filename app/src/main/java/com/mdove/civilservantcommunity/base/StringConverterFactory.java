package com.mdove.civilservantcommunity.base;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class StringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Type token = new TypeToken<String>() {
        }.getType();
        if (type == token) {
            return new ResponseStringConverter();
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }

    private class ResponseStringConverter implements Converter<ResponseBody, String> {

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new RequestStringConver();
    }

    private class RequestStringConver implements Converter<String, RequestBody> {

        @Override
        public RequestBody convert(String value) {
            return RequestBody.create(MEDIA_TYPE, value);
        }
    }
}
