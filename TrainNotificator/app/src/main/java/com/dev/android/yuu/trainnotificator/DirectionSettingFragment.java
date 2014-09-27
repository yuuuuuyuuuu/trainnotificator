package com.dev.android.yuu.trainnotificator;

import android.app.Activity;
import android.content.res.Resources;
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
 * {@link DirectionSettingFragment.OnDirectionChangeListener} interface
 * to handle interaction events.
 * Use the {@link DirectionSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DirectionSettingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnDirectionChangeListener mListener;

    private View mView = null;
    private RadioButton mRadioButtonDirection1 = null;
    private RadioButton mRadioButtonDirection2 = null;
    private boolean mIsInitializeCompleted = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectionSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectionSettingFragment newInstance(String param1, String param2) {
        DirectionSettingFragment fragment = new DirectionSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public DirectionSettingFragment() {
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
        this.mView = inflater.inflate(R.layout.fragment_direction_setting, container, false);

        this.setUiEventListeners();

        this.initializeDirection();

        return this.mView;
    }

    private void initializeDirection()
    {
        Log.d(this.getClass().toString(), "initializeDirection");

        int directionType = UserDataManager.GetDirectionType(this.getActivity());

        switch (directionType)
        {
            case Constants.DIRECTION_TYPE_1:
                this.mRadioButtonDirection1.setChecked(true);
                break;

            case Constants.DIRECTION_TYPE_2:
                this.mRadioButtonDirection2.setChecked(true);
                break;

            default:
                this.mRadioButtonDirection1.setChecked(true);
                break;

        }

        this.mIsInitializeCompleted = true;
    }

    private void setUiEventListeners()
    {
        Log.d(this.getClass().toString(), "setUiEventListeners");

        this.mRadioButtonDirection1 = (RadioButton)this.mView.findViewById(R.id.radioButton_direction1);
        this.mRadioButtonDirection2 = (RadioButton)this.mView.findViewById(R.id.radioButton_direction2);

        this.mRadioButtonDirection1.setOnCheckedChangeListener(this);
        this.mRadioButtonDirection2.setOnCheckedChangeListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDirectionChangeListener) activity;
        } catch (ClassCastException e) {
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
            case R.id.radioButton_direction1:
                this.setDirectionType(Constants.DIRECTION_TYPE_1);

                break;

            case R.id.radioButton_direction2:
                this.setDirectionType(Constants.DIRECTION_TYPE_2);

                break;

            default:
                break;
        }

    }

    private void setSelectedStyle(RadioButton radioButton)
    {
        Log.d(this.getClass().toString(), "setSelectedStyle");

        int id = radioButton.getId();

        if(R.id.radioButton_direction1 == id || R.id.radioButton_direction2 == id)
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

        if(R.id.radioButton_direction1 == id || R.id.radioButton_direction2 == id)
        {
            radioButton.setBackgroundResource(R.drawable.round_button_pale_orange);
        }
        else
        {
            Log.e(this.getClass().toString(), "Unexpected radio button");
        }
    }

    private void setDirectionType(int directionType)
    {
        switch (directionType)
        {
            case Constants.DIRECTION_TYPE_1:
                UserDataManager.SaveDirectionType(Constants.DIRECTION_TYPE_1, getActivity());

                break;

            case Constants.DIRECTION_TYPE_2:
                UserDataManager.SaveDirectionType(Constants.DIRECTION_TYPE_2, getActivity());

                break;

            default:
                break;
        }

        if(this.mIsInitializeCompleted) this.mListener.onDirectionChanged(directionType);

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
    public interface OnDirectionChangeListener
    {
        public void onDirectionChanged(int directionType);
    }

}
