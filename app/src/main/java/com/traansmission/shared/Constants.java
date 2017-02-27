package com.traansmission.shared;

import com.traansmission.EnvironmentConstants;

public class Constants {
	public static final String SHARED_PREFS_FILE = "TraansmissionPrefs20";
    public static final String SHARED_TOKEN = "preftoken";
    public static final String SHARED_CARRIER = "prefcarrier";

	public static final String SHARED_EMAIL_TAG = "email";
    public static final String SHARED_COMPANY_TAG = "company";
	public static final String SHARED_FIRSTNAME_TAG = "firstname";
	public static final String SHARED_LASTNAME_TAG = "lastname";
	public static final String SHARED_PHONE_TAG = "phone";
	public static final String SHARED_MCDOT_TAG = "mcdot";
	public static final String SHARED_VEHICLE_TAG = "vehicle";
	public static final String SHARED_IMAGE_URI = "image_uri";

	public static final String INTENT_PASSWORD_TAG = "password";

    public static final String HTTP_RESULT = "httpresult";
	public static final String HTTP_POST_CARRIER = "postcarrier";
	public static final String HTTP_SEND_VERIFICATION_EMAIL = "sendverificationemail";
	public static final String HTTP_GET_CARRIER_STATUS = "getcarrierstatus";
	public static final String HTTP_GET_SHIPMENTS = "getshipments";
	public static final String HTTP_PATCH_CARRIER = "patchcarrier";

	public static final String INTENT_UPDATE_SHIPMENTS = "updateshipmentsintent";
	public static final String EXTRA_JSON_OBJECT = "jsonobject";
	

	public static final String FILTER_PICKUP_DISTANCE = "filterpickupdistanc";
	public static final String FILTER_TRIP_DISTANCE = "filtertripdistanc";
	public static final String FILTER_PICKUP_DATETIME = "filterpickupdatetime";

	public static final String FILTER_ORDERING = "filterordering";
	public static final String FILTER_VEHICLE_TYPE = "filtervehicletype";

	public static final String FILTER_ORDERING_CLOSEST = "shipper_proximity";
	public static final String FILTER_ORDERING_SOONEST = "pick_up_time_range_end";

	public static final String VALUE_NOT_FOUND = "novalue";
	
    public static final String BASE_URL = EnvironmentConstants.BASE_URL;

    public static final String MIXPANEL_TOKEN = EnvironmentConstants.MIXPANEL_TOKEN;;

    public static final String IMAGE_RESOURCE_ID = "iconResourceID";
    public static final String ITEM_NAME = "itemName";

    public static final String SHIPMENT = "shipment";
    public static final String URL = "url";
    public static final String TITLE = "title";

    public static final Integer SHIPMENT_DETAILS_CODE = 1001;

	public static final String LOCATION_INTENT = "Location Intent";
	public static final String LOCATION_EXTRA = "Location Extra";
	public static final String LOCATION_LAST = "Location Last";

	public static enum ERR_TYPE {
		UNKNOWN,
		NETWORK
	}
}
