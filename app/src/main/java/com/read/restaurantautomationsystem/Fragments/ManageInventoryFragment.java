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
import com.read.restaurantautomationsystem.Adapters.InventoryItemsBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.InventoryItemsValueEventListener;
import com.read.restaurantautomationsystem.Models.InventoryItem;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ManageInventoryFragment extends Fragment {

    private ListView listView;
    private TextView textViewEmpty;
    private ArrayList<InventoryItem> inventoryItems;
    private InventoryItemsBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private InventoryItemsValueEventListener valueEventListener;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_inventory, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_manage_inventory);
        textViewEmpty = rootView.findViewById(R.id.text_view_manage_inventory_empty);

        // Initialize ArrayList and InventoryItemsBaseAdapter.
        inventoryItems = new ArrayList<>();
        baseAdapter = new InventoryItemsBaseAdapter(rootView.getContext(), inventoryItems);

        // Initialize DatabaseReference and InventoryItemsValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new InventoryItemsValueEventListener(inventoryItems, baseAdapter, textViewEmpty);

        // Set adapter of ListView.
        listView.setAdapter(baseAdapter);

        return rootView;
    }

    /**
     * When this fragment is in the foreground, start syncing the ListView with InventoryItem objects
     * from the database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Add InventoryItemsValueEventListener to part of database that contains InventoryItem objects.
        databaseReference.child("InventoryItems").addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach InventoryItemsValueEventListener from the database.
        databaseReference.child("InventoryItems").removeEventListener(valueEventListener);
    }
}