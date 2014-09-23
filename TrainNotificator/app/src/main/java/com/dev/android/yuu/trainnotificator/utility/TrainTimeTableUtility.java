package com.dev.android.yuu.trainnotificator.utility;

import android.content.Context;
import android.util.Log;

import com.dev.android.yuu.trainnotificator.Constants;
import com.dev.android.yuu.trainnotificator.UserDataManager;

import java.util.Calendar;

/**
 * Created by Chieko on 9/15/14.
 */
public class TrainTimeTableUtility
{

    public static String GetTodaysTimetable(Context context)
    {
        Log.d(TrainTimeTableUtility.class.toString(), "GetTodaysTimetable");

        String filename = "";

        int stationId = UserDataManager.GetStationId(context);
        String stationName = TrainTimeTableUtility.GetStationName(stationId);

        String dayName = TrainTimeTableUtility.getDayName();

        int directionType = UserDataManager.GetDirectionType(context);
        String directionName = TrainTimeTableUtility.getDirectionName(directionType);

        //String filePath = stationName + "/" + dayName + "/" + directionName + "_direction";
        //filePath += "/timetable_" + "shonanshinjukuline_" + dayName + "_" + directionName + "_direction.txt";

        filename = "timetable_shonanshinjukuline_" + stationName + "_" + dayName + "_" + directionName + "_direction.txt";

        //Log.d(TrainTimeTableUtility.class.toString(), "filePath: " + filePath);

        return filename;
    }

    public static String GetStationDisplayName(int id)
    {
        String stationName = "";

        switch(id)
        {
            case Constants.STATION_ID_YOKOHAMA:
                stationName = "横浜";
                break;

            case Constants.STATION_ID_SHINKAWASAKI:
                stationName = "新川崎";
                break;

            case Constants.STATION_ID_MUSASHIKOSUGI:
                stationName = "武蔵小杉";
                break;

            case Constants.STATION_ID_NISHIOI:
                stationName = "西大井";
                break;

            case Constants.STATION_ID_OSAKI:
                stationName = "大崎";
                break;

            default:
                break;
        }
        return stationName;
    }

    public static String GetStationName(int id)
    {
        String stationName = "";

        switch(id)
        {
            case Constants.STATION_ID_YOKOHAMA:
                stationName = "yokohama";
                break;

            case Constants.STATION_ID_SHINKAWASAKI:
                stationName = "shinkawasaki";
                break;

            case Constants.STATION_ID_MUSASHIKOSUGI:
                stationName = "musashikosugi";
                break;

            case Constants.STATION_ID_NISHIOI:
                stationName = "nishioi";
                break;

            case Constants.STATION_ID_OSAKI:
                stationName = "osaki";
                break;

            default:
                break;
        }
        return stationName;
    }

    private static String getDirectionName(int directionType)
    {
        Log.d(TrainTimeTableUtility.class.toString(), "getDirectionName: " + directionType);

        String directionName = "";

        switch (directionType)
        {
            case Constants.DIRECTION_TYPE_1:
                directionName = "shinjyuku";
                break;

            case Constants.DIRECTION_TYPE_2:
                directionName = "ofuna";
                break;

            default:
                break;
        }

        return directionName;
    }

    private static String getDayName()
    {
        Log.d(TrainTimeTableUtility.class.toString(), "getDayName");

        int dayType = CalendarUtility.GetCurrentDay();

        String dayName = "";

        switch (dayType)
        {
            case Calendar.SUNDAY:
                dayName = "holiday";
                break;

            case Calendar.MONDAY:
            case Calendar.TUESDAY:
            case Calendar.WEDNESDAY:
            case Calendar.THURSDAY:
            case Calendar.FRIDAY:
                dayName = "weekday";
                break;

            case Calendar.SATURDAY:
                dayName = "saturday";
                break;

            default:
                break;
        }

        return dayName;
    }


}
