package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Activities.ModifyTableActivity;
import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class TablesBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Table> tables;

    /**
     * Defines how Table objects in the ArrayList tables should be adapted to be displayed
     * in a ListView.
     */
    public TablesBaseAdapter(Context context, ArrayList<Table> tables) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_table_manager_view, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView name = view.findViewById(R.id.text_view_table_name);

        // Set the text inside each view to the views of the attributes of each Table.
        name.setText(tables.get(i).getName());

        // Attach a click listener to each view to start the ModifyTableActivity.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ModifyTableActivity.class);

                // Pass the attributes of the selected Table object to the activity.
                intent.putExtra("key", tables.get(i).getKey());
                intent.putExtra("name", tables.get(i).getName());
                intent.putExtra("status", tables.get(i).getStatus());

                context.startActivity(intent);
            }
        });
        return view;
    }
}
