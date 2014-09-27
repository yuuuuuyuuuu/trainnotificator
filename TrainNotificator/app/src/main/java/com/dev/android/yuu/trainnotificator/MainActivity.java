package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements StationSettingFragment.OnStationChangedListener, TimeSettingFragment.TimeSettingChangeListener, DaySettingFragment.OnDayChangeListener, DirectionSettingFragment.OnDirectionChangeListener {

    private NotificationAlarmManager mNotificationAlarmManager = null;

    // fragments
    TrainInfoFragment mTrainInfoFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.mNotificationAlarmManager = NotificationAlarmManager.getInstance(this);

        this.initializeFragment();

        /*
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        */
    }

    private void initializeFragment()
    {
        Log.d(this.getClass().toString(), "initializeFragment");

        FragmentManager fm = this.getFragmentManager();

        this.mTrainInfoFragment = (TrainInfoFragment)fm.findFragmentById(R.id.traininfo_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStationChanged(int stationId)
    {
        Log.d(this.getClass().toString(), "onStationChanged(" + stationId + ")");

        FragmentManager fm = getFragmentManager();
        TrainInfoFragment fragment = (TrainInfoFragment)fm.findFragmentById(R.id.traininfo_fragment);
        fragment.updateTrainInfo();
    }

    @Override
    public void onStartTimeChanged(int hourOfDay, int minute)
    {
        Log.d(this.getClass().toString(), "onStartTimeChanged(" + hourOfDay + ", " + minute + ")");

        String hourString = String.valueOf(hourOfDay);
        String minuteString = String.valueOf(minute);
        if(minute < 10) minuteString = "0" + minuteString;
        String notificationMessage = "通知開始時刻を" +  hourString + ":" +minuteString + "に設定しました。";

        this.showToast(notificationMessage);
        this.update();
    }

    @Override
    public void onEndTimeChanged(int hourOfDay, int minute)
    {
        Log.d(this.getClass().toString(), "onEndTimeChanged(" + hourOfDay + ", " + minute + ")");

        String hourString = String.valueOf(hourOfDay);
        String minuteString = String.valueOf(minute);
        if(minute < 10) minuteString = "0" + minuteString;
        String notificationMessage = "通知終了時刻を" +  hourString + ":" +minuteString + "に設定しました。";

        this.showToast(notificationMessage);
        this.update();
    }

    @Override
    public void onDayChanged(int dayType)
    {
        Log.d(this.getClass().toString(), "onDayChanged(" + dayType + ")");

        Resources res = this.getResources();
        String displayDayName = "";
        switch (dayType)
        {
            case Constants.DATE_TYPE_WEEKDAY:
                displayDayName = res.getString(R.string.label_setting_date_weekday);
                break;

            case Constants.DATE_TYPE_WEEKEND:
                displayDayName = res.getString(R.string.label_setting_date_weekend);
                break;

            case Constants.DATE_TYPE_ALLDAY:
                displayDayName = res.getString(R.string.label_setting_date_allday);
                break;

            default:
                break;
        }

        String notificatioMessage = "通知日時を" + displayDayName + "に設定しました。";
        this.showToast(notificatioMessage);
        this.update();

    }

    @Override
    public void onDirectionChanged(int directionType)
    {
        Log.d(this.getClass().toString(), "onDirectionChanged(" + directionType + ")");

        String directionDisplayName = "";
        Resources res = this.getResources();

        switch (directionType)
        {
            case Constants.DIRECTION_TYPE_1:
                directionDisplayName = res.getString(R.string.name_direction1);
                break;

            case Constants.DIRECTION_TYPE_2:
                directionDisplayName = res.getString(R.string.name_direction2);
                break;

            default:
                break;
        }

        String notificationMessage = "方面を" + directionDisplayName + "に設定しました。";

        this.showToast(notificationMessage);
        this.update();
    }

    private void showToast(String msg)
    {
        Log.d(this.getClass().toString(), "showToast(" + msg + ")");

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void update()
    {
        Log.d(this.getClass().toString(), "update");

        // update display
        if(null == this.mTrainInfoFragment) this.initializeFragment();
        this.mTrainInfoFragment.updateTrainInfo();

        // update notification
        TrainTimeData nextTrainData = TrainTimeTableManager.getInstance(this).FindNextTrainData();
        if(null == this.mNotificationAlarmManager) this.mNotificationAlarmManager = NotificationAlarmManager.getInstance(this);
        this.mNotificationAlarmManager.SetNotification(this, nextTrainData.HourOfDay(), nextTrainData.Minute());
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
