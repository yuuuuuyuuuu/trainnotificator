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
        String stationName = TrainTimeTableUtility.getStationName(stationId);

        String dayName = TrainTimeTableUtility.getDayName();

        int directionType = UserDataManager.GetDirectionType(context);
        String directionName = TrainTimeTableUtility.getDirectionName(directionType);

        String filePath = stationName + "/" + dayName + "/" + directionName;

        Log.d(TrainTimeTableUtility.class.toString(), "filePath: " + filePath);

        return filename;
    }

    private static String getStationName(int id)
    {
        String stationName = "";

        switch(id)
        {
            case Constants.STATION_ID_YOKOHAMA:
                stationName = "yokohama";
                break;

            case Constants.STATION_ID_OSAKI:
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
                directionName = "shinjuku";
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
                dayName = "weekend";
                break;

            default:
                break;
        }

        return dayName;
    }


}
