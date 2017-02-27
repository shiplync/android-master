package com.traansmission.models;

/**
 * Created by SAMBUCA on 3/14/16.
 */
public class AddressDetails {
    public String address;
    public String address_2;
    public String city;
    public String state;
    public String zip_code;

    public String joinedAddress() {
        String[] fields = {address, address_2, city, state, zip_code};
        StringBuilder result = new StringBuilder();
        for(String s: fields) {
            if(s != null && s != "") {
                result.append(s);
                result.append(", ");
            }
        }
        return result.length() > 1 ? result.substring(0, result.length() - 2): "";
    }
}
