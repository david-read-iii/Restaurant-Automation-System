package com.davidread.restaurantautomationsystem.Activities;

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
import com.davidread.restaurantautomationsystem.Adapters.AddOrderBaseAdapter;
import com.davidread.restaurantautomationsystem.Firebase.ChildEventListeners.GenericChildEventListener;
import com.davidread.restaurantautomationsystem.Firebase.Helpers.OrdersFirebaseHelper;
import com.davidread.restaurantautomationsystem.Firebase.ValueEventListeners.MenuItemsWithQuantityValueEventListener;
import com.davidread.restaurantautomationsystem.Models.MenuItemWithQuantity;
import com.davidread.restaurantautomationsystem.Models.Order;
import com.davidread.restaurantautomationsystem.Models.Table;
import com.davidread.restaurantautomationsystem.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddOrderActivity extends AppCompatActivity {

    private String loggedInEmployeeFirstName, loggedInEmployeeLastName;
    private Table selected;
    private DatabaseReference databaseReference;
    private GenericChildEventListener tableChildEventListener;
    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory;
    private AddOrderBaseAdapter baseAdapter;
    private ExpandableListView listView;
    private TextView textViewEmpty, textViewTotalPrice;
    private MenuItemsWithQuantityValueEventListener valueEventListener;
    private int saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        // Retrieve attributes of selected Table object, and the first and last name attributes of the logged in Employee.
        Intent intent = getIntent();

        selected = new Table(
                intent.getStringExtra("key"),
                intent.getStringExtra("name"),
                intent.getStringExtra("status")
        );

        loggedInEmployeeFirstName = intent.getStringExtra("firstName");
        loggedInEmployeeLastName = intent.getStringExtra("lastName");

        // Initialize DatabaseReference and tableChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        tableChildEventListener = new GenericChildEventListener(this, getString(R.string.toast_selected_table_modified), getString(R.string.toast_selected_table_deleted));

        // Attach tableChildEventListener to close this activity if the selected Table object is modified or deleted from the database.
        databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(tableChildEventListener);

        // Initialize ArrayList, HashMap, and BaseAdapter.
        categories = new ArrayList<>();
        menuItemsWithQuantityByCategory = new HashMap<>();
        baseAdapter = new AddOrderBaseAdapter(this, categories, menuItemsWithQuantityByCategory);

        // Initialize ValueEventListener.
        valueEventListener = new MenuItemsWithQuantityValueEventListener(categories, menuItemsWithQuantityByCategory, baseAdapter, databaseReference);

        /* Attach ValueEventListener to the MenuItems collection of the database to sync its current
         * state with the categories ArrayList and menuItemsWithQuantityByCategory HashMap. */
        databaseReference.child("MenuItems").addValueEventListener(valueEventListener);

        // Bring XML elements to Java.
        listView = findViewById(R.id.list_view_add_order);
        textViewEmpty = findViewById(R.id.text_view_add_order_empty);
        textViewTotalPrice = findViewById(R.id.text_view_add_order_total_price);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        // Define ListView clicks.
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                long viewId = view.getId();

                // Create a reference to the selected MenuItemWithQuantity object.
                MenuItemWithQuantity selected = (MenuItemWithQuantity) baseAdapter.getChild(i, i1);

                // Define increment button clicks.
                if (viewId == R.id.button_menu_item_with_quantity_increment) {

                    // Increase quantity and total price attributes of the referenced MenuItemWithQuantity object.
                    selected.setQuantity(selected.getQuantity() + 1);
                    selected.setTotalPrice(selected.getQuantity() * selected.getMenuItem().getPrice());

                    // Notify BaseAdapter to update its ListView.
                    baseAdapter.notifyDataSetChanged();

                    // Create NumberFormat object to format currency attributes.
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

                    // Update textViewTotalPrice with the latest order total price.
                    textViewTotalPrice.setText(currencyFormat.format(calculateOrderTotalPrice(menuItemsWithQuantityByCategory)));
                }

                // Define decrement button clicks.
                else if (viewId == R.id.button_menu_item_with_quantity_decrement && selected.getQuantity() > 0) {

                    // Decrease quantity and total price attributes of the referenced MenuItemWithQuantity object.
                    selected.setQuantity(selected.getQuantity() - 1);
                    selected.setTotalPrice(selected.getQuantity() * selected.getMenuItem().getPrice());

                    // Notify BaseAdapter to update its ListView.
                    baseAdapter.notifyDataSetChanged();

                    // Create NumberFormat object to format currency attributes.
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

                    // Update textViewTotalPrice with the latest order total price.
                    textViewTotalPrice.setText(currencyFormat.format(calculateOrderTotalPrice(menuItemsWithQuantityByCategory)));
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

        // Detach ChildEventListener.
        databaseReference.child("Tables").child(selected.getKey()).removeEventListener(tableChildEventListener);

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

        // Define submit order action click.
        if (id == R.id.action_submit_order) {

            // Unpack the HashMap MenuItemWithQuantity objects into a single ArrayList, excluding objects with a quantity attribute set to 0.
            final ArrayList<MenuItemWithQuantity> orderedItems = unpackHashMapItemsIntoArrayList(menuItemsWithQuantityByCategory);

            // Save an Order object with the specified attributes to the database.
            saved = OrdersFirebaseHelper.save(new Order(
                            0,
                            getResources().getStringArray(R.array.dialog_order_status_options)[0],
                            calculateOrderTotalPrice(orderedItems),
                            new Date(),
                            selected.getName(),
                            orderedItems
                    ),
                    loggedInEmployeeFirstName,
                    loggedInEmployeeLastName,
                    this);

            // If save successful, close this activity.
            if (saved == 0) {
                finish();
            }
            // If save failed due to database error, print Toast.
            else if (saved == 1) {
                Toast.makeText(AddOrderActivity.this, R.string.toast_add_order_failed, Toast.LENGTH_SHORT).show();
            }
            // If save failed due to the object having invalid attributes, print Toast.
            else if (saved == 2) {
                Toast.makeText(AddOrderActivity.this, R.string.toast_order_invalid, Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns the sum of the total prices of the MenuItemWithQuantity objects inside an ArrayList.
     */
    private double calculateOrderTotalPrice(ArrayList<MenuItemWithQuantity> menuItems) {
        // Initialize the resulting sum.
        double sum = 0.0;

        // Iterate through each MenuItemWithQuantity object of the ArrayList.
        for (MenuItemWithQuantity menuItemWithQuantity : menuItems) {
            // Add the total price attribute of the current MenuItemWithQuantity object to that of the resulting sum.
            sum += menuItemWithQuantity.getTotalPrice();
        }

        return sum;
    }

    /**
     * Returns the sum of the total prices of the MenuItemWithQuantity objects inside the ArrayLists
     * of a given HashMap.
     */
    private double calculateOrderTotalPrice(HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory) {
        // Initialize the resulting sum.
        double sum = 0.0;

        // Iterate through each ArrayList stored in the HashMap.
        for (HashMap.Entry<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantity : menuItemsWithQuantityByCategory.entrySet()) {
            // Iterate through each MenuItemWithQuantity object of the selected ArrayList.
            for (MenuItemWithQuantity menuItemWithQuantity : menuItemsWithQuantity.getValue()) {
                // Add the total price attribute of the current MenuItemWithQuantity object to that of the resulting sum.
                sum += menuItemWithQuantity.getTotalPrice();
            }
        }

        return sum;
    }

    /**
     * Returns a single ArrayList of all MenuItemWithQuantity objects excluding objects with a quantity
     * attribute set to 0, given a HashMap of several ArrayLists of MenuItemWithQuantity objects.
     */
    private ArrayList<MenuItemWithQuantity> unpackHashMapItemsIntoArrayList(HashMap<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantityByCategory) {
        // Create the resulting ArrayList.
        ArrayList<MenuItemWithQuantity> result = new ArrayList<>();

        // Iterate through each ArrayList stored in the HashMap.
        for (HashMap.Entry<String, ArrayList<MenuItemWithQuantity>> menuItemsWithQuantity : menuItemsWithQuantityByCategory.entrySet()) {
            // Iterate through each MenuItemWithQuantity object of the selected ArrayList.
            for (MenuItemWithQuantity menuItemWithQuantity : menuItemsWithQuantity.getValue()) {
                // If the current MenuItemWithQuantity object has a quantity attribute greater than 0, add it to the resulting ArrayList.
                if (menuItemWithQuantity.getQuantity() > 0) {
                    result.add(menuItemWithQuantity);
                }
            }
        }

        return result;
    }
}
