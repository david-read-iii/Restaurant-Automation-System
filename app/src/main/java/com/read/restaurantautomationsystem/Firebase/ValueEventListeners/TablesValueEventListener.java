package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Adapters.TablesBaseAdapter;
import com.read.restaurantautomationsystem.Models.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TablesValueEventListener implements ValueEventListener {

    private ArrayList<Table> tables;
    private BaseAdapter baseAdapter;
    private TextView textViewEmpty;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with Table objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView. When the ArrayList is empty, a given TextView is notified so that it can display
     * text that indicates so.
     */
    public TablesValueEventListener(ArrayList<Table> tables, BaseAdapter baseAdapter, TextView textViewEmpty) {
        this.tables = tables;
        this.baseAdapter = baseAdapter;
        this.textViewEmpty = textViewEmpty;
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

        // Sort Tables alphabetically.
        Collections.sort(tables, new Comparator<Table>() {
            @Override
            public int compare(Table table1, Table table2) {
                return table1.getName().compareToIgnoreCase(table2.getName());
            }
        });

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();

        // Set visibility of the empty TextView depending on if the ArrayList is empty.
        if (tables.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
