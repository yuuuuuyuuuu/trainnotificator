package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements SettingFragment.OnTrainInfoUpdatedListener, StationSettingFragment.OnStationChangedListener, TimeSettingFragment.TimeSettingChangeListener {

    private NotificationAlarmManager mNotificationAlarmManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // this.mNotificationAlarmManager = new NotificationAlarmManager(this);
        this.mNotificationAlarmManager = NotificationAlarmManager.getInstance(this);

        /*
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        */
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
    public void onTrainInfoUpdated()
    {
        Log.d(this.getClass().toString(), "onTrainInfoUpdated(");

        FragmentManager fm = getFragmentManager();
        TrainInfoFragment fragment = (TrainInfoFragment)fm.findFragmentById(R.id.traininfo_fragment);
        fragment.updateTrainInfo();
    }

    @Override
    public void onSettingUpdated()
    {
        Log.d(this.getClass().toString(), "onSettingUpdated");
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
    }

    private void showToast(String msg)
    {
        Log.d(this.getClass().toString(), "showToast(" + msg + ")");

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
