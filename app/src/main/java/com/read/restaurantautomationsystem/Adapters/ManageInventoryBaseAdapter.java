package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Comparators.InventoryItemsComparator;
import com.read.restaurantautomationsystem.Models.InventoryItem;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManageInventoryBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InventoryItem> inventoryItems;

    /**
     * Defines how InventoryItem objects in the ArrayList inventoryItems should be adapted to be displayed
     * in a ListView.
     */
    public ManageInventoryBaseAdapter(Context context, ArrayList<InventoryItem> inventoryItems) {
        this.context = context;
        this.inventoryItems = inventoryItems;
    }

    @Override
    public int getCount() {
        return inventoryItems.size();
    }

    @Override
    public Object getItem(int i) {
        return inventoryItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_inventory_item, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView name = view.findViewById(R.id.text_view_inventory_item_name);
        TextView quantity = view.findViewById(R.id.text_view_inventory_item_quantity);

        // Retrieve attributes of the ith InventoryItem object in the ArrayList.
        InventoryItem selected = (InventoryItem) getItem(i);

        // Set text as the attributes of the ith InventoryItem object.
        name.setText(selected.getName());
        quantity.setText(Integer.toString(selected.getQuantity()));

        return view;
    }

    /**
     * Before adapting the data, sort the InventoryItem objects in the ArrayList alphabetically by name.
     */
    @Override
    public void notifyDataSetChanged() {
        Collections.sort(inventoryItems, new InventoryItemsComparator());
        super.notifyDataSetChanged();
    }
}
