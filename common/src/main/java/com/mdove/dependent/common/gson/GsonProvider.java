package com.mdove.dependent.common.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mdove.dependent.common.json.JSONArraySerializer;
import com.mdove.dependent.common.json.JSONObjectSerializer;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by MDove on 2019/9/6.
 */
public class GsonProvider {

    private static final Gson sDefault = new GsonBuilder()
        .registerTypeAdapter(JSONObject.class, new JSONObjectSerializer())
        .registerTypeAdapter(JSONArray.class, new JSONArraySerializer())
        .create();

    public static Gson getDefaultGson() {
        return sDefault;
    }
}
