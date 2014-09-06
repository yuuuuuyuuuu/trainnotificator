package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

            Log.d(TrainTimeTableModel.TAG, "line: " + line);
        }

    }

}
