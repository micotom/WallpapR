package com.dev.funglejunk.wallpapr.util;

import java.text.DateFormat;
import java.util.Date;

public class StringUtils {

    public static String getLocalDateFromMilliseconds(long milliseconds) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        return format.format(new Date(milliseconds));
    }

}
