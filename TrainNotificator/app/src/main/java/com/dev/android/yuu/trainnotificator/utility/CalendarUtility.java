package com.dev.android.yuu.trainnotificator.utility;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Chieko on 9/6/14.
 */
public class CalendarUtility
{
    private static final String TAG = "CalendarUtility";

    public static int GetTodayDate()
    {
        Log.d(CalendarUtility.TAG, "GetTodayDate()");

        Calendar calendar = null;

        return Calendar.SUNDAY;
    }

    public static int GetTommorowDate()
    {
        Log.d(CalendarUtility.TAG, "GetTommorowDate()");

        return Calendar.SUNDAY;
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
