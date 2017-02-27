package com.traansmission.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.traansmission.R;
import com.traansmission.adapters.ShipmentAdapter;
import com.traansmission.models.Shipment;
import com.traansmission.services.BackgroundLocationService;

import java.util.ArrayList;

public class ShipmentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ShipmentAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String objAsJson = bundle.getString("shipment");
        Shipment shipment = Shipment.fromJson(objAsJson);

        if(shipment.id == -1){
            setContentView(R.layout.shipment_no_longer_exist);
            return;
        }

        setContentView(R.layout.activity_shipment);
        mRecyclerView = (RecyclerView) findViewById(R.id.shipment_field_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ShipmentAdapter(new ArrayList<Object>(), this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(shipment);
    }
}