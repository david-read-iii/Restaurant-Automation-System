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
import com.read.restaurantautomationsystem.Adapters.ViewEmployeeLogBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.ViewEmployeeLogValueEventListener;
import com.read.restaurantautomationsystem.Models.Log;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ViewEmployeeLogFragment extends Fragment {

    private ArrayList<Log> logs;
    private ViewEmployeeLogBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private ViewEmployeeLogValueEventListener valueEventListener;
    private ListView listView;
    private TextView textViewEmpty;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_view_employee_log, container, false);

        // Initialize ArrayList and BaseAdapter.
        logs = new ArrayList<>();
        baseAdapter = new ViewEmployeeLogBaseAdapter(rootView.getContext(), logs);

        // Initialize DatabaseReference and ValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new ViewEmployeeLogValueEventListener(logs, baseAdapter);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_view_employee_log);
        textViewEmpty = rootView.findViewById(R.id.text_view_view_employee_log_empty);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        return rootView;
    }

    /**
     * When this fragment is in the foreground, start syncing the ListView with the Log objects
     * in the database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Add ValueEventListener to part of database that contains Order objects.
        databaseReference.child("Log").orderByKey().addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach ValueEventListener from the database.
        databaseReference.child("Log").removeEventListener(valueEventListener);
    }
}