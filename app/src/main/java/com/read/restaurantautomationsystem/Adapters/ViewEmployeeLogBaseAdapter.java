package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Comparators.LogComparator;
import com.read.restaurantautomationsystem.Models.Log;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.Collections;

public class ViewEmployeeLogBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Log> logs;

    /**
     * Defines how Log objects in the ArrayList logs should be adapted to be displayed in the
     * ListView in ViewEmployeeLogFragment.
     */
    public ViewEmployeeLogBaseAdapter(Context context, ArrayList<Log> logs) {
        this.context = context;
        this.logs = logs;
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public Object getItem(int i) {
        return logs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_log, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView message = view.findViewById(R.id.text_view_log_message);
        TextView dateTime = view.findViewById(R.id.text_view_log_date_time_completed);

        // Retrieve attributes from the ith Log object in the ArrayList.
        Log selected = (Log) getItem(i);

        // Set text as the attributes of the ith Log object.
        message.setText(selected.getMessage());
        dateTime.setText(selected.getDateTimeCompleted().toString());

        return view;
    }

    /**
     * Before adapting the data, sort the Log objects in the ArrayList reverse chronologically.
     */
    @Override
    public void notifyDataSetChanged() {

        // Sort Tables objects in ArrayList alphabetically by name.
        Collections.sort(logs, new LogComparator());

        super.notifyDataSetChanged();
    }
}
