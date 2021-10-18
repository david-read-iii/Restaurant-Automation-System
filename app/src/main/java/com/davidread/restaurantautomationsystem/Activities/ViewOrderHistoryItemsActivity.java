package com.davidread.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.davidread.restaurantautomationsystem.Adapters.OrderItemsBaseAdapter;
import com.davidread.restaurantautomationsystem.Firebase.ValueEventListeners.OrderItemsValueEventListener;
import com.davidread.restaurantautomationsystem.Models.MenuItemWithQuantity;
import com.davidread.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOrderHistoryItemsActivity extends AppCompatActivity {

    private String key;
    private DatabaseReference databaseReference;
    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory;
    private OrderItemsBaseAdapter baseAdapter;
    private ExpandableListView listView;
    private TextView textViewEmpty;
    private OrderItemsValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        // Receive the key of the selected Order object.
        Intent intent = getIntent();

        key = intent.getStringExtra("key");

        // Initialize DatabaseReference and ChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize ArrayList, HashMap, and BaseAdapter.
        categories = new ArrayList<>();
        menuItemsWithQuantityByCategory = new HashMap<>();
        baseAdapter = new OrderItemsBaseAdapter(this, categories, menuItemsWithQuantityByCategory);

        // Initialize ValueEventListener.
        valueEventListener = new OrderItemsValueEventListener(categories, menuItemsWithQuantityByCategory, baseAdapter, databaseReference, key);

        /* Attach ValueEventListener to the orderedMenuItemsWithQuantity attribute of the selected
         * Order object in the OrderQueue collection to sync its current state with the categories
         * ArrayList and menuItemsWithQuantityByCategory HashMap. */
        databaseReference.child("CompletedOrders").child(key).child("orderedMenuItemsWithQuantity").addValueEventListener(valueEventListener);

        // Bring XML elements to Java.
        listView = findViewById(R.id.list_view_order_items);
        textViewEmpty = findViewById(R.id.text_view_order_items_empty);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);
    }
}
