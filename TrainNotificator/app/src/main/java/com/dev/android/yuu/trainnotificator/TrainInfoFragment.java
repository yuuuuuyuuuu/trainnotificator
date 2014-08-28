package com.dev.android.yuu.trainnotificator;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Chieko on 8/28/14.
 */
public class TrainInfoFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private Button testButton = null;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        this.view = inflator.inflate(R.layout.fragment_train_info, container, false);

        this.setUiEventHandlers();

        return this.view;
    }

    private void setUiEventHandlers()
    {
        this.testButton = (Button)this.view.findViewById(R.id.button_test);
        this.testButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("Fragment", "onCLick");
    }
}
