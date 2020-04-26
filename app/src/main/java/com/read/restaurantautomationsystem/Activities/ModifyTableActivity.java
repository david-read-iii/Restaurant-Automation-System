package com.read.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.GenericChildEventListener;
import com.read.restaurantautomationsystem.Firebase.Helpers.TablesFirebaseHelper;
import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

public class ModifyTableActivity extends AppCompatActivity {

    private Table selected;
    private DatabaseReference databaseReference;
    private GenericChildEventListener childEventListener;
    private EditText editTextName;
    private Button buttonDelete, buttonSave;
    private int deleted, modified;

    /**
     * When this activity is created, inflate a layout containing an EditText and two Buttons.
     * The Table object selected will represent the Table that the user clicked on
     * to get to this activity. The EditText will allow the user to change the attributes of some
     * Table. The Button buttonSave will confirm the modification of the Table to the
     * database. The Button buttonDelete will delete the selected Table object from the database.
     * An GenericChildEventListener will be setup to close this activity when the selected Table
     * object is changed by another user in the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_table);

        // Receive the attributes of the selected Table object.
        Intent intent = getIntent();

        selected = new Table(
                intent.getStringExtra("key"),
                intent.getStringExtra("name"),
                intent.getStringExtra("status")
        );

        // Initialize DatabaseReference and GenericChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_table_changed));

        // Attach the GenericChildEventListener to the selected Table object in the database.
        databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_modify_table_name);
        buttonDelete = findViewById(R.id.button_modify_table_delete);
        buttonSave = findViewById(R.id.button_modify_table_save);

        // Set EditText values as the attributes of the selected Table.
        editTextName.setText(selected.getName());

        // Attach a click listener to buttonDelete.
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach GenericChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected Table object from the database.
                deleted = TablesFirebaseHelper.delete(selected);

                // Print Toast indicating the status of the deletion.
                if (deleted == 0) {
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_delete_table_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_delete_table_failed, Toast.LENGTH_SHORT).show();
                }

                // Close this activity.
                finish();
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach GenericChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify Table selected in the database with the attributes specified in the EditText.
                modified = TablesFirebaseHelper.modify(selected, new Table(
                        editTextName.getText().toString(),
                        selected.getStatus()
                ));

                // Depending on the status of the modification, print a Toast and take an action.
                if (modified == 0) {
                    // Modification successful. Close this activity.
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_modify_table_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (modified == 2) {
                    // Modification failed due to invalid attributes. Reattach GenericChildEventListener.
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_table_invalid, Toast.LENGTH_SHORT).show();
                    databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(childEventListener);
                } else {
                    // Modification failed due to database error. Close this activity.
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_modify_table_failed, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * When this activity leaves the foreground, close this activity.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Detach GenericChildEventListener.
        databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}
