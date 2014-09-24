package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.util.Log;

/**
 * Created by Chieko on 9/7/14.
 */
public class TrainTimeTableManager
{
    private static TrainTimeTableManager instance = null;

    private Context mContext = null;
    private TrainTimeTableModel mTrainTimeTableModel = null;

    public static TrainTimeTableManager getInstance(Context context)
    {
        if(null == instance)
        {
            instance = new TrainTimeTableManager(context);
        }

        return instance;
    }

    private TrainTimeTableManager(Context context)
    {
        this.mContext = context;
        this.mTrainTimeTableModel = new TrainTimeTableModel(this.mContext);
    }

    public void updateTimetable()
    {
        Log.d(this.getClass().toString(), "updateTimetable");

        this.mTrainTimeTableModel = new TrainTimeTableModel(this.mContext);
    }
    /*
    This methods returns next train data without concerning user preferred time range
     */
    public TrainTimeData FindNextTrainData()
    {
        Log.d(this.getClass().toString(), "FindNextTrainData");

        TrainTimeData nextTrainTimeData = this.mTrainTimeTableModel.GetNextTrainTime();

        Log.d(this.getClass().toString(), "nextTrainTimeData(" + nextTrainTimeData.HourOfDay() + ":" + nextTrainTimeData.Minute() + ")");

        return nextTrainTimeData;
    }

}
