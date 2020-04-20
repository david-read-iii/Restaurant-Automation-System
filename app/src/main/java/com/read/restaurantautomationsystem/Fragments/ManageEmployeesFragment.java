package com.read.restaurantautomationsystem.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Adapters.EmployeesBaseAdapter;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ManageEmployeesFragment extends Fragment {

    private ListView listView;
    private TextView listViewEmptyText;
    private ArrayList<Employee> employees;
    private EmployeesBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    /**
     * When this fragment is created, inflate a layout containing a ListView. The data source of the
     * ListView will be an ArrayList of Employee objects. The adapter of the ListView will be a
     * custom BaseAdapter.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_employees, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.listview_employees);
        listViewEmptyText = rootView.findViewById(R.id.listview_employees_empty_text);

        // Initialize ArrayList.
        employees = new ArrayList<>();

        // Initialize the custom BaseAdapter with context from the MainActivity and a reference to the ArrayList.
        baseAdapter = new EmployeesBaseAdapter(rootView.getContext(), employees);

        // Attach the ListView to the BaseAdapter.
        listView.setAdapter(baseAdapter);

        return rootView;
    }

    /**
     * When this fragment is in the foreground, sync the ListView with Employee objects from the
     * database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Initialize the DatabaseReference.
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Define the ValueEventListener.
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the ArrayList.
                employees.clear();

                // Populate the ArrayList with Employee objects from the database.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Employee employee = ds.getValue(Employee.class);
                    employees.add(employee);
                }

                // Notify the BaseAdapter to update its ListView.
                baseAdapter.notifyDataSetChanged();

                // If the ArrayList is empty, display ListView empty text.
                if (employees.isEmpty()) {
                    listViewEmptyText.setVisibility(View.VISIBLE);
                } else {
                    listViewEmptyText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        // Attach the ValueEventListener to the part of the database containing Employee objects.
        databaseReference.child("Employees").addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach ValueEventListener from the database.
        databaseReference.child("Employees").removeEventListener(valueEventListener);
    }
}