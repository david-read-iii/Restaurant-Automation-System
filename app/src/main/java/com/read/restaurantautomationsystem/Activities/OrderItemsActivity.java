package com.read.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Adapters.OrderItemsBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.GenericChildEventListener;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.OrderItemsValueEventListener;
import com.read.restaurantautomationsystem.Models.MenuItemWithQuantity;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItemsActivity extends AppCompatActivity {

    private String key;
    private DatabaseReference databaseReference;
    private GenericChildEventListener childEventListener;
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
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_selected_order_modified), getString(R.string.toast_selected_order_deleted));

        // Attach the ChildEventListener to the selected Order object in the database.
        databaseReference.child("OrderQueue").child(key).addChildEventListener(childEventListener);

        // Initialize ArrayList, HashMap, and BaseAdapter.
        categories = new ArrayList<>();
        menuItemsWithQuantityByCategory = new HashMap<>();
        baseAdapter = new OrderItemsBaseAdapter(this, categories, menuItemsWithQuantityByCategory);

        // Initialize ValueEventListener.
        valueEventListener = new OrderItemsValueEventListener(categories, menuItemsWithQuantityByCategory, baseAdapter, databaseReference, key);

        /* Attach ValueEventListener to the orderedMenuItemsWithQuantity attribute of the selected
         * Order object in the OrderQueue collection to sync its current state with the categories
         * ArrayList and menuItemsWithQuantityByCategory HashMap. */
        databaseReference.child("OrderQueue").child(key).child("orderedMenuItemsWithQuantity").addValueEventListener(valueEventListener);

        // Bring XML elements to Java.
        listView = findViewById(R.id.list_view_order_items);
        textViewEmpty = findViewById(R.id.text_view_order_items_empty);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);
    }

    /**
     * When this activity leaves the foreground, close this activity.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Detach ChildEventListener.
        databaseReference.child("OrderQueue").child(key).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}
