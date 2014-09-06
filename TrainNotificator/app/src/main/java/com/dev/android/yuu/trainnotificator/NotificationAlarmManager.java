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

    private NotificationManager mNotificationManager = null;

    public NotificationAlarmManager()
    {
        Log.d("NotificationAlarmManager", "default constructor");
    }

    public NotificationAlarmManager(Context context)
    {
        this.mContext = context;

        this.SetNextNotification(this.mContext, 10, 10);
        this.SetNextNotification(this.mContext, 10, 10);
    }

    public void SetNextNotification(Context context, int hourOfDay, int minute)
    {
        Intent intent = new Intent(context, NotificationAlarmManager.class);
        intent.setData(Uri.parse("custom//" + System.currentTimeMillis()));
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("onReceive", "Receiving broadcast intent");

        Random rand = new Random();

        this.mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i = new Intent(context, MainActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pIntent);
        builder.setTicker("test notification");
        builder.setContentTitle("test title");
        builder.setContentText("test content");
        builder.setSmallIcon(R.drawable.ic_action_alarms);
        builder.setAutoCancel(true);

        this.mNotificationManager.notify(rand.nextInt(100), builder.build());

        TrainTimeTableModel tTTM = new TrainTimeTableModel(context);
        tTTM.GetNextTrainTime();

    }


}
