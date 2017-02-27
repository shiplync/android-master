package com.traansmission.models;

import com.google.gson.Gson;
import com.traansmission.utils.FormatUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SAMBUCA on 3/10/16.
 */
public class Shipment {
    public int id;
    public ShipmentStatus delivery_status;
    public String bol_number;
    public Float trip_distance;

    public ArrayList<Location> locations;
    public ArrayList<EquipmentTag> equipmenttags;
    public ShipmentPayout payout_info;

    public Location firstLocation() {
        if(locations.size() > 0) {
            return locations.get(0);
        } else {
            return null;
        }
    }

    public Location lastLocation() {
        if(locations.size() > 0) {
            return locations.get(locations.size()-1);
        } else {
            return null;
        }
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Shipment fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Shipment.class);
    }

    public String formattedPayout() {
        return payout_info.payout != null && payout_info.payout != 0 ? FormatUtils.formatCurrency(payout_info.payout) : null;
    }

    public String formattedPayoutDistance() {
        return payout_info.payout_distance != null && payout_info.payout_distance != 0 ? FormatUtils.formatCurrency(payout_info.payout_distance * FormatUtils.DISTANCE_MULTIPLIER) : null;
    }

    public String formattedTripDistance() {
        return trip_distance != 0 ? FormatUtils.formatDistance(trip_distance) : null;
    }

    public String formattedEquipmentTags() {
        StringBuilder result = new StringBuilder();
        for(EquipmentTag e: equipmenttags) {
            result.append(e.tag_type_label);
            result.append(", ");
        }
        return result.length() > 1 ? result.substring(0, result.length() - 2): "";
    }
}
