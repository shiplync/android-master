package com.traansmission.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yaoxiao on 7/1/16.
 */
public class Notification {
    public static String getDeviceToken(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String token = settings.getString("device_token", null);
        return token;
    }

    public static void setDeviceToken(Context context, String token) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("device_token", token);
        editor.commit();
    }

    public static Boolean getWasKilled(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean wasKilled = settings.getBoolean("wasKilled", false);
        return wasKilled;
    }

    public static void setWasKilled(Context context, Boolean wasKilled) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("wasKilled",wasKilled);
        editor.commit();
    }

    public static String getPendingShipmentJson(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String pendingShipmentJson = settings.getString("pendingShipmentJson", null);
        return pendingShipmentJson;
    }

    public static void setPendingShipmentJson(Context context, String pendingShipmentJson) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pendingShipmentJson", pendingShipmentJson);
        editor.commit();
    }

}

