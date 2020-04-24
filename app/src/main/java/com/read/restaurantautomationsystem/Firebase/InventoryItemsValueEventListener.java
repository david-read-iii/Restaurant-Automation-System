package com.read.restaurantautomationsystem.Firebase;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Adapters.InventoryItemsBaseAdapter;
import com.read.restaurantautomationsystem.Models.InventoryItem;

import java.util.ArrayList;

public class InventoryItemsValueEventListener implements ValueEventListener {

    private ArrayList<InventoryItem> inventoryItems;
    private InventoryItemsBaseAdapter baseAdapter;
    private TextView textViewEmpty;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with InventoryItem objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView. When the ArrayList is empty, a given TextView is notified so that it can display
     * text that indicates so.
     */
    public InventoryItemsValueEventListener(ArrayList<InventoryItem> inventoryItems, InventoryItemsBaseAdapter baseAdapter, TextView textViewEmpty) {
        this.inventoryItems = inventoryItems;
        this.baseAdapter = baseAdapter;
        this.textViewEmpty = textViewEmpty;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear the ArrayList.
        inventoryItems.clear();

        // Retrieve new Employee objects from the database and store them in the ArrayList.
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            InventoryItem inventoryItem = new InventoryItem(
                    ds.getKey(),
                    ds.child("name").getValue(String.class),
                    ds.child("quantity").getValue(String.class)
            );
            inventoryItems.add(inventoryItem);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();

        // Set visibility of the empty TextView depending on if the ArrayList is empty.
        if (inventoryItems.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
