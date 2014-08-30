package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chieko on 8/30/14.
 */
public class SettingFragment extends Fragment {

    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        this.view = inflator.inflate(R.layout.fragment_setting, container, false);

        this.setUiEventHandlers();

        return this.view;
    }

    private void setUiEventHandlers()
    {

    }
}
