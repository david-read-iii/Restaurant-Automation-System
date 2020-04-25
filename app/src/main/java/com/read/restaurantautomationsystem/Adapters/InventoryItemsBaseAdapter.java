package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Activities.ModifyInventoryItemActivity;
import com.read.restaurantautomationsystem.Models.InventoryItem;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class InventoryItemsBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    /**
     * Defines how InventoryItem objects in the ArrayList inventoryItems should be adapted to be displayed
     * in a ListView.
     */
    public InventoryItemsBaseAdapter(Context context, ArrayList<InventoryItem> inventoryItems) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the list view.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_inventory_item, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView name = view.findViewById(R.id.text_view_inventory_item_name);
        TextView quantity = view.findViewById(R.id.text_view_inventory_item_quantity);

        // Set the text inside each view to the attributes of each InventoryItem.
        name.setText(inventoryItems.get(i).getName());
        quantity.setText(inventoryItems.get(i).getQuantity());

        // Attach a click listener to each view to start the ModifyInventoryItemActivity.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ModifyInventoryItemActivity.class);

                // Pass the attributes of the selected InventoryItem to the activity.
                intent.putExtra("key", inventoryItems.get(i).getKey());
                intent.putExtra("name", inventoryItems.get(i).getName());
                intent.putExtra("quantity", inventoryItems.get(i).getQuantity());

                context.startActivity(intent);
            }
        });
        return view;
    }
}
