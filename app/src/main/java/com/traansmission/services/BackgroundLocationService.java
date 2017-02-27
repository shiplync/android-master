package com.traansmission.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class BackgroundLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {//, LocationListener {
    private static final String LOG_TAG = BackgroundLocationService.class.getSimpleName();
    private static final long REPORTING_TIMEDELTA_THRESHOLD       = 15 * 60 * 1000;    // 15 minutes
    protected GoogleApiClient mGoogleApiClient;
    protected PendingIntent mPendingIntent;

    @Override
	public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.d("LOCATION_SERVICE", "Starting background location service");
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // GoogleApiClient.ConnectionCallbacks
    @Override
    public void onConnected(Bundle connectionHint) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(REPORTING_TIMEDELTA_THRESHOLD);
        mLocationRequest.setFastestInterval(REPORTING_TIMEDELTA_THRESHOLD - 5000);
        mLocationRequest.setMaxWaitTime(REPORTING_TIMEDELTA_THRESHOLD + 5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mPendingIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(this, BackgroundLocationServiceReceiver.class),
                PendingIntent.FLAG_CANCEL_CURRENT);
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
