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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Activities.OrderDetailActivity;
import com.read.restaurantautomationsystem.Activities.OrderItemsActivity;
import com.read.restaurantautomationsystem.Adapters.OrderQueueBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.OrderQueueChildEventListener;
import com.read.restaurantautomationsystem.Firebase.Helpers.OrdersFirebaseHelper;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.OrderQueueValueEventListener;
import com.read.restaurantautomationsystem.Models.Order;
import com.read.restaurantautomationsystem.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderQueueFragment extends Fragment {

    private ArrayList<Order> orders;
    private OrderQueueBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private OrderQueueValueEventListener valueEventListener;
    private Context context;
    private ListView listView;
    private TextView textViewEmpty;
    private ChildEventListener childEventListener;
    private AlertDialog orderOptionsDialog, orderStatusDialog;
    private Order selected;
    private int modified;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_order_queue, container, false);

        // Initialize ArrayList and BaseAdapter.
        orders = new ArrayList<>();
        baseAdapter = new OrderQueueBaseAdapter(rootView.getContext(), orders);

        // Initialize DatabaseReference and ValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new OrderQueueValueEventListener(orders, baseAdapter);

        // Make Context globally accessible.
        context = rootView.getContext();

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_order_queue);
        textViewEmpty = rootView.findViewById(R.id.text_view_order_queue_empty);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        // Define ListView clicks.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Retrieve attributes of selected Order object.
                selected = (Order) baseAdapter.getItem(i);

                // Initialize AlertDialog orderOptionsDialog.
                initializeOrderOptionsDialog();

                // Initialize and attach ChildEventListener.
                childEventListener = new OrderQueueChildEventListener(context, orderOptionsDialog, orderStatusDialog, getString(R.string.toast_selected_order_modified), getString(R.string.toast_selected_order_deleted), selected.getKey(), databaseReference);
                databaseReference.child("OrderQueue").child(selected.getKey()).addChildEventListener(childEventListener);

                // Show AlertDialog orderOptionsDialog.
                orderOptionsDialog.show();
            }
        });
        return rootView;
    }

    /**
     * When this fragment is in the foreground, start syncing the ListView with the Order objects
     * in the OrderQueue collection of the database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Add ValueEventListener to part of database that contains Order objects.
        databaseReference.child("OrderQueue").addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView. Also, remove
     * any ChildEventListener set and hide any dialogs on screen.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach ValueEventListener from the database.
        databaseReference.child("OrderQueue").removeEventListener(valueEventListener);

        // Detach ChildEventListener from the database, if one is set.
        if (childEventListener != null) {
            databaseReference.child("OrderQueue").child(selected.getKey()).removeEventListener(childEventListener);
        }

        // Close any AlertDialogs on screen, if any.
        if (orderOptionsDialog != null) {
            orderOptionsDialog.hide();
        }
        if (orderStatusDialog != null) {
            orderStatusDialog.hide();
        }
    }

    /**
     * Initializes the AlertDialog orderOptionsDialog. Will contain a single choice list of options,
     * where one option will start another AlertDialog allowing the user to specify the status of
     * the selected Order object, and the other option will start the OrderDetailActivity allowing
     * the user to view the more detailed attributes of the Order object.
     */
    private void initializeOrderOptionsDialog() {

        // Create new AlertDialog.Builder object to specify the attributes of the AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set title of AlertDialog.
        builder.setTitle(getString(R.string.dialog_order_options_title));

        // Set options of AlertDialog and define option clicks.
        builder.setItems(R.array.dialog_order_options_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {

                    // Define option click for "Update Status".
                    case 0:
                        // Initialize and show the AlertDialog orderStatusDialog.
                        initializeOrderStatusDialog();
                        orderStatusDialog.show();
                        break;

                    // Define option click for "View Items".
                    case 1:

                        // Detach ChildEventListener.
                        databaseReference.child("OrderQueue").child(selected.getKey()).removeEventListener(childEventListener);

                        // Start the OrderItemsActivity.
                        Intent intent = new Intent(context, OrderItemsActivity.class);

                        // Pass the key of the selected Order object to the activity.
                        intent.putExtra("key", selected.getKey());

                        context.startActivity(intent);

                        break;

                    // Define option click for "View Details".
                    case 2:

                        // Detach ChildEventListener.
                        databaseReference.child("OrderQueue").child(selected.getKey()).removeEventListener(childEventListener);

                        // Start the OrderDetailActivity.
                        Intent intent1 = new Intent(context, OrderDetailActivity.class);

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

        // Define cancel clicks of the AlertDialog.
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Detach ChildEventListener.
                databaseReference.child("OrderQueue").child(selected.getKey()).removeEventListener(childEventListener);
            }
        });

        // Initialize the AlertDialog with the attributes specified by the AlertDialog.Builder object.
        orderOptionsDialog = builder.create();
    }

    /**
     * Initializes the AlertDialog orderStatusDialog. Will contain a radio button list of statuses
     * that the user may set upon an Order object.
     */
    private void initializeOrderStatusDialog() {

        // Create new AlertDialog.Builder object to specify the attributes of the AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set title of the AlertDialog.
        builder.setTitle(R.string.dialog_order_status_title);

        // Determine which option of the AlertDialog to initially check.
        final String[] statusOptions = getResources().getStringArray(R.array.dialog_order_status_options);
        int checkedOption = -1;

        for (int i = 0; i < statusOptions.length; i++) {
            if (selected.getStatus().equals(statusOptions[i])) {
                checkedOption = i;
            }
        }

        // Set options of AlertDialog.
        builder.setSingleChoiceItems(R.array.dialog_order_status_options, checkedOption, null);

        // Define positive button clicks of AlertDialog.
        builder.setPositiveButton(R.string.dialog_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Detach ChildEventListener.
                databaseReference.child("OrderQueue").child(selected.getKey()).removeEventListener(childEventListener);

                // Determine which option of the AlertDialog is currently checked.
                int checkedOption = ((AlertDialog)dialogInterface).getListView().getCheckedItemPosition();
                String status = statusOptions[checkedOption];

                // Modify the Order object with the new status.
                modified = OrdersFirebaseHelper.modifyStatus(selected.getKey(), status);

                // Print Toast if modification fails.
                if (modified == 1) {
                    Toast.makeText(context, R.string.toast_update_order_status_failed, Toast.LENGTH_SHORT).show();
                }

                /* If the status will be set to "completed", move the Order object from the
                 * OrderQueue collection to the CompletedOrders collection of the database. */
                if (status.equals("Completed")) {
                    OrdersFirebaseHelper.moveToCompletedOrders(selected.getKey());
                }
            }
        });

        // Define negative button clicks of AlertDialog.
        builder.setNegativeButton(R.string.dialog_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Detach ChildEventListener.
                databaseReference.child("OrderQueue").child(selected.getKey()).removeEventListener(childEventListener);
            }
        });

        // Define cancel clicks of AlertDialog.
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Detach ChildEventListener.
                databaseReference.child("OrderQueue").child(selected.getKey()).removeEventListener(childEventListener);
            }
        });

        // Initialize the AlertDialog with the attributes specified by the AlertDialog.Builder object.
        orderStatusDialog = builder.create();
    }
}