package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Adapters.EmployeesBaseAdapter;
import com.read.restaurantautomationsystem.Models.Employee;

import java.util.ArrayList;

public class EmployeesValueEventListener implements ValueEventListener {

    private ArrayList<Employee> employees;
    private EmployeesBaseAdapter baseAdapter;
    private TextView textViewEmpty;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with Employee objects in the database.
     * When the ArrayList is synced, a given BaseAdapter is notified so that it can update its
     * ListView. When the ArrayList is empty, a given TextView is notified so that it can display
     * text that indicates so.
     */
    public EmployeesValueEventListener(ArrayList<Employee> employees, EmployeesBaseAdapter baseAdapter, TextView textViewEmpty) {
        this.employees = employees;
        this.baseAdapter = baseAdapter;
        this.textViewEmpty = textViewEmpty;
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

        // Set visibility of the empty TextView depending on if the ArrayList is empty.
        if (employees.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
