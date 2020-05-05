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
import com.read.restaurantautomationsystem.Activities.AddOrderActivity;
import com.read.restaurantautomationsystem.Adapters.ServeTablesBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.ServeTablesChildEventListener;
import com.read.restaurantautomationsystem.Firebase.Helpers.LogFirebaseHelper;
import com.read.restaurantautomationsystem.Firebase.Helpers.TablesFirebaseHelper;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.TablesValueEventListener;
import com.read.restaurantautomationsystem.Models.Log;
import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.Date;

public class ServeTablesFragment extends Fragment {

    private String loggedInEmployeeFirstName, loggedInEmployeeLastName;
    private ArrayList<Table> tables;
    private ServeTablesBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private TablesValueEventListener valueEventListener;
    private Context context;
    private ListView listView;
    private TextView textViewEmpty;
    private ChildEventListener childEventListener;
    private AlertDialog tableOptionsDialog, tableStatusDialog;
    private Table selected;
    private int modified;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_serve_tables, container, false);

        // Receive first and last name attributes of the logged in Employee
        loggedInEmployeeFirstName = getArguments().getString("firstName");
        loggedInEmployeeLastName = getArguments().getString("lastName");

        // Initialize ArrayList and BaseAdapter.
        tables = new ArrayList<>();
        baseAdapter = new ServeTablesBaseAdapter(rootView.getContext(), tables);

        // Initialize DatabaseReference and ValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new TablesValueEventListener(tables, baseAdapter);

        // Make Context globally accessible.
        context = rootView.getContext();

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_serve_tables);
        textViewEmpty = rootView.findViewById(R.id.text_view_serve_tables_empty);

        // Set adapter and empty view of ListView.
        listView.setAdapter(baseAdapter);
        listView.setEmptyView(textViewEmpty);

        // Define ListView clicks.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Retrieve attributes of selected Table object.
                selected = (Table) baseAdapter.getItem(i);

                // Initialize AlertDialog tableOptionsDialog.
                initializeTableOptionsDialog();

                // Initialize and attach ChildEventListener.
                childEventListener = new ServeTablesChildEventListener(rootView.getContext(), tableOptionsDialog, tableStatusDialog, getString(R.string.toast_selected_table_modified), getString(R.string.toast_selected_table_deleted), selected.getKey(), databaseReference);
                databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(childEventListener);

                // Show AlertDialog tableOptionsDialog.
                tableOptionsDialog.show();
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
     * When this fragment is taken out of the foreground, stop syncing the ListView. Also, remove
     * any ChildEventListener set and hide any dialogs on screen.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach ValueEventListener from the database.
        databaseReference.child("Tables").removeEventListener(valueEventListener);

        // Detach ChildEventListener from the database, if one is set.
        if (childEventListener != null) {
            databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);
        }

        // Close any AlertDialogs on screen, if any.
        if (tableOptionsDialog != null) {
            tableOptionsDialog.hide();
        }

        if (tableStatusDialog != null) {
            tableStatusDialog.hide();
        }
    }

    /**
     * Initializes the AlertDialog tableOptionsDialog. Will contain a single choice list of options,
     * where one option will start another AlertDialog allowing the user to specify the status of
     * the selected Table object, and the other option will start the TableOrderActivity allowing
     * the user to specify the attributes of a new Order object.
     */
    private void initializeTableOptionsDialog() {

        // Create new AlertDialog.Builder object to specify the attributes of the AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set title of AlertDialog.
        builder.setTitle(getString(R.string.dialog_table_options_title));

        // Set options of AlertDialog and define option clicks.
        builder.setItems(R.array.dialog_table_options_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {

                    // Define option click for "Update Status".
                    case 0:
                        // Initialize and show the AlertDialog tableStatusDialog.
                        initializeTableStatusDialog();
                        tableStatusDialog.show();
                        break;

                    // Define option click for "Take Order".
                    case 1:

                        // Detach ChildEventListener.
                        databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

                        // If the selected Table has an "occupied" status, start the TableOrderActivity.
                        if (selected.getStatus().equals("Occupied")) {
                            Intent intent = new Intent(context, AddOrderActivity.class);

                            // Pass the attributes of the selected Table object to the activity.
                            intent.putExtra("key", selected.getKey());
                            intent.putExtra("name", selected.getName());
                            intent.putExtra("status", selected.getStatus());

                            // Pass the first and last name attributes of the logged in Employee to the activity.
                            intent.putExtra("firstName", loggedInEmployeeFirstName);
                            intent.putExtra("lastName", loggedInEmployeeLastName);

                            context.startActivity(intent);
                        }
                        // If the select Table has any other status, print a Toast.
                        else {
                            Toast.makeText(context, R.string.toast_update_table_status_to_occupied, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        // Define cancel clicks of the AlertDialog.
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Detach ChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);
            }
        });

        // Initialize the AlertDialog with the attributes specified by the AlertDialog.Builder object.
        tableOptionsDialog = builder.create();
    }

    /**
     * Initializes the AlertDialog tableStatusDialog. Will contain a radio button list of statuses
     * that the user may set upon a Table object.
     */
    private void initializeTableStatusDialog() {

        // Create new AlertDialog.Builder object to specify the attributes of the AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set title of the AlertDialog.
        builder.setTitle(R.string.dialog_table_status_title);

        // Determine which option of the AlertDialog to initially check.
        final String[] statusOptions = getResources().getStringArray(R.array.dialog_table_status_options);
        int checkedOption = -1;

        for (int i = 0; i < statusOptions.length; i++) {
            if (selected.getStatus().equals(statusOptions[i])) {
                checkedOption = i;
            }
        }

        // Set options of AlertDialog.
        builder.setSingleChoiceItems(R.array.dialog_table_status_options, checkedOption, null);

        // Define positive button clicks of AlertDialog.
        builder.setPositiveButton(R.string.dialog_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Detach ChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

                // Determine which option of the AlertDialog is currently checked.
                int checkedOption = ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();
                String status = statusOptions[checkedOption];

                // Modify the Table object with the new status.
                modified = TablesFirebaseHelper.modify(selected.getKey(), new Table(
                        selected.getName(),
                        status
                ));

                // Log the logged in Employee's activity in the database if modification is successful.
                if (modified == 0) {
                    LogFirebaseHelper.save(new Log(
                            getString(R.string.log_user_changed_table_status, loggedInEmployeeFirstName, loggedInEmployeeLastName, selected.getName(), status),
                            new Date()
                    ));
                }
                // Print Toast if modification fails.
                if (modified == 1) {
                    Toast.makeText(context, R.string.toast_update_table_status_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Define negative button clicks of AlertDialog.
        builder.setNegativeButton(R.string.dialog_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Detach ChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);
            }
        });

        // Define cancel clicks of AlertDialog.
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Detach ChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);
            }
        });

        // Initialize the AlertDialog with the attributes specified by the AlertDialog.Builder object.
        tableStatusDialog = builder.create();
    }

}