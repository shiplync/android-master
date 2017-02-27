package com.traansmission.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by traansmission on 4/16/15.
 */
public class GsonUtil {

    private static final Gson mGson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

    public static final Gson getGson() {
        return mGson;
    }
}
