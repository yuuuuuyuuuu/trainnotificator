package com.dev.android.yuu.trainnotificator;

import android.util.Log;

/**
 * Created by Chieko on 9/15/14.
 */
public class StationData
{
    private String mName = "";
    private int mId = -1;

    public StationData(String name, int id)
    {
        Log.d(this.getClass().toString(), "Constructor name:" + name + " id:" + id);

        this.mName = name;
        this.mId = id;
    }

    public String Name()
    {
        Log.d(this.getClass().toString(), "Name: " + this.mName);

        return this.mName;
    }

    public int Id()
    {
        Log.d(this.getClass().toString(), "Id: " + this.mId);

        return this.mId;
    }

}
