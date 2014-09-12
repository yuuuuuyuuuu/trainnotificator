package com.dev.android.yuu.trainnotificator.utility;

import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Chieko on 9/6/14.
 */
public class CalendarUtility
{
    private static final String TAG = "CalendarUtility";

    public static int GetCurrentDay()
    {
        Log.d(CalendarUtility.TAG, "GetCurrentHourOfDay()");
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int GetCurrentHourOfDay()
    {
        Log.d(CalendarUtility.TAG, "GetCurrentHourOfDay()");
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int GetCurrentMinute()
    {
        Log.d(CalendarUtility.TAG, "GetCurrentMinute()");
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.MINUTE);
    }

    public static int GetCurrentSecond()
    {
        Log.d(CalendarUtility.TAG, "GetCurrentSecond()");
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.SECOND);
    }

    public static int GetTodayDate()
    {
        Log.d(CalendarUtility.TAG, "GetTodayDate()");

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Log.d(CalendarUtility.TAG, "day:" + day);

        return day;
    }

    public static int GetTommorowDay()
    {
        Log.d(CalendarUtility.TAG, "GetTommorowDate()");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Log.d(CalendarUtility.TAG, "day:" + day);

        return day;
    }

    public static Calendar GetNextWeekdayCalendar()
    {
        Log.d(CalendarUtility.TAG, "GetNextWeekdayCalendar()");

        Calendar calendar = null;

        return calendar;
    }

    public static Calendar GetNextSaturdayCalendar()
    {
        Log.d(CalendarUtility.TAG, "GetNextSaturdayCalendar()");

        Calendar calendar = null;

        return calendar;
    }

    public static Calendar GetNextSundayCalendar()
    {
        Log.d(CalendarUtility.TAG, "GetNextSundayCalendar()");

        Calendar calendar = null;

        return calendar;
    }

    public static Calendar GetNextDateCalendar(int calendarDateName)
    {
        Log.d(CalendarUtility.TAG, "GetNextDateCalendar(" + calendarDateName + ")");

        Calendar calendar = null;

        return calendar;
    }





}
