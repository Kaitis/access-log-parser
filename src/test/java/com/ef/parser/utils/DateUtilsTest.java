package com.ef.parser.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
public class DateUtilsTest {

    private static final SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void when_daily_then_calculateEndDate_correct() throws ParseException {
        //given
        String duration = "daily";
        Date date = dateFormat.parse("2019-01-01 16:00:00");

        //when
        Date end = DateUtils.calculateEndDate(date, duration);

        //then
        assertEquals(dateFormat.format(end), "2019-01-02 16:00:00");

    }

    @Test
    public void when_hourly_then_calculateEndDate_correct() throws ParseException {
        //given
        String duration = "hourly";
        Date date = dateFormat.parse("2019-01-01 16:00:00");

        //when
        Date end = DateUtils.calculateEndDate(date, duration);

        //then
        assertEquals(dateFormat.format(end), "2019-01-01 17:00:00");

    }

    @Test(expected = IllegalArgumentException.class)
    public void when_other_then_calculateEndDate_throws_IllegalArgumentException(){
        //given
        String duration = "other";
        Date date = new Date();

        //when
        Date end = DateUtils.calculateEndDate(date, duration);

        //then should throw Exception

    }
}