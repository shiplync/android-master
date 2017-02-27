package com.traansmission.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by traansmission on 5/12/15.
 */
public class FormatUtils {

    public static final float DISTANCE_MULTIPLIER = 1609.344f; //meters to miles
    private static final Locale mLocale = Locale.US;
    private static final NumberFormat mCurrencyFormat = NumberFormat.getCurrencyInstance(mLocale);
    private static final NumberFormat mDistanceFormat = NumberFormat.getNumberInstance(mLocale);
    private static final Calendar mCalendar = GregorianCalendar.getInstance();

//    private static final String mDateTimeZoneFormatString = "MM/dd/yy, hh:mm a zzz";
//    private static final String mDateTimeFormatString = "MM/dd/yy, hh:mm a";
//    private static final String mTimeZoneFormatString = "hh:mm a zzz";
    private static final String mISO8601DateTimeFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String mDateFormatString = "MM/dd/yy HH:mm zzz";

//    private static final SimpleDateFormat mDateTimeZoneFormat = new SimpleDateFormat(mDateTimeZoneFormatString, mLocale);
//    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat(mDateTimeZoneFormatString, mLocale);
//    private static final SimpleDateFormat mTimeZoneFormat = new SimpleDateFormat(mTimeZoneFormatString, mLocale);
    private static final SimpleDateFormat mISO8601DateTimeFormat = new SimpleDateFormat(mISO8601DateTimeFormatString, mLocale);
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat(mDateFormatString, mLocale);

    public static String formatCurrency(Float amount) {
        if(amount == null ) {
            return null;
        } else {
            return mCurrencyFormat.format(amount);
        }
    }

    public static Float convertDistance(Float distance) {
        //meters to miles
        return distance / DISTANCE_MULTIPLIER;
    }

    public static String formatDistance(Float distance) {
        if(distance== null ) {
            return null;
        } else {
            mDistanceFormat.setMaximumFractionDigits(0);
            Float distance_miles = convertDistance(distance);
            return mDistanceFormat.format(distance_miles) + " miles";
        }
    }

//    public static String formatDateRange(Date start, Date end, TimeZone timeZone) {
//        if (start.compareTo(end) == 0) {
//            return formatDateTimeZone(start, timeZone);
//        }
//
//        mDateTimeFormat.setTimeZone(timeZone);
//        mTimeZoneFormat.setTimeZone(timeZone);
//        StringBuilder builder = new StringBuilder();
//        if (isDateRangeSameDay(start, end)) {
//            builder.append(mDateTimeFormat.format(start))
//                    .append(" - ")
//                    .append(mTimeZoneFormat.format(end));
//        }
//        else {
//            builder.append(formatDate(start, timeZone))
//                    .append("\n")
//                    .append(formatDate(end, timeZone));
//        }
//        return builder.toString();
//    }

    public static String formatDate(Date date, TimeZone timeZone) {
        if (date != null && timeZone != null) {
            mDateFormat.setTimeZone(timeZone);
            return mDateFormat.format(date);

//            mISO8601DateTimeFormat.setTimeZone(timeZone);
//            return mISO8601DateTimeFormat.format(date);
        } else {
            return "";
        }
    }

    public static String formatDateISO8601(Date date, TimeZone timeZone) {
        if (date != null && timeZone != null) {
            mISO8601DateTimeFormat.setTimeZone(timeZone);
            return mISO8601DateTimeFormat.format(date);
        } else {
            return "";
        }
    }

//    public static String formatDateTimeZone(Date date, TimeZone timeZone) {
////        mCalendar.setTime(date);
////        java.text.DateFormat dateFormat = new SimpleDateFormat(mDateTimeZoneFormatString, Locale.US);
////        return dateFormat.format(mCalendar.getTime());
//    }

    private static Boolean isDateRangeSameDay(Date start, Date end) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);

        return startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR)
                && startCal.get(Calendar.DAY_OF_YEAR) == endCal.get(Calendar.DAY_OF_YEAR);
    }
}
