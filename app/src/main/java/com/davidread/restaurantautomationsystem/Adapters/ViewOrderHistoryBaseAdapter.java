package com.davidread.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.davidread.restaurantautomationsystem.Comparators.OrdersComparator;
import com.davidread.restaurantautomationsystem.Models.Order;
import com.davidread.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.Collections;

public class ViewOrderHistoryBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Order> orders;

    /**
     * Defines how Order objects in the ArrayList orders should be adapted to be displayed in a
     * ListView.
     */
    public ViewOrderHistoryBaseAdapter(Context context, ArrayList<Order> orders) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_order_complete, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView number = view.findViewById(R.id.text_view_order_complete_number);

        // Retrieve attributes from the ith Order object in the ArrayList.
        Order selected = (Order) getItem(i);

        // Set text as the attributes of the ith Order object.
        number.setText(context.getString(R.string.label_order, Integer.toString(selected.getNumber())));

        return view;
    }

    /**
     * Before adapting the data, sort the Order objects in the ArrayList by number.
     */
    @Override
    public void notifyDataSetChanged() {

        Collections.sort(orders, new OrdersComparator());

        super.notifyDataSetChanged();
    }
}
