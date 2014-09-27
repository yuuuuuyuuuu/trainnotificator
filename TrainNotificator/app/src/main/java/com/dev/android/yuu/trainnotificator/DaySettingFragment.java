package com.dev.android.yuu.trainnotificator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DaySettingFragment.OnDayChangeListener} interface
 * to handle interaction events.
 * Use the {@link DaySettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DaySettingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnDayChangeListener mListener;

    private View mView = null;

    // Radio Button
    private RadioButton mRadioButtonWeekday = null;
    private RadioButton mRadioButtonWeekend = null;
    private RadioButton mRadioButtonAllday = null;

    private boolean mIsInitialSettingCompleted = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DaySettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DaySettingFragment newInstance(String param1, String param2) {
        DaySettingFragment fragment = new DaySettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public DaySettingFragment() {
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
        // Inflate the layout for this fragment
        this.mView = inflater.inflate(R.layout.fragment_day_setting, container, false);

        this.setUiEventHandlers();
        this.initializeDay();

        return this.mView;
    }

    private void initializeDay()
    {
        Log.d(this.getClass().toString(), "initializeDay");

        int day = UserDataManager.GetDateType(this.getActivity());

        switch (day)
        {
            case Constants.DATE_TYPE_WEEKDAY:
                this.mRadioButtonWeekday.setChecked(true);

                break;
            case Constants.DATE_TYPE_WEEKEND:
                this.mRadioButtonWeekend.setChecked(true);

                break;

            case Constants.DATE_TYPE_ALLDAY:
                this.mRadioButtonAllday.setChecked(true);

                break;

            default:
                break;
        }

        this.mIsInitialSettingCompleted = true;
    }

    private void setUiEventHandlers()
    {
        Log.d(this.getClass().toString(), "setUiEventHandlers");

        this.mRadioButtonWeekday = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_weekday);
        this.mRadioButtonWeekend = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_weekend);
        this.mRadioButtonAllday = (RadioButton)this.mView.findViewById(R.id.radiobutton_setting_allday);

        this.mRadioButtonWeekday.setOnCheckedChangeListener(this);
        this.mRadioButtonWeekend.setOnCheckedChangeListener(this);
        this.mRadioButtonAllday.setOnCheckedChangeListener(this);

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnDayChangeListener) activity;
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
    {
        int radioButtonId = compoundButton.getId();
        Log.d("onCheckedChanged RadioButtonId: ", String.valueOf(radioButtonId));

        // style
        if(isChecked)
        {
            this.setSelectedStyle((RadioButton)compoundButton);
        }
        else
        {
            this.setUnselectedStyle((RadioButton)compoundButton);
        }

        if(!isChecked) return;

        switch (radioButtonId)
        {
            case R.id.radiobutton_setting_weekday:
                this.setDateType(Constants.DATE_TYPE_WEEKDAY);

                break;

            case R.id.radiobutton_setting_weekend:
                this.setDateType(Constants.DATE_TYPE_WEEKEND);

                break;

            case R.id.radiobutton_setting_allday:
                this.setDateType(Constants.DATE_TYPE_ALLDAY);

                break;

            default:
                break;
        }

    }

    private void setSelectedStyle(RadioButton radioButton)
    {
        Log.d(this.getClass().toString(), "setSelectedStyle");

        int id = radioButton.getId();

        if(R.id.radiobutton_setting_weekday == id
                || R.id.radiobutton_setting_weekend == id
                || R.id.radiobutton_setting_allday == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_green_button);
        }
        else if(R.id.radioButton_direction1 == id || R.id.radioButton_direction2 == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_orange_button);
        }
        else
        {
            Log.e(this.getClass().toString(), "Unexpected radio button");
        }
    }

    private void setUnselectedStyle(RadioButton radioButton)
    {
        Log.d(this.getClass().toString(), "setUnselectedStyle");

        int id = radioButton.getId();

        if(R.id.radiobutton_setting_weekday == id
                || R.id.radiobutton_setting_weekend == id
                || R.id.radiobutton_setting_allday == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_button_pale_green);
        }
        else if(R.id.radioButton_direction1 == id || R.id.radioButton_direction2 == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_button_pale_orange);
        }
        else
        {
            Log.e(this.getClass().toString(), "Unexpected radio button");
        }
    }

    private void setDateType(int dateType)
    {
        switch (dateType)
        {
            case Constants.DATE_TYPE_WEEKDAY:
                UserDataManager.SaveDateType(Constants.DATE_TYPE_WEEKDAY, getActivity());
                break;

            case Constants.DATE_TYPE_WEEKEND:
                UserDataManager.SaveDateType(Constants.DATE_TYPE_WEEKEND, getActivity());
                break;

            case Constants.DATE_TYPE_ALLDAY:
                UserDataManager.SaveDateType(Constants.DATE_TYPE_ALLDAY, getActivity());

                break;

            default:
                break;
        }

        if(this.mIsInitialSettingCompleted) this.mListener.onDayChanged(dateType);
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
    public interface OnDayChangeListener
    {
        public void onDayChanged(int dayType);
    }

}
