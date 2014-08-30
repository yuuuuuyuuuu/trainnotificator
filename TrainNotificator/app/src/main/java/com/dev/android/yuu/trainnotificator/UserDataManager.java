package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Chieko on 8/30/14.
 */
public class UserDataManager {



    public static void Load(Context context)
    {
        String PREF_KEY = "preference_key_trainnotificator";
        SharedPreferences sharedPreference = context.getSharedPreferences(PREF_KEY, context.MODE_PRIVATE);

    }

    public static int[] GetStartTime()
    {
        int hourOfDay = 0;
        int minute = 0;


        int[] result = {hourOfDay, minute};

        return result;
    }

    public static int[] GetEndTime()
    {
        int hourOfDay = 0;
        int minute = 0;


        int[] result = {hourOfDay, minute};

        return result;
    }

    public static int GetDateType()
    {
        int dateType = 0;

        return dateType;
    }

    public static void SaveStartTime(int hourOfDay, int minute)
    {

    }

    public static void SaveEndTime(int hourOfDay, int minute)
    {

    }

    public static void SaveDateType(int dateType)
    {

    }



}
