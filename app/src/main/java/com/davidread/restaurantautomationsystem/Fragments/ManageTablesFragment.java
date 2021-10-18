package com.davidread.restaurantautomationsystem.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.davidread.restaurantautomationsystem.Activities.ModifyTableActivity;
import com.davidread.restaurantautomationsystem.Adapters.ManageTablesBaseAdapter;
import com.davidread.restaurantautomationsystem.Firebase.ValueEventListeners.TablesValueEventListener;
import com.davidread.restaurantautomationsystem.Models.Table;
import com.davidread.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ManageTablesFragment extends Fragment {

    private ListView listView;
    private TextView textViewEmpty;
    private ArrayList<Table> tables;
    private ManageTablesBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private TablesValueEventListener valueEventListener;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_manage_tables, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_manager_manage_tables);
        textViewEmpty = rootView.findViewById(R.id.text_view_manager_manage_tables_empty);

        // Initialize ArrayList and ManagerManageTablesBaseAdapter.
        tables = new ArrayList<>();
        baseAdapter = new ManageTablesBaseAdapter(rootView.getContext(), tables);

        // Initialize DatabaseReference and TablesValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new TablesValueEventListener(tables, baseAdapter);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        // Define ListView clicks.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Retrieve attributes of selected Table object.
                Table selected = (Table) baseAdapter.getItem(i);

                // Start ModifyTableActivity.
                Intent intent = new Intent(rootView.getContext(), ModifyTableActivity.class);

                // Pass the attributes of the selected Table object to the activity.
                intent.putExtra("key", selected.getKey());
                intent.putExtra("name", selected.getName());
                intent.putExtra("status", selected.getStatus());

                rootView.getContext().startActivity(intent);
            }
        });

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