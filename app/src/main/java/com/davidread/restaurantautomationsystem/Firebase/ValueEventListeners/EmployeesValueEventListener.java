package com.davidread.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.davidread.restaurantautomationsystem.Models.Employee;

import java.util.ArrayList;

public class EmployeesValueEventListener implements ValueEventListener {

    private ArrayList<Employee> employees;
    private BaseAdapter baseAdapter;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with Employee objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView.
     */
    public EmployeesValueEventListener(ArrayList<Employee> employees, BaseAdapter baseAdapter) {
        this.employees = employees;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear the ArrayList.
        employees.clear();

        // Retrieve new Employee objects from the database and store them in the ArrayList.
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Employee employee = new Employee(
                    ds.getKey(),
                    ds.child("firstName").getValue(String.class),
                    ds.child("lastName").getValue(String.class),
                    ds.child("username").getValue(String.class),
                    ds.child("password").getValue(String.class),
                    ds.child("role").getValue(String.class)
            );
            employees.add(employee);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
