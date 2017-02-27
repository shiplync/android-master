package com.traansmission.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.traansmission.api.RestClient;

/**
 * Created by SAMBUCA on 3/7/16.
 */
public class Auth {

    public static String getToken(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String authToken = settings.getString("authToken", null);
        return authToken;
    }

    public static void setToken(Context context, String token) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("authToken", token);
        editor.commit();
        RestClient.getInstance(context).resetRestClient();
    }

    public static void logout(Context context) {
        setToken(context, null);
    }
}
