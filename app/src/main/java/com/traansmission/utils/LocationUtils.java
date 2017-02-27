package com.traansmission.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.TimeZone;

/**
 * Created by traansmission on 5/13/15.
 */
public class LocationUtils {
    private static final Float mEarthRadiusMiles = 3959.0f;
    private static final Float mMetersPerMile = 1609.34f;

    private static LocationManager mLocationManager = null;

    public static LatLng UNDEFINED_LOCATION = new LatLng(0.0, 0.0);

    public static Float distanceMilesBetween(final LatLng latLng1, final LatLng latLng2) {
        final double lat1 = latLng1.latitude;
        final double lat2 = latLng2.latitude;

        final double lng1 = latLng1.longitude;
        final double lng2 = latLng2.longitude;

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return new Float(mEarthRadiusMiles * c);
    }


    public static JsonObject toJson(Location location) {
        JsonObject json = new JsonObject();
        json.addProperty("latitude", location.getLatitude());
        json.addProperty("longitude", location.getLongitude());
        json.addProperty("altitude", location.getAltitude());
        json.addProperty("accuracy", location.getAccuracy());
        json.addProperty("speed", location.getSpeed());
        json.addProperty("course", location.getBearing());
        json.addProperty("provider", location.getProvider());

        Date timestamp = new Date(location.getTime());
        String timestampString = FormatUtils.formatDateISO8601(timestamp, TimeZone.getDefault());
        json.addProperty("timestamp", timestampString);
        return json;
    }

    public static Location fromJson(JsonObject json) {
        Location location = new Location(json.get("provider").getAsString());
        location.setLatitude(json.get("latitude").getAsDouble());
        location.setLongitude(json.get("longitude").getAsDouble());

        if (location.hasAltitude()) {
            location.setAltitude(json.get("altitude").getAsDouble());
        }

        if (location.hasAccuracy()) {
            location.setAccuracy(json.get("accuracy").getAsFloat());
        }

        if (location.hasSpeed()) {
            location.setSpeed(json.get("speed").getAsFloat());
        }

        if (location.hasBearing()) {
            location.setBearing(json.get("course").getAsFloat());
        }

        Date timestamp = DateUtils.parseDate(json.get("timestamp").getAsString());
        if (timestamp == null) {
            timestamp = new Date();
        }
        location.setTime(timestamp.getTime());
        return location;
    }

    public static String toJsonString(Location location) {
        JsonObject locationJson = LocationUtils.toJson(location);
        return GsonUtil.getGson().toJson(locationJson);
    }

    public static Location fromJsonString(String locationJsonString) {
        JsonParser parser = new JsonParser();
        JsonObject locationJson = (JsonObject) parser.parse(locationJsonString);
        return LocationUtils.fromJson(locationJson);
    }

    public static String toGeoHeader(Location location) {
        StringBuilder builder = new StringBuilder();
        builder.append("geo:").append(location.getLongitude()).append(",").append(location.getLatitude());
        return builder.toString();
    }

    private static LocationManager getLocationManager(Context context) {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        return mLocationManager;
    }

}
