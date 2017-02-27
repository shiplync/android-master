package com.traansmission.adapters;

/**
 * Created by SAMBUCA on 3/11/16.
 */
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traansmission.R;
import com.traansmission.activities.ShipmentActivity;
import com.traansmission.models.Shipment;

public class ShipmentsAdapter extends RecyclerView.Adapter<ShipmentsAdapter.ViewHolder> {
    private ArrayList<Shipment> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView _id;
        public TextView _status;
        public TextView _from_address;
        public TextView _from_date;
        public TextView _to_address;
        public TextView _to_date;

        public ViewHolder(View v) {
            super(v);
            View container = v.findViewById(R.id.shipments_list_item_container);
            container.setOnClickListener(this);

            _id = (TextView) v.findViewById(R.id.shipments_list_item_id);
            _status = (TextView) v.findViewById(R.id.shipments_list_item_status);
            _from_address = (TextView) v.findViewById(R.id.shipments_list_item_from_address);
            _from_date= (TextView) v.findViewById(R.id.shipments_list_item_from_date);
            _to_address = (TextView) v.findViewById(R.id.shipments_list_item_to_address);
            _to_date= (TextView) v.findViewById(R.id.shipments_list_item_to_date);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Shipment s = mDataset.get(i);
            Intent intent = new Intent(mContext, ShipmentActivity.class);
            intent.putExtra("shipment", s.toJson());
            mContext.startActivity(intent);
        }
    }

    public void add(int position, Shipment item) {
        mDataset.add(mDataset.size(), item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        mDataset.clear();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShipmentsAdapter(ArrayList<Shipment> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShipmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipments_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

            final Shipment shipment = mDataset.get(position);
            holder._id.setText("#" + Integer.toString(shipment.id));
            holder._status.setText(shipment.delivery_status.label);
            holder._from_address.setText(shipment.firstLocation().address_details.city + ", " + shipment.firstLocation().address_details.state);
            holder._to_address.setText(shipment.lastLocation().address_details.city + ", " + shipment.lastLocation().address_details.state);
            holder._from_date.setText(shipment.firstLocation().time_range.timeRangeEndDescription());
            holder._to_date.setText(shipment.lastLocation().time_range.timeRangeEndDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}