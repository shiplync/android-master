package com.traansmission.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traansmission.R;
import com.traansmission.models.Location;
import com.traansmission.models.Shipment;

import java.util.ArrayList;

/**
 * Created by SAMBUCA on 3/14/16.
 */
public class ShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> mData;
    private Context mContext;
    private final int SHIPMENT_FIELD = 0, SHIPMENT_SEPARATOR = 1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class FieldViewHolder extends RecyclerView.ViewHolder {
        public TextView _label;
        public TextView _value;

        public FieldViewHolder(View v) {
            super(v);
            _label = (TextView) v.findViewById(R.id.shipment_field_item_label);
            _value = (TextView) v.findViewById(R.id.shipment_field_item_value);
        }
    }

    public class SeparatorViewHolder extends RecyclerView.ViewHolder {
        public TextView _label;

        public SeparatorViewHolder(View v) {
            super(v);
            _label = (TextView) v.findViewById(R.id.shipment_section_separator_label);
        }
    }

//    public void add(int position, Shipment item) {
//        mData.add(mData.size(), item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(String item) {
//        int position = mData.indexOf(item);
//        mData.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public void removeAll() {
//        mData.clear();
//    }

    public class ShipmentField {
        public ShipmentField(String l, String v) {
            label = l;
            value = v;
        }
        String value;
        String label;
    }
    public void setData(Shipment shipment) {
        mData.add(new ShipmentSeparator("Shipment info"));
        setField("ID", Integer.toString(shipment.id));
        setField("Status", shipment.delivery_status.label);
        setField("BOL number", shipment.bol_number);
        setField("Trip distance", shipment.formattedTripDistance());
        setField("Cost", shipment.formattedPayout());
        setField("Cost per mile", shipment.formattedPayoutDistance());
        setField("Equipment", shipment.formattedEquipmentTags());

        for (Location location : shipment.locations) {
            mData.add(new ShipmentSeparator("Location " + Integer.toString(shipment.locations.indexOf(location)+1)));
            setField("Location type", location.location_type.label);
            setField("Arrive by (earliest)", location.time_range.timeRangeStartDescription());
            setField("Arrive by (latest)", location.time_range.timeRangeEndDescription());
            setField("Arrived at", location.arrivalTimeDescription());
            setField("Company", location.company_name);
            setField("Address", location.address_details.joinedAddress());
            setField("Contact name", location.contact.name);
            setField("Contact phone", location.contact.phone);
            setField("Contact email", location.contact.email);
            setField("Weight", location.features.weight != null && location.features.weight != 0 ? Integer.toString(location.features.weight.intValue()) + " pounds" : null);
            setField("Palletized", location.features.palletized ? "Yes" : null);
            setField("# of pallets", location.features.pallet_number != null && location.features.pallet_number != 0 ? Integer.toString(location.features.pallet_number) : null);
            setField("Pallet dimensions", location.features.palletDescription());
        }
    }
    void setField(String label, String value) {
        if(label != null && value != null && value != "") {
            mData.add(new ShipmentField(label, value));
        }
    }

    public class ShipmentSeparator {
        public ShipmentSeparator(String l) {
            label = l;
        }
        String label;
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public ShipmentAdapter(ArrayList<Object> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof ShipmentField) {
            return SHIPMENT_FIELD;
        } else if (mData.get(position) instanceof ShipmentSeparator) {
            return SHIPMENT_SEPARATOR;
        }
        return -1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = null;
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case SHIPMENT_FIELD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipment_field_item, parent, false);
                vh = new FieldViewHolder(v);
                break;
            case SHIPMENT_SEPARATOR:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipment_section_separator, parent, false);
                vh = new SeparatorViewHolder(v);
                break;

        }
        // set the view's size, margins, paddings and layout parameters
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        switch (holder.getItemViewType()) {
            case SHIPMENT_FIELD:
                final ShipmentField field = (ShipmentField) mData.get(position);
                FieldViewHolder fh = (FieldViewHolder) holder;
                fh._label.setText(field.label);
                fh._value.setText(field.value);
                break;
            case SHIPMENT_SEPARATOR:
                final ShipmentSeparator s = (ShipmentSeparator) mData.get(position);
                SeparatorViewHolder sh = (SeparatorViewHolder) holder;
                sh._label.setText(s.label);
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }

}
