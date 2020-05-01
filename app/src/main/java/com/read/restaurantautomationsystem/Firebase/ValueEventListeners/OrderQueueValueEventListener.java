package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.Models.MenuItemWithQuantity;
import com.read.restaurantautomationsystem.Models.Order;

import java.util.ArrayList;
import java.util.Date;

public class OrderQueueValueEventListener implements ValueEventListener {

    private ArrayList<Order> orders;
    private BaseAdapter baseAdapter;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with Order objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView.
     */
    public OrderQueueValueEventListener(ArrayList<Order> orders, BaseAdapter baseAdapter) {
        this.orders = orders;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear the ArrayList.
        orders.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            ArrayList<MenuItemWithQuantity> orderedMenuItemsWithQuantity = new ArrayList<>();

            for (DataSnapshot ds1 : dataSnapshot.child(ds.getKey()).child("orderedMenuItemsWithQuantity").getChildren()) {
                orderedMenuItemsWithQuantity.add(new MenuItemWithQuantity(
                        new MenuItem(
                                ds1.child("menuItem").child("key").getValue(String.class),
                                ds1.child("menuItem").child("name").getValue(String.class),
                                ds1.child("menuItem").child("price").getValue(Double.class),
                                ds1.child("menuItem").child("category").getValue(String.class)
                        ),
                        ds1.child("quantity").getValue(Integer.class),
                        ds1.child("totalPrice").getValue(Double.class)
                ));
            }

            Order order = new Order(
                    ds.getKey(),
                    ds.child("number").getValue(Integer.class),
                    ds.child("status").getValue(String.class),
                    ds.child("totalPrice").getValue(Double.class),
                    ds.child("dateTimeOrdered").getValue(Date.class),
                    ds.child("tableNameOrdered").getValue(String.class),
                    orderedMenuItemsWithQuantity
            );
            orders.add(order);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
