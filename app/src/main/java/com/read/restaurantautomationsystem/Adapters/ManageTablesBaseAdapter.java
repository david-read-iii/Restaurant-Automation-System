package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ManageTablesBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Table> tables;

    /**
     * Defines how Table objects in the ArrayList tables should be adapted to be displayed
     * in the ListView in the ManageTablesFragment.
     */
    public ManageTablesBaseAdapter(Context context, ArrayList<Table> tables) {
        this.context = context;
        this.tables = tables;
    }

    @Override
    public int getCount() {
        return tables.size();
    }

    @Override
    public Object getItem(int i) {
        return tables.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_table_manager, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView name = view.findViewById(R.id.text_view_table_manager_view_name);

        // Retrieve attributes of the ith Table object in the ArrayList.
        Table selected = (Table) getItem(i);

        // Set text as the attributes of the ith Table object.
        name.setText(selected.getName());

        return view;
    }
}
