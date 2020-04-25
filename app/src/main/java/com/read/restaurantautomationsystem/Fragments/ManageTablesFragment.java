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
import com.read.restaurantautomationsystem.Adapters.TablesBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.TablesValueEventListener;
import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ManageTablesFragment extends Fragment {

    private ListView listView;
    private TextView textViewEmpty;
    private ArrayList<Table> tables;
    private TablesBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private TablesValueEventListener valueEventListener;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_manage_tables, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_manage_tables);
        textViewEmpty = rootView.findViewById(R.id.text_view_manage_tables_empty);

        // Initialize ArrayList and TablesBaseAdapter.
        tables = new ArrayList<>();
        baseAdapter = new TablesBaseAdapter(rootView.getContext(), tables);

        // Initialize DatabaseReference and TablesValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new TablesValueEventListener(tables, baseAdapter, textViewEmpty);

        // Set adapter of ListView.
        listView.setAdapter(baseAdapter);

        return rootView;
    }

    /**
     * When this fragment is in the foreground, start syncing the ListView with Table objects
     * from the database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Add TablesValueEventListener to part of database that contains Table objects.
        databaseReference.child("Tables").addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach TablesValueEventListener from the database.
        databaseReference.child("Tables").removeEventListener(valueEventListener);
    }
}