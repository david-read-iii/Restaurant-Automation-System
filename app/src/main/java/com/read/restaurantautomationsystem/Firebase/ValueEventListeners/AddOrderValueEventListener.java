package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseExpandableListAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.Models.MenuItemWithQuantity;

import java.util.ArrayList;
import java.util.HashMap;

public class AddOrderValueEventListener implements ValueEventListener {

    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory;
    private BaseExpandableListAdapter baseAdapter;
    private DatabaseReference databaseReference;

    /**
     * Defines a ValueEventListener to fill an ArrayList with a MenuItemWithQuantity object for every
     * MenuItem object stored in the current state of the database. This listener is not meant to
     * sync a ListView and will only execute once.
     */
    public AddOrderValueEventListener(ArrayList<String> categories, HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory, BaseExpandableListAdapter baseAdapter, DatabaseReference databaseReference) {
        this.categories = categories;
        this.menuItemsWithQuantityByCategory = menuItemsWithQuantityByCategory;
        this.baseAdapter = baseAdapter;
        this.databaseReference = databaseReference;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        // Clear the ArrayList and HashMap.
        categories.clear();
        menuItemsWithQuantityByCategory.clear();

        /* Retrieve new MenuItem objects from the database. Store each MenuItem categories once in
         * the ArrayList categories. Store MenuItem objects as MenuItemWithQuantity objects. Map all
         * MenuItemWithQuantity objects to their categories in HashMap menuItemsWithQuantityByCategory. */
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            MenuItemWithQuantity menuItemWithQuantity = new MenuItemWithQuantity(
                    new MenuItem(ds.getKey(), ds.child("name").getValue(String.class), ds.child("price").getValue(String.class), ds.child("category").getValue(String.class)),
                    "0"
            );

            if (!categories.contains(menuItemWithQuantity.getMenuItem().getCategory())) {
                categories.add(menuItemWithQuantity.getMenuItem().getCategory());
                menuItemsWithQuantityByCategory.put(menuItemWithQuantity.getMenuItem().getCategory(), new ArrayList<MenuItemWithQuantity>());
            }

            menuItemsWithQuantityByCategory.get(menuItemWithQuantity.getMenuItem().getCategory()).add(menuItemWithQuantity);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();

        // Remove this listener after getting the MenuItemWithQuantity objects from the database once.
        databaseReference.child("MenuItems").removeEventListener(this);
    }
    
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
