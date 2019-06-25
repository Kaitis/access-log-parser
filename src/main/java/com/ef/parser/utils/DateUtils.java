package com.ef.parser.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
public class DateUtils {


    public static Date calculateEndDate(Date startDate, String duration) throws IllegalArgumentException{

        if (!duration.equalsIgnoreCase("daily") &&
                !duration.equalsIgnoreCase("hourly")){
           throw new IllegalArgumentException("Duration can only be 'daily' or 'hourly'");
        }

        int hours = 1;

        if (duration.equalsIgnoreCase("daily")) {
            hours = 24;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return calendar.getTime();
    }
}

