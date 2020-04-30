package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Models.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MenuItemsValueEventListener implements ValueEventListener {

    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItem>> menuItemsByCategory;
    private BaseExpandableListAdapter baseAdapter;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with MenuItem objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView.
     */
    public MenuItemsValueEventListener(ArrayList<String> categories, HashMap<String, ArrayList<MenuItem>> menuItemsByCategory, BaseExpandableListAdapter baseAdapter) {
        this.categories = categories;
        this.menuItemsByCategory = menuItemsByCategory;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear the ArrayList and HashMap.
        categories.clear();
        menuItemsByCategory.clear();

        /* Retrieve new MenuItem objects from the database. Store all MenuItem categories once in
         * the ArrayList categories. Map all MenuItems to their categories in HashMap menuItemsByCategory. */
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            MenuItem menuItem = new MenuItem(
                    ds.getKey(),
                    ds.child("name").getValue(String.class),
                    ds.child("price").getValue(Double.class),
                    ds.child("category").getValue(String.class)
            );

            if (!categories.contains(menuItem.getCategory())) {
                categories.add(menuItem.getCategory());
                menuItemsByCategory.put(menuItem.getCategory(), new ArrayList<MenuItem>());
            }

            menuItemsByCategory.get(menuItem.getCategory()).add(menuItem);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
