package com.dev.android.yuu.trainnotificator.utility;

import android.content.Context;
import android.util.Log;

import com.dev.android.yuu.trainnotificator.Constants;
import com.dev.android.yuu.trainnotificator.SettingFragment;
import com.dev.android.yuu.trainnotificator.UserDataManager;

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

    public static boolean IsNowInUserPreferenceTime(Context context)
    {
        Log.d(CalendarUtility.TAG, "IsNowInUserPreferenceTime");

        boolean result = false;

        int[] startTime = UserDataManager.GetStartTime(context);
        int[] endTime = UserDataManager.GetEndTime(context);
        int dayType = UserDataManager.GetDateType(context);
        int todaysDay = CalendarUtility.GetCurrentDay();

        boolean isDayInPreference = false;
        boolean isTimeInPreference = false;

        // compare day
        switch (dayType)
        {
            case Constants.DATE_TYPE_WEEKDAY:
                if(Calendar.SUNDAY != todaysDay && Calendar.SATURDAY != todaysDay)
                {
                    isDayInPreference = true;
                }
                break;

            case Constants.DATE_TYPE_WEEKEND:
                if(Calendar.SUNDAY == todaysDay || Calendar.SATURDAY == todaysDay)
                {
                    isDayInPreference = true;
                }
                break;

            case Constants.DATE_TYPE_ALLDAY:
                isDayInPreference = true;
                break;
        }

        // compare time
        if(isDayInPreference)
        {
            Log.d(CalendarUtility.TAG, "Day is in preference");

            Calendar userStartCalendar = Calendar.getInstance();
            userStartCalendar.set(Calendar.HOUR_OF_DAY, startTime[0]);
            userStartCalendar.set(Calendar.MINUTE, startTime[1]);
            long userStartTime = userStartCalendar.getTimeInMillis();

            Calendar userEndCalendar = Calendar.getInstance();
            userEndCalendar.set(Calendar.HOUR_OF_DAY, endTime[0]);
            userEndCalendar.set(Calendar.MINUTE, endTime[1]);
            long userEndTime = userEndCalendar.getTimeInMillis();

            Calendar currentCalendar = Calendar.getInstance();
            long currentTime = currentCalendar.getTimeInMillis();

            if(userStartTime < currentTime && currentTime < userEndTime)
            {
                Log.d(CalendarUtility.TAG, "Time is in preference");
                isTimeInPreference = true;
            }
            else
            {
                Log.d(CalendarUtility.TAG, "userStartTime: " + userStartTime + " currentTime:" + currentTime + " userEndTime:" + userEndTime);
            }
        }

        if(isDayInPreference && isTimeInPreference)
        {
            Log.d(CalendarUtility.TAG, "It is in user preference");
            result = true;
        }
        else
        {
            Log.d(CalendarUtility.TAG, "isDayInPreference: " + isDayInPreference + "  isTimeInPreference:" + isTimeInPreference);
        }

        return result;
    }

}
