package com.dev.android.yuu.trainnotificator.utility;

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
