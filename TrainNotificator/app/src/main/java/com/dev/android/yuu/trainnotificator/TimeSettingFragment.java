package com.dev.android.yuu.trainnotificator;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeSettingFragment.TimeSettingChangeListener} interface
 * to handle interaction events.
 * Use the {@link TimeSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TimeSettingFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TimeSettingChangeListener mListener;

    private View mView = null;
    private Button mButtonStartTime = null;
    private Button mButtonEndTime = null;

    private TimePickerDialog mTimePickerDialog = null;
    private int mLastClickButtonId = -1; /* ideitify start-time or end-time button clicked */

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeSettingFragment newInstance(String param1, String param2) {
        TimeSettingFragment fragment = new TimeSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }
    public TimeSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mView = inflater.inflate(R.layout.fragment_time_setting, container, false);

        this.setUiEventListeners();

        this.initializeTimePickerDialog();
        this.initializeTimeSetting();

        return this.mView;

    }

    private void initializeTimePickerDialog()
    {
        this.mTimePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar, this, 7, 0, true);
    }

    private void initializeTimeSetting()
    {
        Log.d(this.getClass().toString(), "initializeTimeSetting");

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
        this.mButtonStartTime.setText(startLabel);

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
        this.mButtonEndTime.setText(endLabel);
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

    private void setLastClickButtonId(int buttonId)
    {
        this.mLastClickButtonId = buttonId;
    }



    private void setUiEventListeners()
    {
        Log.d(this.getClass().toString(), "setUiEventListeners");

        this.mButtonStartTime = (Button)this.mView.findViewById(R.id.button_setting_start_time);
        this.mButtonEndTime = (Button)this.mView.findViewById(R.id.button_setting_end_time);

        this.mButtonStartTime.setOnClickListener(this);
        this.mButtonEndTime.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListener = (TimeSettingChangeListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();

        Log.d(this.getClass().toString(), "onClick viewId:" + viewId);

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

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
    {
        Log.d("onTimeSet", "Set to " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        switch (this.mLastClickButtonId)
        {
            case R.id.button_setting_start_time:
                this.setStartTime(hourOfDay, minute);
                break;

            case R.id.button_setting_end_time:
                this.setEndTime(hourOfDay, minute);
                break;

            default:
                this.mLastClickButtonId = -1;
                break;
        }
    }

    private void setStartTime(int hourOfDay, int minute)
    {
        Log.d("setStartTime", "Setting start time " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        UserDataManager.SaveStartTime(hourOfDay, minute, getActivity());

        String minuteString = String.valueOf(minute);
        if(minute < 10) minuteString = "0" + minuteString;

        String label = String.valueOf(hourOfDay) + ":" + minuteString;
        this.mButtonStartTime.setText(label);

        this.mListener.onStartTimeChanged(hourOfDay, minute);

    }

    private void setEndTime(int hourOfDay, int minute)
    {
        Log.d("setEndTime", "Setting end time " + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

        UserDataManager.SaveEndTime(hourOfDay, minute, getActivity());

        String minuteString = String.valueOf(minute);
        if(minute < 10) minuteString = "0" + minuteString;

        String label = String.valueOf(hourOfDay) + ":" + minuteString;
        this.mButtonEndTime.setText(label);

        this.mListener.onEndTimeChanged(hourOfDay, minute);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface TimeSettingChangeListener
    {
        public void onStartTimeChanged(int hourOfDay, int minute);
        public void onEndTimeChanged(int hourOfDay, int minute);
    }

}
