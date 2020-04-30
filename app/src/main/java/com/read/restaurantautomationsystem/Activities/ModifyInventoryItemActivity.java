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
import com.read.restaurantautomationsystem.Firebase.Helpers.InventoryItemsFirebaseHelper;
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
     * An ChildEventListener will be setup to close this activity when the selected InventoryItem
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
                Integer.parseInt(intent.getStringExtra("quantity"))
        );

        // Initialize DatabaseReference and ChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_selected_inventory_item_modified), getString(R.string.toast_selected_inventory_item_deleted));

        // Attach the ChildEventListener to the selected InventoryItem object in the database.
        databaseReference.child("InventoryItems").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_modify_inventory_item_name);
        editTextQuantity = findViewById(R.id.edit_text_modify_inventory_item_quantity);
        buttonDelete = findViewById(R.id.button_modify_inventory_item_delete);
        buttonSave = findViewById(R.id.button_modify_inventory_item_save);

        // Set EditText values as the attributes of the selected InventoryItem.
        editTextName.setText(selected.getName());
        editTextQuantity.setText(Integer.toString(selected.getQuantity()));

        // Attach a click listener to buttonDelete.
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Detach ChildEventListener.
                databaseReference.child("InventoryItems").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected InventoryItem object from the database.
                deleted = InventoryItemsFirebaseHelper.delete(selected.getKey());

                // If deletion successful, close this activity.
                if (deleted == 0) {
                    finish();
                }
                // If deletion failed due to database error, reattach ChildEventListener and print Toast.
                else {
                    databaseReference.child("InventoryItems").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_delete_inventory_item_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Detach ChildEventListener.
                databaseReference.child("InventoryItems").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify InventoryItem selected in the database with the attributes specified in the EditTexts.
                modified = InventoryItemsFirebaseHelper.modify(selected.getKey(), new InventoryItem(
                        editTextName.getText().toString(),
                        Integer.parseInt(editTextQuantity.getText().toString())
                ));

                // If modification successful, close this activity.
                if (modified == 0) {
                    finish();
                }
                // If modification failed due to database error, reattach ChildEventListener and print Toast.
                else if (modified == 1) {
                    databaseReference.child("InventoryItems").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_modify_inventory_item_failed, Toast.LENGTH_SHORT).show();
                }
                // If modification failed due to the object having invalid attributes, reattach ChildEventListener and print Toast.
                else {
                    databaseReference.child("InventoryItems").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyInventoryItemActivity.this, R.string.toast_inventory_item_invalid, Toast.LENGTH_SHORT).show();
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
        databaseReference.child("InventoryItems").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}
