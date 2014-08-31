package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.net.UnknownServiceException;

/**
 * Created by Chieko on 8/30/14.
 */
public class SettingFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View mView = null;

    private TimePickerDialog mTimePickerDialog = null;

    // Button
    private Button mButtonSetStartTime = null;
    private Button mButtonSetEndTime = null;
    private int mLastClickButtonId = -1;

    // Radio Button
    private RadioButton mRadioButtonWeekday = null;
    private RadioButton mRadioButtonWeekend = null;
    private RadioButton mRadioButtonAllday = null;

    // Date Type
    private final static int DATE_TYPE_WEEKDAY = 1;
    private final static int DATE_TYPE_WEEKEND = 2;
    private final static int DATE_TYPE_ALLDAY = 3;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        this.mView = inflator.inflate(R.layout.fragment_setting, container, false);

        this.setUiEventHandlers();

        this.setTimePickerDialog();

        this.setUserTime();

        return this.mView;
    }

    private void setUserTime()
    {
        int[] startTime = UserDataManager.GetStartTime(getActivity());
        int[] endTime = UserDataManager.GetEndTime(getActivity());

        // start
        int startHourOfDay = startTime[0];
        int startMinute = startTime[1];
        String startLabel = "7:00";
        if(-1 == startHourOfDay || -1 == startMinute)
        {
            Log.d("setUserTime", "setting default start time");
        }
        else
        {
            if(startMinute < 10)
            {
                startLabel = String.valueOf(startHourOfDay) + ":" + String.valueOf(startMinute) + "0";
            }
            else
            {
                startLabel = String.valueOf(startHourOfDay) + ":" + String.valueOf(startMinute);
            }

        }
        this.mButtonSetStartTime.setText(startLabel);

        // end
        int endHourOfDay = endTime[0];
        int endMinute = endTime[1];
        String endLabel = "9:00";
        if(-1 == endHourOfDay || -1 == endMinute)
        {
            Log.d("setUserTime", "setting default end time");
        }
        else
        {
            if(endMinute < 10)
            {
                endLabel = String.valueOf(endHourOfDay) + ":" + String.valueOf(endMinute) + "0";
            }
            else
            {
                endLabel = String.valueOf(endHourOfDay) + ":" + String.valueOf(endMinute);
            }

        }
        this.mButtonSetEndTime.setText(endLabel);

        // Date type
        int dateType = UserDataManager.GetDateType(getActivity());
        if(-1 == dateType)
        {
            Log.d("setUserTime", "setting default date type");
        }
        else
        {
            this.setDateTypeCheck(dateType);
        }

    }

    private void setDateTypeCheck(int dateType)
    {
        switch (dateType)
        {
            case SettingFragment.DATE_TYPE_WEEKDAY:
                this.mRadioButtonWeekday.setChecked(true);
                break;

            case SettingFragment.DATE_TYPE_WEEKEND:
                this.mRadioButtonWeekend.setChecked(true);
                break;

            case SettingFragment.DATE_TYPE_ALLDAY:
                this.mRadioButtonAllday.setChecked(true);
                break;

            default:
                break;

        }
    }

    private void setTimePickerDialog()
    {
        this.mTimePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar, this, 7, 0, true);
    }

    private void setUiEventHandlers()
    {
        // Time setting buttons
        this.mButtonSetStartTime = (Button)this.mView.findViewById(R.id.button_setting_start_time);
        this.mButtonSetStartTime.setOnClickListener(this);

        this.mButtonSetEndTime = (Button)this.mView.findViewById(R.id.button_setting_end_time);
        this.mButtonSetEndTime.setOnClickListener(this);

        // Date type setting radio buttons
        this.mRadioButtonWeekday = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_weekday);
        this.mRadioButtonWeekday.setOnCheckedChangeListener(this);

        this.mRadioButtonWeekend = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_weekend);
        this.mRadioButtonWeekend.setOnCheckedChangeListener(this);

        this.mRadioButtonAllday = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_allday);
        this.mRadioButtonAllday.setOnCheckedChangeListener(this);
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

        UserDataManager.SaveStartTime(hourOfDay, minute, getActivity());

        String label = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        this.mButtonSetStartTime.setText(label);
    }

    private void setEndTime(int hourOfDay, int minute)
    {
        Log.d("setEndTime", "Setting end time " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        UserDataManager.SaveEndTime(hourOfDay, minute, getActivity());

        String label = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        this.mButtonSetEndTime.setText(label);
    }

    private void setDateType(int dateType)
    {
        switch (dateType)
        {
            case SettingFragment.DATE_TYPE_WEEKDAY:
                UserDataManager.SaveDateType(SettingFragment.DATE_TYPE_WEEKDAY, getActivity());
                break;

            case SettingFragment.DATE_TYPE_WEEKEND:
                UserDataManager.SaveDateType(SettingFragment.DATE_TYPE_WEEKEND, getActivity());
                break;

            case SettingFragment.DATE_TYPE_ALLDAY:
                UserDataManager.SaveDateType(SettingFragment.DATE_TYPE_ALLDAY, getActivity());
                break;

            default:
                break;
        }
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
    {
        int radioButtonId = compoundButton.getId();
        Log.d("onCHeckedCHanged RadioButtonId: ", String.valueOf(radioButtonId));

        if(!isChecked) return;

        switch (radioButtonId)
        {
            case R.id.radiobutton_setting_weekday:
                this.setDateType(SettingFragment.DATE_TYPE_WEEKDAY);
                break;

            case R.id.radiobutton_setting_weekend:
                this.setDateType(SettingFragment.DATE_TYPE_WEEKEND);
                break;

            case R.id.radiobutton_setting_allday:
                this.setDateType(SettingFragment.DATE_TYPE_ALLDAY);
                break;

            default:
                break;
        }


    }
}
