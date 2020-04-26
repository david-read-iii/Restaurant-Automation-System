package com.read.restaurantautomationsystem.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Adapters.EmployeesBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.EmployeesValueEventListener;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ManageEmployeesFragment extends Fragment {

    private ListView listView;
    private TextView textViewEmpty;
    private ArrayList<Employee> employees;
    private EmployeesBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private EmployeesValueEventListener valueEventListener;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_employees, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_manage_employees);
        textViewEmpty = rootView.findViewById(R.id.text_view_manage_employees_empty);

        // Initialize ArrayList and EmployeesBaseAdapter.
        employees = new ArrayList<>();
        baseAdapter = new EmployeesBaseAdapter(rootView.getContext(), employees);

        // Initialize DatabaseReference and EmployeesValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new EmployeesValueEventListener(employees, baseAdapter, textViewEmpty);

        // Set adapter of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        return rootView;
    }

    /**
     * When this fragment is in the foreground, start syncing the ListView with Employee objects
     * from the database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Add EmployeesValueEventListener to part of database that contains Employee objects.
        databaseReference.child("Employees").addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach EmployeesValueEventListener from the database.
        databaseReference.child("Employees").removeEventListener(valueEventListener);
    }
}