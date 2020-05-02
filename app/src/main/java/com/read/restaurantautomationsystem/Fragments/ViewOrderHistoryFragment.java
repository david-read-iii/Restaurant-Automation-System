package com.read.restaurantautomationsystem.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.read.restaurantautomationsystem.Activities.ViewOrderHistoryDetailActivity;
import com.read.restaurantautomationsystem.Activities.ViewOrderHistoryItemsActivity;
import com.read.restaurantautomationsystem.Adapters.ViewOrderHistoryBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.ViewOrderHistoryValueEventListener;
import com.read.restaurantautomationsystem.Models.Order;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class ViewOrderHistoryFragment extends Fragment {

    private ArrayList<Order> orders;
    private ViewOrderHistoryBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private ViewOrderHistoryValueEventListener valueEventListener;
    private Context context;
    private ListView listView;
    private TextView textViewEmpty;
    private AlertDialog optionsDialog;
    private Order selected;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_view_order_history, container, false);

        // Initialize ArrayList and BaseAdapter.
        orders = new ArrayList<>();
        baseAdapter = new ViewOrderHistoryBaseAdapter(rootView.getContext(), orders);

        // Initialize DatabaseReference and ValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new ViewOrderHistoryValueEventListener(orders, baseAdapter);

        // Make Context globally accessible.
        context = rootView.getContext();

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_view_order_history);
        textViewEmpty = rootView.findViewById(R.id.text_view_view_order_history_empty);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        // Define ListView clicks.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Retrieve attributes of selected Order object.
                selected = (Order) baseAdapter.getItem(i);

                // Initialize AlertDialog optionsDialog.
                initializeOptionsDialog();

                // Show AlertDialog optionsDialog.
                optionsDialog.show();
            }
        });
        return rootView;
    }

    /**
     * When this fragment is in the foreground, start syncing the ListView with the Order objects
     * in the CompletedOrders collection of the database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Add ValueEventListener to part of database that contains Order objects.
        databaseReference.child("CompletedOrders").addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach ValueEventListener from the database.
        databaseReference.child("CompletedOrders").removeEventListener(valueEventListener);
    }

    /**
     * Initializes the AlertDialog optionsDialog. Will contain a single choice list of options,
     * where one option will start the ViewOrderHistoryItemsActivity and one will start the
     * ViewOrderHistoryDetailActivity.
     */
    private void initializeOptionsDialog() {

        // Create new AlertDialog.Builder object to specify the attributes of the AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set title of AlertDialog.
        builder.setTitle(getString(R.string.dialog_order_history_title));

        // Set options of AlertDialog and define option clicks.
        builder.setItems(R.array.dialog_order_history_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {

                    // Define option click for "View Items".
                    case 0:

                        // Start the OrderItemsActivity.
                        Intent intent = new Intent(context, ViewOrderHistoryItemsActivity.class);

                        // Pass the key of the selected Order object to the activity.
                        intent.putExtra("key", selected.getKey());

                        context.startActivity(intent);

                        break;

                    // Define option click for "View Details".
                    case 1:

                        // Start the OrderDetailActivity.
                        Intent intent1 = new Intent(context, ViewOrderHistoryDetailActivity.class);

                        // Pass the attributes of the selected Order object to the activity.
                        intent1.putExtra("key", selected.getKey());
                        intent1.putExtra("number", selected.getNumber());
                        intent1.putExtra("status", selected.getStatus());
                        intent1.putExtra("totalPrice", selected.getTotalPrice());
                        intent1.putExtra("dateTimeOrdered", selected.getDateTimeOrdered().getTime());
                        intent1.putExtra("tableNameOrdered", selected.getTableNameOrdered());

                        context.startActivity(intent1);

                        break;
                }
            }
        });

        // Initialize the AlertDialog with the attributes specified by the AlertDialog.Builder object.
        optionsDialog = builder.create();
    }
}
