package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dev.android.yuu.trainnotificator.utility.CalendarUtility;

import java.util.Calendar;

/**
 * Created by Chieko on 8/30/14.
 */
public class UserDataManager {

    // Key
    private static String PREFERENCE_KEY_TRAINNOTIFICATION_= "preference_key_trainnotificator";
    private static String DATA_KEY_START_HOUR_OF_DAY = "start_hour_of_day";
    private static String DATA_KEY_START_MINUTE = "start_minute";
    private static String DATA_KEY_END_HOUR_OF_DAY = "end_hour_of_day";
    private static String DATA_KEY_END_MINUTE = "end_minute";
    private static String DATA_KEY_DATE_TYPE = "date";
    private static String DATA_KEY_DIRECTION_TYPE = "direction";
    private static String DATA_KEY_STATION = "station";

    private static SharedPreferences LoadSharedPreference(Context context)
    {
        SharedPreferences sharedPreference = context.getSharedPreferences(UserDataManager.PREFERENCE_KEY_TRAINNOTIFICATION_, context.MODE_PRIVATE);

        return sharedPreference;
    }

    public static int[] GetStartTime(Context context)
    {
        int hourOfDay = 0;
        int minute = 0;

        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);
        hourOfDay = sp.getInt(UserDataManager.DATA_KEY_START_HOUR_OF_DAY, -1);
        minute = sp.getInt(UserDataManager.DATA_KEY_START_MINUTE, -1);

        int[] result = {hourOfDay, minute};

        return result;
    }

    public static int[] GetEndTime(Context context)
    {
        int hourOfDay = 0;
        int minute = 0;

        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);
        hourOfDay = sp.getInt(UserDataManager.DATA_KEY_END_HOUR_OF_DAY, -1);
        minute = sp.getInt(UserDataManager.DATA_KEY_END_MINUTE, -1);

        int[] result = {hourOfDay, minute};

        return result;
    }

    public static int GetDateType(Context context)
    {
        int dateType = 0;

        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);
        dateType = sp.getInt(UserDataManager.DATA_KEY_DATE_TYPE, -1);

        return dateType;
    }

    public static int GetDirectionType(Context context)
    {
        int directionType = 0;

        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);
        directionType = sp.getInt(UserDataManager.DATA_KEY_DIRECTION_TYPE, -1);

        return directionType;
    }

    public static int GetStationId(Context context)
    {
        int stationId = 0;

        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);
        stationId = sp.getInt(UserDataManager.DATA_KEY_STATION, -1);

        return stationId;
    }

    public static void SaveStartTime(int hourOfDay, int minute, Context context)
    {
        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UserDataManager.DATA_KEY_START_HOUR_OF_DAY, hourOfDay);
        editor.putInt(UserDataManager.DATA_KEY_START_MINUTE, minute);

        editor.commit();
    }

    public static void SaveEndTime(int hourOfDay, int minute, Context context)
    {
        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UserDataManager.DATA_KEY_END_HOUR_OF_DAY, hourOfDay);
        editor.putInt(UserDataManager.DATA_KEY_END_MINUTE, minute);

        editor.commit();
    }

    public static void SaveDateType(int dateType, Context context)
    {
        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UserDataManager.DATA_KEY_DATE_TYPE, dateType);

        editor.commit();
    }

    public static void SaveDirectionType(int directionType, Context context)
    {
        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UserDataManager.DATA_KEY_DIRECTION_TYPE, directionType);

        editor.commit();
    }

    public static void SaveStation(int stationId, Context context)
    {
        SharedPreferences sp = UserDataManager.LoadSharedPreference(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UserDataManager.DATA_KEY_STATION, stationId);

        editor.commit();
    }

    public static boolean IsNowInUserPreferableTime(Context context)
    {
        boolean result = false;

        int userDay = UserDataManager.GetDateType(context);
        int[] userStartTime = UserDataManager.GetStartTime(context);
        int userStartHourOfDay = userStartTime[0];
        int userStartMinute = userStartTime[1];
        int[] userEndTime = UserDataManager.GetEndTime(context);
        int userEndHourOfDay = userEndTime[0];
        int userEndMinute = userEndTime[1];

        int currentDay = CalendarUtility.GetCurrentDay();
        int currentHourOfDay = CalendarUtility.GetCurrentHourOfDay();
        int curentMinute = CalendarUtility.GetCurrentMinute();

        boolean isDayWithin = false;
        boolean isTimeWithin = false;
        boolean isMinuteWithin = false;

        // Day
        if(Constants.DATE_TYPE_WEEKDAY == userDay)
        {
            if(currentDay != Calendar.SUNDAY && currentDay != Calendar.SATURDAY)
            {
                isDayWithin = true;
            }
        }
        else if(Constants.DATE_TYPE_WEEKEND == userDay)
        {
            if(currentDay == Calendar.SUNDAY || currentDay == Calendar.SATURDAY)
            {
                isDayWithin = true;
            }
        }
        else if(Constants.DATE_TYPE_ALLDAY == userDay)
        {
            isDayWithin = true;
        }

        // Hour
        if(userStartHourOfDay < currentHourOfDay && currentHourOfDay < userEndHourOfDay)
        {
            isTimeWithin = true;
        }
        else if(userStartHourOfDay == currentHourOfDay)
        {
            if(userStartMinute <= currentHourOfDay)
            {
                isTimeWithin = true;
            }
        }
        else if(currentHourOfDay == userEndHourOfDay)
        {
            if(currentHourOfDay <= userEndHourOfDay)
            {
                isTimeWithin = true;
            }
        }

        if(isDayWithin && isTimeWithin) result = true;

        return result;
    }


}
