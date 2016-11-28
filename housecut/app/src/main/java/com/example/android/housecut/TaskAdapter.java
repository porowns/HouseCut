package com.example.android.housecut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ckm13 on 11/27/2016.
 */

public class TaskAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Task> mDataSource;

    public TaskAdapter(Context context, ArrayList<Task> items) {
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
        View rowView = mInflater.inflate(R.layout.list_item_task, parent, false);

        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.task_list_name);

        TextView typeTextView =
                (TextView) rowView.findViewById(R.id.task_list_type);

        TextView assignedTextView =
                (TextView) rowView.findViewById(R.id.task_list_assigned);

        Task task = (Task) getItem(position);

        titleTextView.setText(task.name);
        assignedTextView.setText("Assigned to: " + task.currentlyAssignedName);
        typeTextView.setText("Type: " + task.type);


        return rowView;
    }
}
