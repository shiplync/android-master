package com.traansmission.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.google.gson.JsonObject;
import com.traansmission.api.RestClient;
import com.traansmission.utils.LocationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SAMBUCA on 11/2/15.
 */
public class BackgroundLocationServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LOCATION_SERVICE", "Received location");
        if (LocationResult.hasResult(intent)) {
            final Location location = LocationResult.extractResult(intent).getLastLocation();
            if(location != null) {
                postLocationJson(location, context);
            }
        }
    }

    private void postLocationJson(Location location, Context context) {
        Log.d("LOCATION_SERVICE", "Sending location");
        JsonObject locationJson = LocationUtils.toJson(location);
        Call<JsonObject> call = RestClient.getInstance(context).apiServiceAuthenticated().postGeolocation(locationJson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Call c = call;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Call c = call;
            }
        });
    }
}