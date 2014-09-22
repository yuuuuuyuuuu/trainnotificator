package com.dev.android.yuu.trainnotificator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dev.android.yuu.trainnotificator.utility.TrainTimeTableUtility;

import java.util.ArrayList;

/**
 * Created by Chieko on 9/14/14.
 */
public class StationSettingFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemClickListener{

    public interface OnStationChangedListener
    {
        public void onStationChanged(int stationId);
    }

    private OnStationChangedListener mCallback = null;

    private View mView = null;
    private Button mButtonStationSetting = null;

    private ArrayList<StationData> mStationDataList = null;
    private ListView mStationListView = null;
    private AlertDialog.Builder mStationListDialog = null;
    private AlertDialog mStationListDialogBody = null;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(this.getClass().toString(), "onCreateView");

        this.mView = inflator.inflate(R.layout.fragment_station_setting, container, false);

        this.setUiEventHandler();

        this.setStationListView();

        this.setUpStation();

        return this.mView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            this.mCallback = (OnStationChangedListener)activity;
        }
        catch (Exception e)
        {
            throw new ClassCastException(activity.toString() + " cannot be casted to OnStationChangedListener");
        }
    }


    private void setUpStation()
    {
        Log.d(this.getClass().toString(), "setUpStation");

        int stationId = this.loadStation();
        Log.d(this.getClass().toString(), "stationId: " + stationId);

        StationData stationData = null;
        for(StationData data : this.mStationDataList)
        {
            if(data.Id() == stationId)
            {
                stationData = data;
                break;
            }
        }

        if(null == stationData)
        {
            Log.d(this.getClass().toString(), "saved station data not found");
            return;
        }

        this.updateStation(stationData.Name());

    }

    private void setStationListView() {
        Log.d(this.getClass().toString(), "setStationList");

        this.mStationDataList = new ArrayList<StationData>();

        StationData stationData1 = new StationData("横浜", Constants.STATION_ID_YOKOHAMA);
        StationData stationData2 = new StationData("新川崎", Constants.STATION_ID_SHINKAWASAKI);
        StationData stationData3 = new StationData("武蔵小杉", Constants.STATION_ID_MUSASHIKOSUGI);
        StationData stationData4 = new StationData("西大井", Constants.STATION_ID_NISHIOI);
        StationData stationData5 = new StationData("大崎", Constants.STATION_ID_OSAKI);

        this.mStationDataList.add(stationData1);
        this.mStationDataList.add(stationData2);
        this.mStationDataList.add(stationData3);
        this.mStationDataList.add(stationData4);
        this.mStationDataList.add(stationData5);

        //ArrayAdapter<StationData> arrayAdapter = new ArrayAdapter<StationData>(this.getActivity(), android.R.layout.simple_list_item_1, this.mStationDataList);
        StationListAdapter stationListAdapter = new StationListAdapter(this.getActivity(), R.layout.station_list_item, this.mStationDataList);

        this.mStationListView = new ListView(this.getActivity());
        this.mStationListView.setAdapter(stationListAdapter);

        this.mStationListDialog = new AlertDialog.Builder(this.getActivity());
        this.mStationListDialog.setTitle("駅の選択");
        this.mStationListDialog.setIcon(R.drawable.ic_action_location_found_dark);
        this.mStationListDialog.setNegativeButton(R.string.label_setting_station_dialog_cancel, this);

        this.mStationListDialogBody = this.mStationListDialog.setView(this.mStationListView).create();

        this.mStationListView.setOnItemClickListener(this);

    }
    private void setUiEventHandler()
    {
        Log.d(this.getClass().toString(), "setUiEventHandler");

        this.mButtonStationSetting = (Button)this.mView.findViewById(R.id.buttonStationSetting);
        this.mButtonStationSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        Log.d(this.getClass().toString(), "onClick viewId: " + viewId);

        switch (viewId)
        {
            case R.id.buttonStationSetting:

                /*test*/
                //this.mStationListDialog.show();
                this.mStationListDialogBody.show();
                break;

            default:
                break;

        }

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i)
    {
        Log.d(this.getClass().toString(), "onClick(DialogInterface) i: " + i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Log.d(this.getClass().toString(), "onItemClick position: " + position);

        this.mStationListDialogBody.cancel();

        StationData stationData = this.mStationDataList.get(position);

        this.saveStation(stationData.Id());
        this.updateStation(stationData.Name());
    }

    private void updateStation(String stationName)
    {
        Log.d(this.getClass().toString(), "updateStation(" + stationName + ")");

        // test
        TrainTimeTableUtility.GetTodaysTimetable(this.getActivity());

        this.mButtonStationSetting.setText(stationName);
    }

    private void saveStation(int stationId)
    {
        Log.d(this.getClass().toString(), "saveStation(" + stationId + ")");

        UserDataManager.SaveStation(stationId, this.getActivity());
    }

    private int loadStation()
    {
        Log.d(this.getClass().toString(), "loadStation");

        return UserDataManager.GetStationId(this.getActivity());
    }
}
