package com.traansmission.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by traansmission on 6/1/15.
 */
public class DateUtils {
    private static final String LOG_TAG = DateUtils.class.getSimpleName();
    private static final Calendar mCalendar = GregorianCalendar.getInstance();


    public static Date parseDate(String originalDateString) {
        Date date = parseISO8601Milliseconds(originalDateString);
        if (date == null) {
            date = parseISO8601(originalDateString);
        }
        return date;
    }

    private static Date parseISO8601Milliseconds(String dateString) {
        Date date = null;
        try {
            String formatString = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSSZ";
            DateFormat dateFormat = new SimpleDateFormat(formatString);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Zulu"));
            date = dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return date;
    }

    private static Date parseISO8601(String dateString) {
        Date date = null;
        try {
            String formatString = "yyyy'-'MM'-'dd'T'HH':'mm':'ssZ";
            DateFormat dateFormat = new SimpleDateFormat(formatString);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Zulu"));
            date = dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return date;
    }
}
