package com.traansmission.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class BackgroundOneshotLocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {//, LocationListener {

    protected GoogleApiClient mGoogleApiClient;
    protected PendingIntent mPendingIntent;

    public BackgroundOneshotLocationService() {
        super("BackgroundOneshotLocationService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.d("LOCATION_SERVICE", "Starting background oneshot location service");
        mGoogleApiClient.connect();

        // Disconnect after 30 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                    Log.d("LOCATION_SERVICE", "Closing google client");
                }
            }
        }, 30 * 1000);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // GoogleApiClient.ConnectionCallbacks
    @Override
    public void onConnected(Bundle connectionHint) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setMaxWaitTime(3000);
        Log.d("LOCATION_SERVICE", "Connected to google client");

        // Try sending two location updates to server

        // Send without GPS
        mPendingIntent = PendingIntent.getBroadcast(this, 101,
                new Intent(this, BackgroundLocationServiceReceiver.class),
                PendingIntent.FLAG_ONE_SHOT);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
        } catch (SecurityException e) {
        }

        // Send with GPS
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mPendingIntent = PendingIntent.getBroadcast(this, 101,
                new Intent(this, BackgroundLocationServiceReceiver.class),
                PendingIntent.FLAG_ONE_SHOT);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
        } catch (SecurityException e) {
        }
    }

    // GoogleApiClient.ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int cause) {
        // NO-OP
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // NO-OP
    }
}
