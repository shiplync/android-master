package com.traansmission.models;

import java.util.ArrayList;

/**
 * Created by SAMBUCA on 3/11/16.
 */
public class PaginatedShipments {
    public int count;
    public String next;
    public String previous;
    public ArrayList<Shipment> results;
}
