package com.traansmission.models;

/**
 * Created by SAMBUCA on 3/15/16.
 */
public class ShipmentFeatures {
    public Float weight;

    public Boolean palletized;
    public Integer pallet_number;
    public Integer pallet_length;
    public Integer pallet_width;
    public Integer pallet_height;

    public String palletDescription() {
        String result = "";
        result += pallet_height != null && pallet_height != 0 ? "H: " + Integer.toString(pallet_height) + " " : "";
        result += pallet_width!= null && pallet_width!= 0 ? "W: " + Integer.toString(pallet_width) + " " : "";
        result += pallet_length!= null && pallet_length!= 0 ? "L: " + Integer.toString(pallet_length) + " " : "";
        return result;
    }
}