package com.dev.android.yuu.trainnotificator;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dev.android.yuu.trainnotificator.utility.CalendarUtility;

import java.net.UnknownServiceException;

/**
 * Created by Chieko on 8/30/14.
 */
public class SettingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private View mView = null;

    private RadioButton mRadioButtonDirection1 = null;
    private RadioButton mRadioButtonDirection2 = null;

    private boolean mIsCreateViewCompleted = false;

    private TrainTimeTableManager mTrainTimeTableManager = null;
    private NotificationAlarmManager mNotificationAlarmManager = null;

    public interface OnTrainInfoUpdatedListener
    {
        public void onTrainInfoUpdated();
        public void onSettingUpdated();
    }

    private OnTrainInfoUpdatedListener mCallback = null;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        this.mView = inflator.inflate(R.layout.fragment_setting, container, false);

        this.setUiEventHandlers();

        this.setUserTime();

        this.mIsCreateViewCompleted = true;

        return this.mView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            this.mCallback = (OnTrainInfoUpdatedListener)activity;
        }
        catch (Exception e)
        {
            throw new ClassCastException(activity.toString() + " cannot be casted to OnTrainInfoUpdatedListener");
        }
    }

    private void setUserTime()
    {
        Log.d(this.getClass().toString(), "setUserTime");

        // Direction
        int directionType = UserDataManager.GetDirectionType(getActivity());
        if(-1 == directionType)
        {
            Log.d("setUserTime", "setting default direction type");
            directionType = Constants.DIRECTION_TYPE_1;
        }

        this.setDirectionType(directionType);

        this.launchNotification();
    }

    private void setUiEventHandlers()
    {
        this.mRadioButtonDirection1 = (RadioButton)this.mView.findViewById(R.id.radioButton_direction1);
        this.mRadioButtonDirection1.setOnCheckedChangeListener(this);

        this.mRadioButtonDirection2 = (RadioButton)this.mView.findViewById(R.id.radioButton_direction2);
        this.mRadioButtonDirection2.setOnCheckedChangeListener(this);
    }

    private void setDirectionType(int directionType)
    {
        Resources res = getResources();

        switch (directionType)
        {
            case Constants.DIRECTION_TYPE_1:
                UserDataManager.SaveDirectionType(Constants.DIRECTION_TYPE_1, getActivity());

                if(this.mIsCreateViewCompleted) this.showToast("方面が " + res.getString(R.string.name_direction1) +  " 方面に設定されました");
                this.setSelectedStyle(this.mRadioButtonDirection1);
                if(!this.mIsCreateViewCompleted) this.mRadioButtonDirection1.setChecked(true);
                break;

            case Constants.DIRECTION_TYPE_2:
                UserDataManager.SaveDirectionType(Constants.DIRECTION_TYPE_2, getActivity());

                if(this.mIsCreateViewCompleted) this.showToast("方面が " + res.getString(R.string.name_direction2) +  " 方面に設定されました");
                this.setSelectedStyle(this.mRadioButtonDirection2);
                if(!this.mIsCreateViewCompleted) this.mRadioButtonDirection2.setChecked(true);
                break;

            default:
                break;
        }
        if(null != this.mTrainTimeTableManager) this.mTrainTimeTableManager.updateTimetable();
        this.updateNextNotification(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
    {
        int radioButtonId = compoundButton.getId();
        Log.d("onCheckedChanged RadioButtonId: ", String.valueOf(radioButtonId));

        // style
        if(isChecked)
        {
            this.setSelectedStyle((RadioButton)compoundButton);
        }
        else
        {
            this.setUnselectedStyle((RadioButton)compoundButton);
        }

        if(!isChecked) return;

        switch (radioButtonId)
        {
            case R.id.radioButton_direction1:
                this.setDirectionType(Constants.DIRECTION_TYPE_1);

                break;

            case R.id.radioButton_direction2:
                this.setDirectionType(Constants.DIRECTION_TYPE_2);

                break;

            default:
                break;
        }

        this.launchNotification();
    }

    private void setSelectedStyle(RadioButton radioButton)
    {
        Log.d(this.getClass().toString(), "setSelectedStyle");

        int id = radioButton.getId();

        if(R.id.radiobutton_setting_weekday == id
                || R.id.radiobutton_setting_weekend == id
                || R.id.radiobutton_setting_allday == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_green_button);
        }
        else if(R.id.radioButton_direction1 == id || R.id.radioButton_direction2 == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_orange_button);
        }
        else
        {
            Log.e(this.getClass().toString(), "Unexpected radio button");
        }
    }

    private void setUnselectedStyle(RadioButton radioButton)
    {
        Log.d(this.getClass().toString(), "setUnselectedStyle");

        int id = radioButton.getId();

        if(R.id.radiobutton_setting_weekday == id
                || R.id.radiobutton_setting_weekend == id
                || R.id.radiobutton_setting_allday == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_button_pale_green);
        }
        else if(R.id.radioButton_direction1 == id || R.id.radioButton_direction2 == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_button_pale_orange);
        }
        else
        {
            Log.e(this.getClass().toString(), "Unexpected radio button");
        }
    }

    private void updateNextNotification(boolean needForceUpdate)
    {
        Log.d(this.getClass().toString(), "updateNextNotification(" + needForceUpdate + ")");

        if(needForceUpdate || null == this.mTrainTimeTableManager)
        {
            this.mTrainTimeTableManager = TrainTimeTableManager.getInstance(this.getActivity());
            this.mTrainTimeTableManager.updateTimetable();
        }

        TrainTimeData nextTrainData = this.mTrainTimeTableManager.FindNextTrainData();

        if(null == nextTrainData)
        {
            Log.d(this.getClass().toString(), "next train data not found");
            return;
        }

        if(null == this.mNotificationAlarmManager) this.mNotificationAlarmManager = NotificationAlarmManager.getInstance((this.getActivity()));

        this.mNotificationAlarmManager.SetNotification(this.getActivity(), nextTrainData.HourOfDay(), nextTrainData.Minute());

        this.mCallback.onTrainInfoUpdated();
    }

    private void launchNotification()
    {
        Log.d(this.getClass().toString(), "launchNotification");

        boolean isNowInUserPreference = CalendarUtility.IsNowInUserPreferenceTime(this.getActivity());
        if(!isNowInUserPreference)
        {
            Log.d(this.getClass().toString(), "It is NOT time to launch notification.");
            return;
        }

        if(null == this.mTrainTimeTableManager)
        {
            this.mTrainTimeTableManager = TrainTimeTableManager.getInstance(this.getActivity());
        }

        TrainTimeData nextTrainData = this.mTrainTimeTableManager.FindNextTrainData();

        if(null == nextTrainData)
        {
            Log.d(this.getClass().toString(), "next train data not found");
            return;
        }

        if(null == this.mNotificationAlarmManager) this.mNotificationAlarmManager = NotificationAlarmManager.getInstance((this.getActivity()));
        this.mNotificationAlarmManager.SetNotification(this.getActivity(), nextTrainData.HourOfDay(), nextTrainData.Minute());
    }

    private void showToast(String message)
    {
        Log.d(this.getClass().toString(), "showToast(" + message + "");

        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
