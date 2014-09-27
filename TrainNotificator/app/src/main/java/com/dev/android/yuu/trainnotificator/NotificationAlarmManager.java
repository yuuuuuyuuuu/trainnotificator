package com.dev.android.yuu.trainnotificator;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dev.android.yuu.trainnotificator.utility.CalendarUtility;
import com.dev.android.yuu.trainnotificator.utility.TrainTimeTableUtility;

import java.util.Calendar;

/**
 * Created by Chieko on 8/31/14.
 */
public class NotificationAlarmManager extends BroadcastReceiver
{
    private Context mContext = null;

    private static final String TRAIN_NOTIFICATOR_ACTION_ALARM = "train_notificator_action_alarm";
    private static final int PENDING_INTENT_REQUEST_CODE = 99999;

    private NotificationManager mNotificationManager = null;

    private TrainTimeTableModel mTrainTimeTableModel = null;

    private long mLastNotificationLaunchTime = 0;

    public NotificationAlarmManager()
    {
        Log.d(this.getClass().toString(), "default constructor");
    }

    private static NotificationAlarmManager instance = null;

    public static NotificationAlarmManager getInstance(Context context)
    {
        if(null == instance) instance = new NotificationAlarmManager(context);

        return instance;
    }

    private NotificationAlarmManager(Context context)
    {
        this.mContext = context;

        this.mTrainTimeTableModel = new TrainTimeTableModel(this.mContext);

    }

    public void SetNotification(Context context, int hourOfDay, int minute)
    {
        Log.d(this.getClass().toString(), "SetNextNotification(" + hourOfDay + "," + minute);

        // Setting next notification time
        Calendar calendar = Calendar.getInstance();
        Log.d(this.getClass().toString(), calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DATE) +"  " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get((Calendar.MINUTE)));

        boolean needToSetForTomorrow = false;
        int currentHourOfDay = CalendarUtility.GetCurrentHourOfDay();
        int currentMinute = CalendarUtility.GetCurrentMinute();

        if(hourOfDay < currentHourOfDay)
        {
            needToSetForTomorrow = true;
        }
        else if(hourOfDay == currentHourOfDay && minute < currentMinute)
        {
            needToSetForTomorrow = true;
        }
        else
        {
            // no need to set notification for tomorrow
        }

        if(needToSetForTomorrow) calendar.add(Calendar.DATE, 1);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        Log.d(this.getClass().toString(), calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DATE) +"  " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get((Calendar.MINUTE)));

        calendar.set(Calendar.MINUTE, minute);
        Log.d(this.getClass().toString(), calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DATE) +"  " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get((Calendar.MINUTE)));

        calendar.set(Calendar.SECOND, 0);

        // Setting train time data
        Intent intent = new Intent(context, NotificationAlarmManager.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

        if(UserDataManager.IsNowInUserPreferableTime(this.mContext))
        {
            this.launchNotification(this.mContext, hourOfDay, minute);
        }
    }

    private void launchNotification(Context context, int hourOfDay, int minute)
    {
        Log.d(this.getClass().toString(), "LaunchNotification(" + hourOfDay + "," + minute + ")");

        if(Calendar.getInstance().getTimeInMillis() - this.mLastNotificationLaunchTime < 1000)
        {
            Log.d(this.getClass().toString(), "Notification launch canceled since it's within 300ms from last launch");
            return;
        }

        this.mLastNotificationLaunchTime = Calendar.getInstance().getTimeInMillis();

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification
        String hourOfDayString = String.valueOf(hourOfDay);
        String minuteString = String.valueOf(minute);
        if(minute < 10)
        {
            minuteString = "0" + minuteString;
        }

        Resources res = this.mContext.getResources();
        // String notificationTitle = res.getString(R.string.name_target_station) + "発 ";
        String notificationTitle = TrainTimeTableUtility.GetStationDisplayName(UserDataManager.GetStationId(context)) + "発 ";

        int dayType = UserDataManager.GetDirectionType(this.mContext);
        if(dayType == Constants.DIRECTION_TYPE_1)
        {
            notificationTitle += res.getString(R.string.name_direction1);
        }
        else if(dayType == Constants.DIRECTION_TYPE_2)
        {
            notificationTitle += res.getString(R.string.name_direction2);
        }

        notificationTitle += "方面 " + res.getString(R.string.name_line);

        String notificationTicker = res.getString(R.string.label_notification_message_prefix) + hourOfDayString + ":" + minuteString + res.getString(R.string.label_notification_message_suffix);
        String notificationContent = notificationTicker;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pIntent);
        builder.setTicker(notificationTicker);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationContent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(true);
        builder.setWhen(Calendar.getInstance().getTimeInMillis());

        this.mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mNotificationManager.cancel(this.PENDING_INTENT_REQUEST_CODE);
        this.mNotificationManager.notify(this.PENDING_INTENT_REQUEST_CODE, builder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("onReceive", "Receiving broadcast intent");

        this.mContext = context;

        // Next train data
        TrainTimeTableManager trainTimeTableManager = TrainTimeTableManager.getInstance(this.mContext);
        TrainTimeData nextTrainData = trainTimeTableManager.FindNextTrainData();

        if(null == nextTrainData)
        {
            Log.e(this.getClass().toString(), "Could NOT find next train data");
            return;
        }

        // set following notification to update train time
        this.SetNotification(context, nextTrainData.HourOfDay(), nextTrainData.Minute());
    }
}
