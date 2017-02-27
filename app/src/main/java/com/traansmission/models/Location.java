package com.traansmission.models;

import com.traansmission.utils.FormatUtils;

import java.util.Date;

/**
 * Created by SAMBUCA on 3/14/16.
 */
public class Location {
    public AddressDetails address_details;
    public TimeRange time_range;
    public LocationType location_type;
    public Date arrived_at;
    public String company_name;
    public Person contact;
    public ShipmentFeatures features;

    public String arrivalTimeDescription() {
        return FormatUtils.formatDate(arrived_at, time_range.timeZone());
    }
}
