package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Models.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TablesValueEventListener implements ValueEventListener {

    private ArrayList<Table> tables;
    private BaseAdapter baseAdapter;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with Table objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView.
     */
    public TablesValueEventListener(ArrayList<Table> tables, BaseAdapter baseAdapter) {
        this.tables = tables;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear the ArrayList.
        tables.clear();

        // Retrieve new Table objects from the database and store them in the ArrayList.
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Table table = new Table(
                    ds.getKey(),
                    ds.child("name").getValue(String.class),
                    ds.child("status").getValue(String.class)
            );
            tables.add(table);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
