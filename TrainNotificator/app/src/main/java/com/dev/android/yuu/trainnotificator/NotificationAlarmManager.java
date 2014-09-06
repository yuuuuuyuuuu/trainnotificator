package com.dev.android.yuu.trainnotificator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dev.android.yuu.trainnotificator.utility.CalendarUtility;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Chieko on 8/31/14.
 */
public class NotificationAlarmManager extends BroadcastReceiver
{
    private Context mContext = null;

    private static final String TRAIN_NOTIFICATOR_ACTION_ALARM = "train_notificator_action_alarm";
    private static final int PENDING_INTENT_REQUEST_CODE = 10;

    private static final String EXTRA_KEY_HOUR_OF_DAY = "extra_key_hour_of_day";
    private static final String EXTRA_KEY_MINUTE = "extra_key_minute";

    private NotificationManager mNotificationManager = null;

    private TrainTimeTableModel mTrainTimeTableModel = null;

    public NotificationAlarmManager()
    {
        Log.d(this.getClass().toString(), "default constructor");


    }

    public NotificationAlarmManager(Context context)
    {
        this.mContext = context;

        this.mTrainTimeTableModel = new TrainTimeTableModel(this.mContext);

        TrainTimeData nextTrainData = this.mTrainTimeTableModel.GetNextTrainTime();

        this.SetNotification(this.mContext, nextTrainData.HourOfDay(), nextTrainData.Minute());
        // this.SetNotification(this.mContext, 10, 10);
    }

    public void SetNotification(Context context, int hourOfDay, int minute)
    {
        Log.d(this.getClass().toString(), "SetNextNotification(" + hourOfDay + "," + minute);

        Intent intent = new Intent(context, NotificationAlarmManager.class);
        intent.setData(Uri.parse("custom//" + System.currentTimeMillis()));
        intent.putExtra(EXTRA_KEY_HOUR_OF_DAY, hourOfDay);
        intent.putExtra(EXTRA_KEY_MINUTE, minute);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        //calendar.add(Calendar.SECOND, 5);
        calendar.setTimeInMillis(System.currentTimeMillis());

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("onReceive", "Receiving broadcast intent");

        int hourOfDay = intent.getExtras().getInt(EXTRA_KEY_HOUR_OF_DAY);
        int minute = intent.getExtras().getInt(EXTRA_KEY_MINUTE);
        Log.d(this.getClass().toString(), "hourOfDay: " + hourOfDay + "  minute:" + minute);

        Random rand = new Random();

        this.mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i = new Intent(context, MainActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pIntent);
        builder.setTicker("Next train is at " + hourOfDay + ":" + minute);
        builder.setContentTitle("Next train information");
        builder.setContentText("Next train is at " + hourOfDay + ":" + minute);
        builder.setSmallIcon(R.drawable.ic_action_alarms);
        builder.setAutoCancel(true);

        this.mNotificationManager.notify(rand.nextInt(100), builder.build());

        TrainTimeTableModel timeTableModel = new TrainTimeTableModel(context);
        TrainTimeData nextTrainData = timeTableModel.GetNextTrainTime();

        if(null == nextTrainData)
        {
            Log.e(this.getClass().toString(), "Could NOT find next train data");
            return;
        }

        this.SetNotification(context, nextTrainData.HourOfDay(), nextTrainData.Minute());

    }


}
