package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Models.Order;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class OrderQueueBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Order> orders;

    /**
     * Defines how Order objects in the ArrayList orders should be adapted to be displayed in a
     * ListView.
     */
    public OrderQueueBaseAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int i) {
        return orders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order_incomplete, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView number = view.findViewById(R.id.text_view_order_incomplete_number);
        TextView status = view.findViewById(R.id.text_view_order_incomplete_status);

        // Retrieve attributes from the ith Order object in the ArrayList.
        Order selected = (Order) getItem(i);

        // Set text as the attributes of the ith Order object.
        number.setText(context.getString(R.string.label_order, Integer.toString(selected.getNumber())));
        status.setText(selected.getStatus());

        return view;
    }
}
