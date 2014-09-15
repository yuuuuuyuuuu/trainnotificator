package com.dev.android.yuu.trainnotificator;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chieko on 9/15/14.
 */
public class StationListAdapter extends ArrayAdapter<StationData>
{
    private LayoutInflater mInflator = null;

    public StationListAdapter(Context context, int resource, ArrayList<StationData> list)
    {
        super(context, resource, list);

        this.mInflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        StationData data = (StationData)getItem(position);

        if(null == v) v = this.mInflator.inflate(R.layout.station_list_item, null);

        TextView textView = (TextView)v.findViewById(R.id.textView_station_name);
        textView.setText(data.Name());

        return v;
    }

}
