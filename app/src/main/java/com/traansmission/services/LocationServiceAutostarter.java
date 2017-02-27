package com.traansmission.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class LocationServiceAutostarter extends BroadcastReceiver
{
    public void onReceive(Context arg0, Intent arg1)
    {
        Intent locationService = new Intent(arg0,BackgroundLocationService.class);
        arg0.startService(locationService);

        Log.i("Autostart", "started");
    }
}
