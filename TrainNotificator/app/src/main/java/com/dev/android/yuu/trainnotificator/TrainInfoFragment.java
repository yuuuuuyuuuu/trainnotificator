package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Chieko on 8/28/14.
 */
public class TrainInfoFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private TextView mTextViewTrainInfo = null;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(this.getClass().toString(), "onCreateView");

        this.view = inflator.inflate(R.layout.fragment_train_info, container, false);

        this.setUiEventHandlers();

        return this.view;
    }

    public void onResume()
    {
        Log.d(this.getClass().toString(), "onResume");
        super.onResume();

        // Update train information
        TrainTimeTableManager trainTimeTableManager = new TrainTimeTableManager(this.getActivity());
        TrainTimeData nextTrainTimeData = trainTimeTableManager.FindNextTrainData();

        String minuteString = String.valueOf(nextTrainTimeData.Minute());
        if(nextTrainTimeData.Minute() < 10) minuteString = "0" + minuteString;

        String trainInfoLabel = nextTrainTimeData.HourOfDay() + ":" + minuteString;
        this.mTextViewTrainInfo.setText(trainInfoLabel);
    }

    private void setUiEventHandlers()
    {
        this.mTextViewTrainInfo = (TextView)this.view.findViewById(R.id.textView_trainInfo);
    }

    @Override
    public void onClick(View view) {
        Log.d("Fragment", "onCLick");
    }
}
