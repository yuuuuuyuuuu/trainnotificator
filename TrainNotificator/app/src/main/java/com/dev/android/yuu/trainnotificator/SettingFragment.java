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

        this.setTimePickerDialog();

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
            //dateType = this.DATE_TYPE_WEEKDAY;
            dateType = Constants.DATE_TYPE_WEEKDAY;
        }

        this.setDateTypeCheck(dateType);

        // Direction
        int directionType = UserDataManager.GetDirectionType(getActivity());
        if(-1 == directionType)
        {
            Log.d("setUserTime", "setting default direction type");
            // directionType = this.DIRECTION_TYPE_1;
            directionType = Constants.DIRECTION_TYPE_1;
        }

        this.setDirectionType(directionType);

        this.launchNotification();
    }

    private void setDateTypeCheck(int dateType)
    {
        Log.d(this.getClass().toString(), "setDateTypeCheck(" + dateType + ")");

        switch (dateType)
        {
            // case SettingFragment.DATE_TYPE_WEEKDAY:
            case Constants.DATE_TYPE_WEEKDAY:
                this.mRadioButtonWeekday.setChecked(true);
                if(!this.mIsCreateViewCompleted)
                {
                    this.setSelectedStyle(this.mRadioButtonWeekday);
                }
                break;

            // case SettingFragment.DATE_TYPE_WEEKEND:
            case Constants.DATE_TYPE_WEEKEND:
                this.mRadioButtonWeekend.setChecked(true);
                if(!this.mIsCreateViewCompleted)
                {
                    this.setSelectedStyle(this.mRadioButtonWeekend);
                }
                break;

            // case SettingFragment.DATE_TYPE_ALLDAY:
            case Constants.DATE_TYPE_ALLDAY:
                this.mRadioButtonAllday.setChecked(true);
                if(!this.mIsCreateViewCompleted)
                {
                    this.setSelectedStyle(this.mRadioButtonAllday);
                }
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
        //this.mButtonSetStartTime.setOnClickListener(this);

        this.mButtonSetEndTime = (Button)this.mView.findViewById(R.id.button_setting_end_time);
        //this.mButtonSetEndTime.setOnClickListener(this);

        // Date type setting radio buttons
        this.mRadioButtonWeekday = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_weekday);
        this.mRadioButtonWeekday.setOnCheckedChangeListener(this);

        this.mRadioButtonWeekend = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_weekend);
        this.mRadioButtonWeekend.setOnCheckedChangeListener(this);

        this.mRadioButtonAllday = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_allday);
        this.mRadioButtonAllday.setOnCheckedChangeListener(this);

        this.mRadioButtonDirection1 = (RadioButton)this.mView.findViewById(R.id.radioButton_direction1);
        this.mRadioButtonDirection1.setOnCheckedChangeListener(this);

        this.mRadioButtonDirection2 = (RadioButton)this.mView.findViewById(R.id.radioButton_direction2);
        this.mRadioButtonDirection2.setOnCheckedChangeListener(this);
    }

    private void showTimePickerDialog(String title, int hourOfDay, int minute)
    {
        if(null == this.mTimePickerDialog)
        {
            Log.e("showTimePickerDialog", "this.mTimePickerDialog is null");
        }

        this.mTimePickerDialog.updateTime(hourOfDay, minute);
        this.mTimePickerDialog.setTitle(title);
        this.mTimePickerDialog.show();
    }

    private void setStartTime(int hourOfDay, int minute)
    {
        Log.d("setStartTime", "Setting start time " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        UserDataManager.SaveStartTime(hourOfDay, minute, getActivity());

        String minuteString = String.valueOf(minute);
        if(minute < 10) minuteString = "0" + minuteString;

        String label = String.valueOf(hourOfDay) + ":" + minuteString;
        this.mButtonSetStartTime.setText(label);

        this.updateNextNotification(false);

        this.showToast("通知開始時刻が " + hourOfDay + ":" + minuteString + " に設定されました");
    }

    private void setEndTime(int hourOfDay, int minute)
    {
        Log.d("setEndTime", "Setting end time " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        UserDataManager.SaveEndTime(hourOfDay, minute, getActivity());

        String minuteString = String.valueOf(minute);
        if(minute < 10) minuteString = "0" + minuteString;

        String label = String.valueOf(hourOfDay) + ":" + minuteString;
        this.mButtonSetEndTime.setText(label);

        this.updateNextNotification(false);

        this.showToast("通知終了時刻が " + hourOfDay + ":" + minuteString + " に設定されました");
    }

    private void setDateType(int dateType)
    {
        switch (dateType)
        {
            // case SettingFragment.DATE_TYPE_WEEKDAY:
            case Constants.DATE_TYPE_WEEKDAY:
                // UserDataManager.SaveDateType(SettingFragment.DATE_TYPE_WEEKDAY, getActivity());
                UserDataManager.SaveDateType(Constants.DATE_TYPE_WEEKDAY, getActivity());

                if(this.mIsCreateViewCompleted) this.showToast("通知日が " + "平日のみ" +  " に設定されました");
                break;

            // case SettingFragment.DATE_TYPE_WEEKEND:
            case Constants.DATE_TYPE_WEEKEND:
                // UserDataManager.SaveDateType(SettingFragment.DATE_TYPE_WEEKEND, getActivity());
                UserDataManager.SaveDateType(Constants.DATE_TYPE_WEEKEND, getActivity());
                if(this.mIsCreateViewCompleted) this.showToast("通知日が " + "土日のみ" +  " に設定されました");
                break;

            // case SettingFragment.DATE_TYPE_ALLDAY:
            case Constants.DATE_TYPE_ALLDAY:
                // UserDataManager.SaveDateType(SettingFragment.DATE_TYPE_ALLDAY, getActivity());
                UserDataManager.SaveDateType(Constants.DATE_TYPE_ALLDAY, getActivity());

                if(this.mIsCreateViewCompleted) this.showToast("通知日が " + "毎日" +  " に設定されました");
                break;

            default:
                break;
        }

        this.updateNextNotification(true);
    }

    private void setDirectionType(int directionType)
    {
        Resources res = getResources();

        switch (directionType)
        {
            // case SettingFragment.DIRECTION_TYPE_1:
            case Constants.DIRECTION_TYPE_1:
                //UserDataManager.SaveDirectionType(SettingFragment.DIRECTION_TYPE_1, getActivity());
                UserDataManager.SaveDirectionType(Constants.DIRECTION_TYPE_1, getActivity());

                if(this.mIsCreateViewCompleted) this.showToast("方面が " + res.getString(R.string.name_direction1) +  " 方面に設定されました");
                this.setSelectedStyle(this.mRadioButtonDirection1);
                if(!this.mIsCreateViewCompleted) this.mRadioButtonDirection1.setChecked(true);
                break;

            // case SettingFragment.DIRECTION_TYPE_2:
            case Constants.DIRECTION_TYPE_2:
                // UserDataManager.SaveDirectionType(SettingFragment.DIRECTION_TYPE_2, getActivity());
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
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
    {
        Log.d("onTimeSet", "Set to " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        boolean isTimeChanged = false;

        switch (this.mLastClickButtonId)
        {
            case R.id.button_setting_start_time:
                this.setStartTime(hourOfDay, minute);
                isTimeChanged = true;
                break;

            case R.id.button_setting_end_time:
                this.setEndTime(hourOfDay, minute);
                isTimeChanged = true;
                break;

            default:
                this.mLastClickButtonId = -1;
                break;
        }

        if(isTimeChanged)
        {
            Log.d(this.getClass().toString(), "Launch new notification since user time changed.");
            this.launchNotification();
        }
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        int[] startTime = UserDataManager.GetStartTime(getActivity());
        int[] endTime = UserDataManager.GetEndTime(getActivity());

        Resources res = getResources();

        switch (viewId)
        {
            case R.id.button_setting_start_time:
                this.setLastClickButtonId(viewId);

                this.showTimePickerDialog(res.getString(R.string.label_setting_starttime_title), startTime[0], startTime[1]);
                break;

            case R.id.button_setting_end_time:
                this.setLastClickButtonId(viewId);
                this.showTimePickerDialog(res.getString(R.string.label_setting_endtime_title), endTime[0], endTime[1]);
                break;

            default:
                break;
        }
    }

    /*
    Remember which button was pressed to show time picker dialog
     */
    private void setLastClickButtonId(int buttonId)
    {
        this.mLastClickButtonId = buttonId;
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
            case R.id.radiobutton_setting_weekday:

                // this.setDateType(SettingFragment.DATE_TYPE_WEEKDAY);
                this.setDateType(Constants.DATE_TYPE_WEEKDAY);

                break;

            case R.id.radiobutton_setting_weekend:
                // this.setDateType(SettingFragment.DATE_TYPE_WEEKEND);
                this.setDateType(Constants.DATE_TYPE_WEEKEND);

                break;

            case R.id.radiobutton_setting_allday:
                // this.setDateType(SettingFragment.DATE_TYPE_ALLDAY);
                this.setDateType(Constants.DATE_TYPE_ALLDAY);

                break;

            case R.id.radioButton_direction1:
                // this.setDirectionType(SettingFragment.DIRECTION_TYPE_1);
                this.setDirectionType(Constants.DIRECTION_TYPE_1);

                break;

            case R.id.radioButton_direction2:
                // this.setDirectionType(SettingFragment.DIRECTION_TYPE_2);
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

        // TrainTimeData nextTrainData = this.mTrainTimeTableManager.FindNextTrainDataWithUserPreference();
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
