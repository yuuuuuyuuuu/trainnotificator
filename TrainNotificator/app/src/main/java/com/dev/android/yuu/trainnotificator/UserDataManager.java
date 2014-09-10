package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.content.SharedPreferences;

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

}
