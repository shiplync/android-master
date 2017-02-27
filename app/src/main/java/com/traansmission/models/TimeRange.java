package com.traansmission.models;

import com.traansmission.utils.FormatUtils;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by SAMBUCA on 3/14/16.
 */
public class TimeRange {
    public Date time_range_start;
    public Date time_range_end;
    public String tz;

    public TimeZone timeZone() {
        TimeZone Tz = null;
        if (tz!= null) {
            Tz = TimeZone.getTimeZone(tz);
        }
        if (Tz == null) {
            Tz = TimeZone.getDefault();
        }
        return Tz;
    }

    public String timeRangeStartDescription() {
        return FormatUtils.formatDate(time_range_start, timeZone());
    }

    public String timeRangeEndDescription() {
        return FormatUtils.formatDate(time_range_end, timeZone());
    }
}
