package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dev.android.yuu.trainnotificator.utility.TrainTimeTableUtility;

import org.w3c.dom.Text;

/**
 * Created by Chieko on 8/28/14.
 */
public class TrainInfoFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private TextView mTextViewTrainInfo = null;
    private TextView mTextViewTrainInfoTitle = null;

    private NotificationAlarmManager mNotificationManager = null;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(this.getClass().toString(), "onCreateView");

        this.view = inflator.inflate(R.layout.fragment_train_info, container, false);

        this.setUiEventHandlers();

        this.mNotificationManager = NotificationAlarmManager.getInstance(this.getActivity());

        return this.view;
    }

    public void onResume()
    {
        Log.d(this.getClass().toString(), "onResume");
        super.onResume();

        this.updateTrainInfo();
    }

    public void updateTrainInfo()
    {
        Log.d(this.getClass().toString(), "updateTrainInfo");

        Resources res = getResources();

        // Update train information
        TrainTimeTableManager trainTimeTableManager = TrainTimeTableManager.getInstance(this.getActivity());
        trainTimeTableManager.updateTimetable();
        TrainTimeData nextTrainTimeData = trainTimeTableManager.FindNextTrainData();

        String minuteString = String.valueOf(nextTrainTimeData.Minute());
        if(nextTrainTimeData.Minute() < 10) minuteString = "0" + minuteString;

        String trainInfoLabel = nextTrainTimeData.HourOfDay() + ":" + minuteString;
        this.setTrainInfoLabel(trainInfoLabel);

        int directionType = UserDataManager.GetDirectionType(this.getActivity());
        int stationId = UserDataManager.GetStationId(this.getActivity());
        String stationName = TrainTimeTableUtility.GetStationDisplayName(stationId);

        String trainInfoTitle = "次の" + stationName + "発 ";

        if(directionType == Constants.DIRECTION_TYPE_1)
        {
            trainInfoTitle += res.getString(R.string.name_direction1);
        }
        else if(directionType == Constants.DIRECTION_TYPE_2)
        {
            trainInfoTitle += res.getString(R.string.name_direction2);
        }
        else
        {
            Log.e(this.getClass().toString(), "directiooType NOT set unexpectedly");
        }

        trainInfoTitle += " 方面は";
        this.setTrainInfoTitle(trainInfoTitle);

        if(UserDataManager.IsNowInUserPreferableTime(this.getActivity()))
        {
            this.mNotificationManager.LaunchNotification(this.getActivity(), nextTrainTimeData.HourOfDay(), nextTrainTimeData.Minute());
        }

    }


    private void setTrainInfoLabel(String text)
    {
        this.mTextViewTrainInfo.setText(text);
    }

    private void setTrainInfoTitle(String text)
    {
        this.mTextViewTrainInfoTitle.setText(text);
    }

    private void setUiEventHandlers()
    {
        this.mTextViewTrainInfo = (TextView)this.view.findViewById(R.id.textView_trainInfo);
        this.mTextViewTrainInfoTitle = (TextView)this.view.findViewById(R.id.textView_traininfo_title);
    }

    @Override
    public void onClick(View view) {
        Log.d("Fragment", "onCLick");
    }
}
