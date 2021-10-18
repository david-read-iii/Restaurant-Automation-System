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
import com.davidread.restaurantautomationsystem.Activities.ModifyInventoryItemActivity;
import com.davidread.restaurantautomationsystem.Adapters.ManageInventoryBaseAdapter;
import com.davidread.restaurantautomationsystem.Firebase.ValueEventListeners.InventoryItemsValueEventListener;
import com.davidread.restaurantautomationsystem.Models.InventoryItem;
import com.davidread.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ManageInventoryFragment extends Fragment {

    private ListView listView;
    private TextView textViewEmpty;
    private ArrayList<InventoryItem> inventoryItems;
    private ManageInventoryBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private InventoryItemsValueEventListener valueEventListener;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_manage_inventory, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_manage_inventory);
        textViewEmpty = rootView.findViewById(R.id.text_view_manage_inventory_empty);

        // Initialize ArrayList and ManageInventoryBaseAdapter.
        inventoryItems = new ArrayList<>();
        baseAdapter = new ManageInventoryBaseAdapter(rootView.getContext(), inventoryItems);

        // Initialize DatabaseReference and InventoryItemsValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new InventoryItemsValueEventListener(inventoryItems, baseAdapter);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        // Define ListView clicks.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Retrieve attributes of selected InventoryItem object.
                InventoryItem selected = (InventoryItem) baseAdapter.getItem(i);

                // Start the ModifyInventoryItemActivity.
                Intent intent = new Intent(rootView.getContext(), ModifyInventoryItemActivity.class);

                // Pass the attributes of the selected InventoryItem to the activity.
                intent.putExtra("key", inventoryItems.get(i).getKey());
                intent.putExtra("name", inventoryItems.get(i).getName());
                intent.putExtra("quantity", Integer.toString(inventoryItems.get(i).getQuantity()));

                rootView.getContext().startActivity(intent);
            }
        });

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