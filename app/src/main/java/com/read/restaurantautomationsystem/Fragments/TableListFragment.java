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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Activities.TableOrderActivity;
import com.read.restaurantautomationsystem.Adapters.TableListBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.DialogChildEventListener;
import com.read.restaurantautomationsystem.Firebase.Helpers.TablesFirebaseHelper;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.TablesValueEventListener;
import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class TableListFragment extends Fragment {

    private ListView listView;
    private TextView textViewEmpty;
    private ArrayList<Table> tables;
    private TableListBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private TablesValueEventListener valueEventListener;
    private DialogChildEventListener childEventListener;
    private AlertDialog tableOptionsDialog, tableStatusDialog;
    private int modified;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_table_list, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_table_list);
        textViewEmpty = rootView.findViewById(R.id.text_view_table_list_empty);

        // Initialize ArrayList and TableListBaseAdapter.
        tables = new ArrayList<>();
        baseAdapter = new TableListBaseAdapter(rootView.getContext(), tables);

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
                Table table = (Table) baseAdapter.getItem(i);

                // Initialize AlertDialog tableOptionsDialog.
                initializeTableOptionsDialog(rootView.getContext(), table);

                // Initialize and attach DialogChildEventListener to the selected Table object in the database.
                childEventListener = new DialogChildEventListener(rootView.getContext(), tableOptionsDialog, getString(R.string.toast_table_changed));
                databaseReference.child("Tables").child(table.getKey()).addChildEventListener(childEventListener);

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
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach TablesValueEventListener from the database.
        databaseReference.child("Tables").removeEventListener(valueEventListener);

        // Detach DialogChildEventListener from the database.
        if (childEventListener != null) {
            databaseReference.child("Tables").removeEventListener(childEventListener);
        }

        // Close any AlertDialogs on screen.
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
    private void initializeTableOptionsDialog(final Context context, final Table table) {

        // Create new AlertDialog.Builder object to specify the attributes of the AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set title of AlertDialog.
        builder.setTitle(getString(R.string.dialog_table_list_title));

        // Set options of AlertDialog and define option clicks.
        builder.setItems(R.array.dialog_options_table_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Detach DialogChildEventListener.
                databaseReference.child("Tables").child(table.getKey()).removeEventListener(childEventListener);

                switch (i) {
                    // "Update Status" clicked. Start AlertDialog tableStatusDialog.
                    case 0:

                        // Initialize AlertDialog tableStatusDialog.
                        initializeTableStatusDialog(context, table);

                        // Initialize and attach DialogChildEventListener to the selected Table object in the database.
                        childEventListener = new DialogChildEventListener(context, tableStatusDialog, getString(R.string.toast_table_changed));
                        databaseReference.child("Tables").child(table.getKey()).addChildEventListener(childEventListener);

                        // Show AlertDialog tableStatusDialog.
                        tableStatusDialog.show();

                        break;
                    // "Take Order" clicked. Start TableOrderActivity.
                    case 1:
                        Intent intent = new Intent(context, TableOrderActivity.class);

                        // Pass the attributes of the selected Table object to the activity.
                        intent.putExtra("key", table.getKey());
                        intent.putExtra("name", table.getName());
                        intent.putExtra("status", table.getStatus());

                        context.startActivity(intent);

                        break;
                }
            }
        });

        // Define cancel clicks of tableOptionsDialog.
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Detach DialogChildEventListener.
                if (childEventListener != null) {
                    databaseReference.child("Tables").child(table.getKey()).removeEventListener(childEventListener);
                }
            }
        });

        // Initialize the AlertDialog tableOptionsDialog with the attributes specified by the AlertDialog.Builder object.
        tableOptionsDialog = builder.create();
    }

    /**
     * Initializes the AlertDialog tableStatusDialog. Will contain a ratio button list of statuses
     * that the user may set upon a Table object.
     */
    private void initializeTableStatusDialog(final Context context, final Table table) {

        // Create new AlertDialog.Builder object to specify the attributes of the AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set title of the AlertDialog.
        builder.setTitle(R.string.dialog_table_status_title);

        // Determine which option of the AlertDialog to initially check.
        final String[] statusOptions = getResources().getStringArray(R.array.dialog_options_table_status);

        int checkedOption = -1;
        for (int i = 0; i < statusOptions.length; i++) {
            if (table.getStatus().equals(statusOptions[i])) {
                checkedOption = i;
            }
        }

        final int finalCheckedOption = checkedOption;

        // Set options of AlertDialog.
        builder.setSingleChoiceItems(R.array.dialog_options_table_status, finalCheckedOption, null);

        // Define positive button clicks of AlertDialog.
        builder.setPositiveButton(R.string.dialog_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Detach DialogChildEventListener.
                databaseReference.child("Tables").child(table.getKey()).removeEventListener(childEventListener);

                // Determine which option of the AlertDialog is currently checked. The checked option determines the new status.
                int checkedOption = ((AlertDialog)dialogInterface).getListView().getCheckedItemPosition();
                String status = statusOptions[checkedOption];

                // Modify the Table object with the new status.
                modified = TablesFirebaseHelper.modify(table, new Table(
                        table.getName(),
                        status
                ));

                // Print Toast if modification fails.
                if (modified == 1) {
                    Toast.makeText(context, R.string.toast_update_status_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Define negative button clicks of AlertDialog.
        builder.setNegativeButton(R.string.dialog_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Detach DialogChildEventListener.
                if (childEventListener != null) {
                    databaseReference.child("Tables").child(table.getKey()).removeEventListener(childEventListener);
                }
            }
        });

        // Define cancel clicks of AlertDialog.
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Detach DialogChildEventListener.
                if (childEventListener != null) {
                    databaseReference.child("Tables").child(table.getKey()).removeEventListener(childEventListener);
                }
            }
        });

        // Initialize the AlertDialog with the attributes specified by the AlertDialog.Builder object.
        tableStatusDialog = builder.create();
    }

}