package com.example.android.housecut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jose Fernandes on 11/30/2016.
 */

public class RoommateAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Roommate> mDataSource;

    public RoommateAdapter(Context context, ArrayList<Roommate> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_roomate, parent, false);

        TextView titleTextView = (TextView) rowView.findViewById(R.id.roommate_name);
        Roommate roommate = (Roommate) getItem(position);

        titleTextView.setText(roommate.name);

        return rowView;
    }
}
