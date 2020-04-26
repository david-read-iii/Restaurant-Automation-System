package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Adapters.InventoryItemsBaseAdapter;
import com.read.restaurantautomationsystem.Models.InventoryItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InventoryItemsValueEventListener implements ValueEventListener {

    private ArrayList<InventoryItem> inventoryItems;
    private BaseAdapter baseAdapter;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with InventoryItem objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView.
     */
    public InventoryItemsValueEventListener(ArrayList<InventoryItem> inventoryItems, BaseAdapter baseAdapter) {
        this.inventoryItems = inventoryItems;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear the ArrayList.
        inventoryItems.clear();

        // Retrieve new InventoryItem objects from the database and store them in the ArrayList.
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            InventoryItem inventoryItem = new InventoryItem(
                    ds.getKey(),
                    ds.child("name").getValue(String.class),
                    ds.child("quantity").getValue(String.class)
            );
            inventoryItems.add(inventoryItem);
        }

        // Sort InventoryItem objects in ArrayList alphabetically by name.
        Collections.sort(inventoryItems, new Comparator<InventoryItem>() {
            @Override
            public int compare(InventoryItem i1, InventoryItem i2) {
                return i1.getName().compareToIgnoreCase(i2.getName());
            }
        });

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
