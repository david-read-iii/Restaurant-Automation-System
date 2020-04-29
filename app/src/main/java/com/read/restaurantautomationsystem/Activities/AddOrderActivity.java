package com.read.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Adapters.AddOrderBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.AddOrderChildEventListener;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.GenericChildEventListener;
import com.read.restaurantautomationsystem.Firebase.Helpers.OrdersFirebaseHelper;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.AddOrderValueEventListener;
import com.read.restaurantautomationsystem.Models.MenuItemWithQuantity;
import com.read.restaurantautomationsystem.Models.Order;
import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddOrderActivity extends AppCompatActivity {

    private Table selected;
    private DatabaseReference databaseReference;
    private GenericChildEventListener tableChildEventListener;
    private AddOrderChildEventListener menuItemsChildEventListener;
    private ExpandableListView listView;
    private TextView textViewEmpty, textViewTotalPrice;
    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory;
    private AddOrderBaseAdapter baseAdapter;
    private AddOrderValueEventListener valueEventListener;
    private int saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        // TODO: Add empty TextView to activity_add_order.xml

        // Retrieve attributes of selected Table object.
        Intent intent = getIntent();

        selected = new Table(
                intent.getStringExtra("key"),
                intent.getStringExtra("name"),
                intent.getStringExtra("status")
        );

        // Initialize DatabaseReference and ChildEventListeners.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        tableChildEventListener = new GenericChildEventListener(this, getString(R.string.toast_table_changed));
        menuItemsChildEventListener = new AddOrderChildEventListener(this);

        /* Attach ChildEventListeners. tableChildEventListener will close the activity if the selected
         * Table object is changed in the database. menuItemsChildEventListener will close the activity
         * if any MenuItem objects change in the database. */
        databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(tableChildEventListener);
        databaseReference.child("MenuItems").addChildEventListener(menuItemsChildEventListener);

        // Initialize ArrayList, HashMap, and BaseAdapter.
        categories = new ArrayList<>();
        menuItemsWithQuantityByCategory = new HashMap<>();
        baseAdapter = new AddOrderBaseAdapter(this, categories, menuItemsWithQuantityByCategory);

        // Initialize ValueEventListener.
        valueEventListener = new AddOrderValueEventListener(categories, menuItemsWithQuantityByCategory, baseAdapter, databaseReference);

        // Attach ValueEventListener to the database
        databaseReference.child("MenuItems").addValueEventListener(valueEventListener);

        // Bring XML elements to Java.
        listView = findViewById(R.id.list_view_add_order);
        textViewEmpty = null; // TODO: Set textViewEmpty.
        textViewTotalPrice = findViewById(R.id.text_view_add_order_total_price);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        // TODO: Set empty view of list view.

        // Define ListView clicks.
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                long viewId = view.getId();

                // Retrieve attributes of selected MenuItemWithQuantity object.
                MenuItemWithQuantity selected = (MenuItemWithQuantity) baseAdapter.getChild(i, i1);

                // Change the quantity attribute of the selected MenuItemWithQuantity object in the ArrayList.
                if (viewId == R.id.button_menu_item_with_quantity_increment) {
                    menuItemsWithQuantityByCategory.get(categories.get(i)).get(i1).setQuantity(
                            Integer.toString(Integer.parseInt(selected.getQuantity()) + 1)
                    );
                    baseAdapter.notifyDataSetChanged();
                    textViewTotalPrice.setText(Double.toString(getTotalPrice(menuItemsWithQuantityByCategory)));
                } else if (viewId == R.id.button_menu_item_with_quantity_decrement && Integer.parseInt(selected.getQuantity()) > 0) {
                    menuItemsWithQuantityByCategory.get(categories.get(i)).get(i1).setQuantity(
                            Integer.toString(Integer.parseInt(selected.getQuantity()) - 1)
                    );
                    baseAdapter.notifyDataSetChanged();
                    textViewTotalPrice.setText(Double.toString(getTotalPrice(menuItemsWithQuantityByCategory)));
                }

                return false;
            }
        });
    }

    /**
     * When this activity leaves the foreground, close this activity.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Detach ChildEventListeners.
        databaseReference.child("Tables").child(selected.getKey()).removeEventListener(tableChildEventListener);
        databaseReference.child("MenuItems").removeEventListener(menuItemsChildEventListener);

        // Close this activity.
        finish();
    }

    /**
     * Specify Toolbar actions.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_order_menu, menu);
        return true;
    }

    /**
     * Define what to do in response to clicks in the Toolbar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit_order) {
            
            ArrayList<MenuItemWithQuantity> orderedItems = unpackOrderedItemsIntoArrayList(menuItemsWithQuantityByCategory);
            
            if (orderedItems.isEmpty()) {
                Toast.makeText(this, R.string.toast_ordered_items_empty, Toast.LENGTH_SHORT).show();
            } else {
                // Create SimpleDateFormat object to format Date objects into strings.
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy - h:mm:ssa");

                // Save an Order object with the specified attributes to the database.
                saved = OrdersFirebaseHelper.save(new Order(
                        getResources().getStringArray(R.array.spinner_options_order_status)[0],
                        Double.toString(getTotalPrice(menuItemsWithQuantityByCategory)),
                        simpleDateFormat.format(new Date()),
                        selected.getName(),
                        orderedItems
                ));

                // Close this activity.
                finish();
            }

            
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns the sum of the total prices of all the MenuItemWithQuantity objects.
     */
    private double getTotalPrice(HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory) {
        double sum = 0.0;
        for (HashMap.Entry<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantity : menuItemsWithQuantityByCategory.entrySet()) {
            for (MenuItemWithQuantity menuItemWithQuantity : menuItemsWithQuantity.getValue()) {
                sum += Double.parseDouble(menuItemWithQuantity.getTotalPrice());
            }
        }
        return sum;
    }

    private ArrayList<MenuItemWithQuantity> unpackOrderedItemsIntoArrayList(HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory) {
        ArrayList<MenuItemWithQuantity> result = new ArrayList<>();

        for (HashMap.Entry<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantity : menuItemsWithQuantityByCategory.entrySet()) {
            for (MenuItemWithQuantity menuItemWithQuantity : menuItemsWithQuantity.getValue()) {
                if (Integer.parseInt(menuItemWithQuantity.getQuantity()) > 0) {
                    result.add(menuItemWithQuantity);
                }
            }
        }

        return result;
    }
}
