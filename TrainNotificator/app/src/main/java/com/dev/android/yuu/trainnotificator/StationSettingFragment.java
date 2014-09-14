package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chieko on 9/14/14.
 */
public class StationSettingFragment extends Fragment
{
    private View mView = null;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(this.getClass().toString(), "onCreateView");

        this.mView = inflator.inflate(R.layout.fragment_station_setting, container, false);

        return this.mView;
    }
}
