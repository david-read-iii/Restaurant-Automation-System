package com.davidread.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.davidread.restaurantautomationsystem.Models.Order;

import java.util.ArrayList;
import java.util.Date;

public class OrderQueueValueEventListener implements ValueEventListener {

    private ArrayList<Order> orders;
    private BaseAdapter baseAdapter;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with Order objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView. Note that orderedMenuItemsWithQuantity attribute is left intentionally null, and
     * will be fetched when the user navigates to the proper activity.
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

            Order order = new Order(
                    ds.getKey(),
                    ds.child("number").getValue(Integer.class),
                    ds.child("status").getValue(String.class),
                    ds.child("totalPrice").getValue(Double.class),
                    ds.child("dateTimeOrdered").getValue(Date.class),
                    ds.child("tableNameOrdered").getValue(String.class),
                    null
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
