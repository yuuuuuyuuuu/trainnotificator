package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.dev.android.yuu.trainnotificator.utility.CalendarUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Chieko on 8/31/14.
 */
public class TrainTimeTableModel
{
    private Context mContext = null;

    private ArrayList<TrainTimeData> mWeekdayTrainTimeDataList = null;
    private ArrayList<TrainTimeData> mWeekendTrainTimeDataList = null;

    /* test */
    private static final String FILENAME_TIMETABLE_SOTESTU_RYOKUENTOSHI_WEEKDAY = "timetable_sotetsu_ryokuen.txt";

    public TrainTimeTableModel(Context context)
    {
        Log.d(this.getClass().toString(), "TrainTimeTableModel()");

        this.mContext = context;

        this.loadTimeTable();
    }

    public TrainTimeData GetNextTrainTime()
    {
        Log.d(this.getClass().toString(), "GetNextTrainTime()");

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TrainTimeData nextTrainData = this.findNextTrainTime(hourOfDay, minute);
        if(null == nextTrainData)
        {
            Log.d(this.getClass().toString(), "nextTrainData is null");
        }
        else
        {
            Log.d(this.getClass().toString(), "nextTrainData hourOfDay:" + nextTrainData.HourOfDay() + "  minute:" + nextTrainData.Minute());
        }

        return nextTrainData;
    }

    public TrainTimeData GetNextTraimTime(int targetHourOfDay, int targetMinute)
    {
        Log.d(this.getClass().toString(), "GetNextTraimTime(" + targetHourOfDay + "," + targetMinute + ")");

        TrainTimeData nextTrainData = this.findNextTrainTime(targetHourOfDay, targetMinute);

        return nextTrainData;
    }

    public int GetTrainTimeDataNumber()
    {
        Log.d(this.getClass().toString(), "GetTrainTimeDataNumber");
        return this.mWeekdayTrainTimeDataList.size();
    }


    private TrainTimeData findNextTrainTime(int hourOfDay, int minute)
    {
        Log.d(this.getClass().toString(), "findNextTrainTime(" + hourOfDay + ", " + minute + ")");

        // search weekday time table
        if(null == this.mWeekdayTrainTimeDataList)
        {
            Log.e(this.getClass().toString(), "mWeekdayTrainTimeDataList in null.");
            return null;
        }

        TrainTimeData nextTrainTimeData = null;
        for(TrainTimeData timeData : this.mWeekdayTrainTimeDataList)
        {
            // past time data
            if(timeData.HourOfDay() < hourOfDay) continue;

            // TODO: may be able to combine if statements
            if(timeData.HourOfDay() == hourOfDay)
            {
                if(timeData.Minute() <= minute) continue;

                // Next data found
                nextTrainTimeData = timeData;
                Log.d(this.getClass().toString(), "next data found. hourOfDay:" + timeData.HourOfDay() + "  minute:" + timeData.Minute());
                break;
            }
            else if(hourOfDay < timeData.HourOfDay())
            {
                // Next data found
                nextTrainTimeData = timeData;
                Log.d(this.getClass().toString(), "next data found. hourOfDay:" + timeData.HourOfDay() + "  minute:" + timeData.Minute());
                break;
            }

        }

        if(null == nextTrainTimeData)
        {
            Log.d(this.getClass().toString(), "next data NOT found. Setting first train data");
            nextTrainTimeData = this.mWeekdayTrainTimeDataList.get(0);
        }

        return nextTrainTimeData;

    }

    private void loadTimeTable()
    {
        Log.d(this.getClass().toString(), "loadTimeTable()");

        this.mWeekdayTrainTimeDataList = new ArrayList<TrainTimeData>();
        AssetManager am = this.mContext.getAssets();

        InputStream is = null;

        String dataTableFileName = this.getTableDataFile();
        Log.d(this.getClass().toString(), "time table file: " + dataTableFileName);

        try
        {
            is = am.open(dataTableFileName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = "";
        while(null != line)
        {
            try
            {
                line = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if(null == line) break;

            if(!(line.equals("start") || line.equals("end")))
            {
                int hourOfDay = this.extractHourOfDay(line);
                ArrayList<Integer> minuteArray = this.extractMinute(line);

                for(Integer minuteData : minuteArray)
                {
                    TrainTimeData newData = new TrainTimeData(hourOfDay, minuteData, 0, "");
                    this.mWeekdayTrainTimeDataList.add(newData);
                }
            }

            Log.d(this.getClass().toString(), "line: " + line);
        }

        Log.d(this.getClass().toString(), "this.mWeekdayTrainTimeDataList.size(): " + this.mWeekdayTrainTimeDataList.size());

    }

    private int extractHourOfDay(String lineData)
    {
        Log.d(this.getClass().toString(), "extractHourOfDay(" + lineData + ")");

        int hourOfDay = -1;

        int EXPECTED_SPLITTED_NUMBER = 2;
        String[] splitted = lineData.split(":");

        if(EXPECTED_SPLITTED_NUMBER != splitted.length)
        {
            Log.e(this.getClass().toString(), "splitted.length is NOT 2 in extractHourOfDay");
            return -1;
        }

        hourOfDay = Integer.parseInt(splitted[0]);
        Log.d(this.getClass().toString(), "extracted hourOfDay: " + hourOfDay);

        return hourOfDay;
    }

    private ArrayList<Integer> extractMinute(String lineData)
    {
        Log.d(this.getClass().toString(), "extractMinute(" + lineData + ")");

        String[] splitted = lineData.split(":");

        int EXPECTED_SPLITTED_NUMBER = 2;
        if(EXPECTED_SPLITTED_NUMBER != splitted.length)
        {
            Log.e(this.getClass().toString(), "splitted.length is NOT 2 in extractMinute");
            return null;
        }

        String minuteString = splitted[1];
        Log.d(this.getClass().toString(), "minuteString: " + minuteString);

        String[] minuteSplitted = minuteString.split(" ");
        Log.d(this.getClass().toString(), "minuteSplitted.length: " + minuteSplitted.length);

        ArrayList<Integer> minuteArray = new ArrayList<Integer>();
        for(int i = 0; i < minuteSplitted.length; i++)
        {
            int minute = Integer.parseInt(minuteSplitted[i].replaceAll("[^0-9]", ""));
            String type = minuteSplitted[i].replaceAll("[^A-z]", "");

            Log.d(this.getClass().toString(), "minute: " + minute + "  type:" + type);

            minuteArray.add(minute);
        }

        return minuteArray;
    }

    private String getTableDataFile()
    {
        Log.d(this.getClass().toString(), "getTableDataFile");

        String filename = "";

        int currentDay = CalendarUtility.GetCurrentDay();
        int directionType = UserDataManager.GetDirectionType(this.mContext);

        Log.d(this.getClass().toString(), "currentDay:" + currentDay);

        Resources res = this.mContext.getResources();

        switch (currentDay)
        {
            case Calendar.SUNDAY:
                if(Constants.DIRECTION_TYPE_1 == directionType)
                {
                    filename = res.getString(R.string.file_timetable_direction1_holiday);
                }
                else
                {
                    filename = res.getString(R.string.file_timetable_direction2_holiday);
                }
                break;

            case Calendar.SATURDAY:
                if(Constants.DIRECTION_TYPE_1 == directionType)
                {
                    filename = res.getString(R.string.file_timetable_direction1_saturday);
                }
                else
                {
                    filename = res.getString(R.string.file_timetable_direction2_saturday);
                }
                break;

            case Calendar.MONDAY:
            case Calendar.TUESDAY:
            case Calendar.WEDNESDAY:
            case Calendar.THURSDAY:
            case Calendar.FRIDAY:
                if(Constants.DIRECTION_TYPE_1 == directionType)
                {
                    filename = res.getString(R.string.file_timetable_direction1_weekday);
                }
                else
                {
                    filename = res.getString(R.string.file_timetable_direction2_weekday);
                }
                break;

            default:
                break;
        }

        Log.d(this.getClass().toString(), "filename: " + filename);

        return  filename;
    }



}
