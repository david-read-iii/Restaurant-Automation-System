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

public class MenuItemsWithQuantityValueEventListener implements ValueEventListener {

    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory;
    private BaseExpandableListAdapter baseAdapter;
    private DatabaseReference databaseReference;

    /**
     * Defines a ValueEventListener to fill an ArrayList with one MenuItemWithQuantity object for
     * every MenuItem object stored in the database. This listener will remove itself once it has
     * synced the ArrayList once.
     */
    public MenuItemsWithQuantityValueEventListener(ArrayList<String> categories, HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory, BaseExpandableListAdapter baseAdapter, DatabaseReference databaseReference) {
        this.categories = categories;
        this.menuItemsWithQuantityByCategory = menuItemsWithQuantityByCategory;
        this.baseAdapter = baseAdapter;
        this.databaseReference = databaseReference;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        /* Retrieve new MenuItem objects from the database. Store each MenuItem categories once in
         * the ArrayList categories. Store MenuItem objects as MenuItemWithQuantity objects. Map all
         * MenuItemWithQuantity objects to their categories in HashMap menuItemsWithQuantityByCategory. */
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            MenuItemWithQuantity menuItemWithQuantity = new MenuItemWithQuantity(
                    new MenuItem(ds.getKey(), ds.child("name").getValue(String.class), ds.child("price").getValue(Double.class), ds.child("category").getValue(String.class)),
                    0,
                    0.0
            );

            if (!categories.contains(menuItemWithQuantity.getMenuItem().getCategory())) {
                categories.add(menuItemWithQuantity.getMenuItem().getCategory());
                menuItemsWithQuantityByCategory.put(menuItemWithQuantity.getMenuItem().getCategory(), new ArrayList<MenuItemWithQuantity>());
            }

            menuItemsWithQuantityByCategory.get(menuItemWithQuantity.getMenuItem().getCategory()).add(menuItemWithQuantity);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();

        // Remove this ValueEventListener once the HashMap and ArrayList have been synced once.
        databaseReference.child("MenuItems").removeEventListener(this);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
