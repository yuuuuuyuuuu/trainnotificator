package com.dev.android.yuu.trainnotificator.utility;

import android.util.Log;

/**
 * Created by Chieko on 9/6/14.
 */
public class TrainTimeData
{
    private int mHourOfDay = -1;
    private int mMinute = -1;
    private int mTrainType = 0;
    private String mDestination = "Not Set";

    public TrainTimeData()
    {
        Log.d(this.getClass().toString(), "Default constructor");
    }

    public TrainTimeData(int hourOfDay, int minute, int trainType, String destination)
    {
        Log.d(this.getClass().toString(), "Constructor with parameters");

        this.mHourOfDay = hourOfDay;
        this.mMinute = minute;
        this.mTrainType = trainType;
        this.mDestination = destination;
    }

    public void Set(int hourOfDay, int minute, int trainType, String destination)
    {
        Log.d(this.getClass().toString(), "Set");

        this.mHourOfDay = hourOfDay;
        this.mMinute = minute;
        this.mTrainType = trainType;
        this.mDestination = destination;
    }

    public int HourOfDay()
    {
        return this.mHourOfDay;
    }

    public int Minute()
    {
        return this.mMinute;
    }

    public int TrainType()
    {
        return this.mTrainType;
    }

    public String Destination()
    {
        return this.mDestination;
    }



}
