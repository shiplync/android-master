/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.traansmission.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.traansmission.R;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private static int unique_id = 0;

    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String email = data.getString("email");
        String title = data.getString("title");
        String shipment_id = data.getString("shipment_id");
        String locationBroadcast = data.getString("location_broadcast");

        Log.i(TAG, "From: " + from);

        if (locationBroadcast != null) {
            Log.i(TAG, "Location Broadcast: " + locationBroadcast);
            Intent backgroundLocationIntent = new Intent(this, BackgroundOneshotLocationService.class);
            startService(backgroundLocationIntent);
        }

        if (shipment_id != null) {
            Log.i(TAG, "Shipment Notification: " + shipment_id);
            createShipmentNotification(title, message, shipment_id, email);
        }
    }

    private void createShipmentNotification(String title, String message, String shipment_id, String email) {
        unique_id = unique_id + 1;
        Intent intent = new Intent(this, ShipmentNotificationService.class);

        //Besides of unique_id, also need to make intent different
        //so that multiple notification can be triggered instead of newer substituting the older one
        intent.setAction(Long.toString(System.currentTimeMillis()));

        intent.putExtra("shipment_id",shipment_id);
        intent.putExtra("email",email);

        PendingIntent pendingIntent = PendingIntent.getService(this, unique_id, intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(unique_id, notificationBuilder.build());
    }
}
