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
import com.read.restaurantautomationsystem.Firebase.GenericChildEventListener;
import com.read.restaurantautomationsystem.Firebase.InventoryItemsFirebaseHelper;
import com.read.restaurantautomationsystem.Models.InventoryItem;
import com.read.restaurantautomationsystem.R;

public class ModifyInventoryItemActivity extends AppCompatActivity {

    private InventoryItem selected;
    private DatabaseReference databaseReference;
    private GenericChildEventListener childEventListener;
    private EditText editTextName, editTextQuantity;
    private Button buttonDelete, buttonSave;
    private int deleted, modified;

    /**
     * When this activity is created, inflate a layout containing some EditTexts and two Buttons.
     * The InventoryItem object selected will represent the InventoryItem that the user clicked on
     * to get to this activity. The EditTexts will allow the user to change the attributes of some
     * InventoryItem. The Button buttonSave will confirm the modification of the InventoryItem to the
     * database. The Button buttonDelete will delete the selected InventoryItem object from the database.
     * An GenericChildEventListener will be setup to close this activity when the selected InventoryItem
     * object is changed by another user in the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_inventory_item);

        // Receive the attributes of the selected InventoryItem.
        Intent intent = getIntent();

        selected = new InventoryItem(
                intent.getStringExtra("key"),
                intent.getStringExtra("name"),
                intent.getStringExtra("quantity")
        );

        // Initialize DatabaseReference and GenericChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_inventory_item_changed));

        // Attach the GenericChildEventListener to the selected InventoryItem object in the database.
        databaseReference.child("InventoryItems").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_modify_inventory_item_name);
        editTextQuantity = findViewById(R.id.edit_text_modify_inventory_item_quantity);
        buttonDelete = findViewById(R.id.button_modify_inventory_item_delete);
        buttonSave = findViewById(R.id.button_modify_inventory_item_save);

        // Set EditText values as the attributes of the selected InventoryItem.
        editTextName.setText(selected.getName());
        editTextQuantity.setText(selected.getQuantity());

        // Attach a click listener to buttonDelete.
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach GenericChildEventListener.
                databaseReference.child("InventoryItems").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected InventoryItem object from the database.
                deleted = InventoryItemsFirebaseHelper.delete(selected);

                // Print Toast indicating the status of the deletion.
                if (deleted == 0) {
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_delete_inventory_item_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_delete_inventory_item_failed, Toast.LENGTH_SHORT).show();
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
                databaseReference.child("InventoryItems").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify InventoryItem selected in the database with the attributes specified in the EditTexts.
                modified = InventoryItemsFirebaseHelper.modify(selected, new InventoryItem(
                        editTextName.getText().toString(),
                        editTextQuantity.getText().toString()
                ));

                // Depending on the status of the modification, print a Toast and take an action.
                if (modified == 0) {
                    // Modification successful. Close this activity.
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_modify_inventory_item_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (modified == 2) {
                    // Modification failed due to invalid attributes. Reattach GenericChildEventListener.
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_inventory_item_invalid, Toast.LENGTH_SHORT).show();
                    databaseReference.child("InventoryItems").child(selected.getKey()).addChildEventListener(childEventListener);
                } else {
                    // Modification failed due to database error. Close this activity.
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_modify_inventory_item_failed, Toast.LENGTH_SHORT).show();
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
        databaseReference.child("InventoryItems").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}
