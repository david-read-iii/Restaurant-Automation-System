package com.davidread.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.davidread.restaurantautomationsystem.Models.Log;

import java.util.ArrayList;
import java.util.Date;

public class ViewEmployeeLogValueEventListener implements ValueEventListener {

    private ArrayList<Log> logs;
    private BaseAdapter baseAdapter;

    public ViewEmployeeLogValueEventListener(ArrayList<Log> logs, BaseAdapter baseAdapter) {
        this.logs = logs;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear the ArrayList.
        logs.clear();

        // Retrieve new Log objects from the database and store them in the ArrayList.
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            Log log = new Log(
                    ds.getKey(),
                    ds.child("message").getValue(String.class),
                    ds.child("dateTimeCompleted").getValue(Date.class)
            );
            logs.add(log);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
