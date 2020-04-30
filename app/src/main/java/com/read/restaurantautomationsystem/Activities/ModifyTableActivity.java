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
     * A ChildEventListener will be setup to close this activity when the selected Table
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

        // Initialize DatabaseReference and ChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_selected_table_modified), getString(R.string.toast_selected_table_deleted));

        // Attach the ChildEventListener to the selected Table object in the database.
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

                // Detach ChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected Table object from the database.
                deleted = TablesFirebaseHelper.delete(selected.getKey());

                // If deletion successful, close this activity.
                if (deleted == 0) {
                    finish();
                }
                // If deletion failed due to database error, reattach ChildEventListener and print Toast.
                else {
                    databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_delete_table_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Detach ChildEventListener.
                databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify Table selected in the database with the attributes specified in the EditText.
                modified = TablesFirebaseHelper.modify(selected.getKey(), new Table(
                        editTextName.getText().toString(),
                        selected.getStatus()
                ));

                // If modification successful, close this activity.
                if (modified == 0) {
                    finish();
                }
                // If modification failed due to database error, reattach ChildEventListener and print Toast.
                else if (modified == 1) {
                    databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_modify_table_failed, Toast.LENGTH_SHORT).show();
                }
                // If modification failed due to the object having invalid attributes, reattach ChildEventListener and print Toast.
                else {
                    databaseReference.child("Tables").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyTableActivity.this, R.string.toast_table_invalid, Toast.LENGTH_SHORT).show();
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

        // Detach ChildEventListener.
        databaseReference.child("Tables").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}
