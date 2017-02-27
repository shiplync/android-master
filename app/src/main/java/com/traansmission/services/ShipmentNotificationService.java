package com.traansmission.services;

import android.app.IntentService;
import android.content.Intent;

import com.traansmission.activities.MainActivity;
import com.traansmission.activities.ShipmentActivity;
import com.traansmission.api.RestClient;
import com.traansmission.models.PaginatedShipments;
import com.traansmission.models.Shipment;
import com.traansmission.models.User;

import com.traansmission.shared.Notification;

import java.util.ArrayList;

import retrofit2.Call;


public class ShipmentNotificationService extends IntentService {

    private static final String TAG = "ShipNotificationService";
    public ShipmentNotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        User user;

        try{
            Call<User> call = RestClient.getInstance(getApplicationContext()).apiServiceAuthenticated().getUserSelf();
            user = call.execute().body();
        }catch(Exception e){
            bringToFront();
            return;
        }

        String s_shipment_id = intent.getStringExtra("shipment_id");
        int shipment_id = Integer.parseInt(s_shipment_id);
        String email = intent.getStringExtra("email");
        Shipment shipment = new Shipment();

        try{
            Call<PaginatedShipments> call = RestClient.getInstance(getApplicationContext()).apiServiceAuthenticated().getShipmentById(shipment_id);
            PaginatedShipments pShipments = call.execute().body();
            ArrayList<Shipment> shipments = pShipments.results;
            if(pShipments.count > 0){
                shipment = shipments.get(0);
            }else{
                shipment.id = -1;
            }
        }catch(Exception e){
            bringToFront();
            return;
        }

        if(!user.email.equals(email)){
            bringToFront();
            return;
        }

        Boolean wasKilled = Notification.getWasKilled(getApplicationContext());
        if(wasKilled){
            Notification.setPendingShipmentJson(getApplicationContext(),shipment.toJson());
            bringToFront();
            return;
        }

        Intent shipmentIntent = new Intent(this, ShipmentActivity.class);
        shipmentIntent.putExtra("shipment", shipment.toJson());
        shipmentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shipmentIntent);
    }

    private void bringToFront() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
    }

}
