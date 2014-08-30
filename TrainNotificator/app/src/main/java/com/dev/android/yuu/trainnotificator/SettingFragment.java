package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by Chieko on 8/30/14.
 */
public class SettingFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private View mView = null;

    private TimePickerDialog mTimePickerDialog = null;

    private Button mButtonSetStartTime = null;
    private Button mButtonSetEndTime = null;
    private int mLastClickButtonId = -1;


    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        this.mView = inflator.inflate(R.layout.fragment_setting, container, false);

        this.setUiEventHandlers();

        this.setTimePickerDialog();

        return this.mView;
    }

    private void setTimePickerDialog()
    {
        this.mTimePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar, this, 7, 0, true);
    }

    private void setUiEventHandlers()
    {
        this.mButtonSetStartTime = (Button)this.mView.findViewById(R.id.button_setting_start_time);
        this.mButtonSetStartTime.setOnClickListener(this);

        this.mButtonSetEndTime = (Button)this.mView.findViewById(R.id.button_setting_end_time);
        this.mButtonSetEndTime.setOnClickListener(this);
    }

    private void showTimePickerDialog(String title, int originButtonId)
    {
        if(null == this.mTimePickerDialog)
        {
            Log.e("showTimePickerDialog", "this.mTimePickerDialog is null");
        }

        this.mTimePickerDialog.setTitle(title);
        this.mTimePickerDialog.show();
    }

    private void setStartTime(int hourOfDay, int minute)
    {
        Log.d("setStartTime", "Setting start time " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        String label = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        this.mButtonSetStartTime.setText(label);
    }

    private void setEndTime(int hourOfDay, int minute)
    {
        Log.d("setEndTime", "Setting end time " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        String label = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        this.mButtonSetEndTime.setText(label);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
    {
        Log.d("onTimeSet", "Set to " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        switch (this.mLastClickButtonId)
        {
            case R.id.button_setting_start_time:
                this.setStartTime(hourOfDay, minute);
                break;

            case R.id.button_setting_end_time:
                this.setEndTime(hourOfDay, minute);
                break;

            default:
                this.mLastClickButtonId = -1;
                break;
        }
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        switch (viewId)
        {
            case R.id.button_setting_start_time:
                this.setLastClickButtonId(viewId);
                this.showTimePickerDialog("Start Time", R.id.button_setting_start_time);
                break;

            case R.id.button_setting_end_time:
                this.setLastClickButtonId(viewId);
                this.showTimePickerDialog("Start Time", R.id.button_setting_end_time);
                break;

            default:
                break;
        }

    }

    private void setLastClickButtonId(int buttonId)
    {
        this.mLastClickButtonId = buttonId;
    }

}
