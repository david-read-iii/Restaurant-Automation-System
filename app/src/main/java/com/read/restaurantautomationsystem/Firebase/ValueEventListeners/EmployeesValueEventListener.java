package com.read.restaurantautomationsystem.Firebase.ValueEventListeners;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Models.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        // Sort Employee objects in ArrayList alphabetically by last name, then by first name.
        Collections.sort(employees, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                int lastNameCompareValue = e1.getLastName().compareToIgnoreCase(e2.getLastName());

                if (lastNameCompareValue != 0) {
                    return lastNameCompareValue;
                } else {
                    return e1.getFirstName().compareToIgnoreCase(e2.getLastName());
                }
            }
        });

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
