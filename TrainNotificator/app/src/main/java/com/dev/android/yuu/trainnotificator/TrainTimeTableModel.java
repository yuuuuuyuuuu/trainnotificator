package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Chieko on 8/31/14.
 */
public class TrainTimeTableModel
{
    private Context mContext = null;

    private static final String TAG = "TrainTimeTableModel";

    public  TrainTimeTableModel(Context context)
    {
        Log.d(TrainTimeTableModel.TAG, "TrainTimeTableModel()");

        this.mContext = context;

        this.loadTimeTable();
    }

    public int NetTrainHourOfDay()
    {
        Log.d(TrainTimeTableModel.TAG, "NetTrainHourOfDay()");

        return 0;
    }

    public int NextTrainMinute()
    {
        Log.d(TrainTimeTableModel.TAG, "NextTrainMinute()");

        return 0;
    }

    private void loadTimeTable()
    {
        Log.d(TrainTimeTableModel.TAG, "loadTimeTable()");

        AssetManager am = this.mContext.getAssets();

        InputStream is = null;

        try
        {
            is = am.open("timetable_shonanshinjukuline.txt");
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

            }

            Log.d(TrainTimeTableModel.TAG, "line: " + line);
        }

    }

    private int extractHourOfDay(String lineData)
    {
        Log.d(TrainTimeTableModel.TAG, "extractHourOfDay(" + lineData + ")");

        int hourOfDay = -1;

        int EXPECTED_SPLITTED_NUMBER = 2;
        String[] splitted = lineData.split(":");

        if(EXPECTED_SPLITTED_NUMBER != splitted.length)
        {
            Log.e(this.getClass().toString(), "splitted.length is NOT 2 in extractHourOfDay");
            return -1;
        }

        hourOfDay = Integer.parseInt(splitted[0]);
        Log.d(TrainTimeTableModel.TAG, "extracted hourOfDay: " + hourOfDay);

        return hourOfDay;
    }

    private ArrayList<Integer> extractMinute(String lineData)
    {
        Log.d(TrainTimeTableModel.TAG, "extractMinute(" + lineData + ")");

        String[] splitted = lineData.split(":");

        int EXPECTED_SPLITTED_NUMBER = 2;
        if(EXPECTED_SPLITTED_NUMBER != splitted.length)
        {
            Log.e(this.getClass().toString(), "splitted.length is NOT 2 in extractMinute");
            return null;
        }

        String minuteString = splitted[1];
        Log.d(TrainTimeTableModel.TAG, "minuteString: " + minuteString);

        String[] minuteSplitted = minuteString.split(" ");
        Log.d(TrainTimeTableModel.TAG, "minuteSplitted.length: " + minuteSplitted.length);

        ArrayList<Integer> minuteArray = new ArrayList<Integer>();
        for(int i = 0; i < minuteSplitted.length; i++)
        {
            int minute = Integer.parseInt(minuteSplitted[i].replaceAll("[^0-9]", ""));
            String type = minuteSplitted[i].replaceAll("[^A-z]", "");

            Log.d(TrainTimeTableModel.TAG, "minute: " + minute + "  type:" + type);

            minuteArray.add(minute);
        }

        return minuteArray;
    }


}
