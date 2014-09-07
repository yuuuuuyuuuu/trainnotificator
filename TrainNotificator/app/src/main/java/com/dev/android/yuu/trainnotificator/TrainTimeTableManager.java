package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.util.Log;

/**
 * Created by Chieko on 9/7/14.
 */
public class TrainTimeTableManager
{

    private Context mContext = null;
    private TrainTimeTableModel mTrainTimeTableModel = null;

    public TrainTimeTableManager(Context context)
    {
        this.mContext = context;
        this.mTrainTimeTableModel = new TrainTimeTableModel(this.mContext);
    }

    /*
    This method returns next train data which filtered with user preffered date and time
     */
    public TrainTimeData FindNextTrainDataWithUserPreference()
    {
        Log.d(this.getClass().toString(), "FindNextTrainDataWithUserPreference");

        int[] startTime = UserDataManager.GetStartTime(this.mContext);
        int[] endTime = UserDataManager.GetEndTime(this.mContext);

        int userStartTimeHourOfDay = startTime[0];
        int userStartTimeMinute = startTime[1];
        int userEndTimeHourOfDay = endTime[0];
        int userEndTimeMinute = endTime[1];
        Log.d(this.getClass().toString(), "UserPreferenceTime: start(" + userStartTimeHourOfDay + ":" + userStartTimeMinute + ") end(" + userEndTimeHourOfDay + ":" + userEndTimeMinute + ")");

        TrainTimeData nextTrainTimeData = null;
        boolean isNextTrainTimeDataFound = false;

        int timeDataNum = this.mTrainTimeTableModel.GetTrainTimeDataNumber();
        int loopCounter = 0;

        while(!isNextTrainTimeDataFound)
        {
            loopCounter++;

            TrainTimeData timeDataCandidate = this.mTrainTimeTableModel.GetNextTrainTime();
            boolean isStartTimeWithinRange = false;
            boolean isEndTimeWithinRange = false;

            // check start time
            if(timeDataCandidate.HourOfDay() < userStartTimeHourOfDay) continue;

            if(userStartTimeHourOfDay == timeDataCandidate.HourOfDay())
            {
                if(timeDataCandidate.Minute() <= userStartTimeMinute ) continue;

                isStartTimeWithinRange = true;
            }
            else
            {
                isStartTimeWithinRange = true;
            }

            if(!isStartTimeWithinRange) continue;

            // check end time
            if(userEndTimeHourOfDay < timeDataCandidate.HourOfDay()) continue;

            if(userEndTimeHourOfDay == timeDataCandidate.HourOfDay())
            {
                if(userEndTimeMinute <= timeDataCandidate.Minute()) continue;

                nextTrainTimeData = timeDataCandidate;
                isEndTimeWithinRange = true;
            }
            else
            {
                isEndTimeWithinRange = true;
            }

            if(isStartTimeWithinRange && isEndTimeWithinRange)
            {
                Log.d(this.getClass().toString(), "nextTrainTimeData found (" + timeDataCandidate.HourOfDay() + ":" + timeDataCandidate.Minute() + ")");

                nextTrainTimeData = timeDataCandidate;
                isNextTrainTimeDataFound = true;
                break;
            }

            if(timeDataNum <= loopCounter)
            {
                Log.d(this.getClass().toString(), "search all data -> break loop");
                break;
            }
        }

        if(null == nextTrainTimeData)
        {
            Log.d(this.getClass().toString(), "nextTrainTimeData is NOT found.");
        }

        return nextTrainTimeData;
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
