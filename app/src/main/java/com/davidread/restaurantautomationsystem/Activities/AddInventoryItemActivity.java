package com.davidread.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.davidread.restaurantautomationsystem.Firebase.Helpers.InventoryItemsFirebaseHelper;
import com.davidread.restaurantautomationsystem.Models.InventoryItem;
import com.davidread.restaurantautomationsystem.R;

public class AddInventoryItemActivity extends AppCompatActivity {

    private EditText editTextName, editTextQuantity;
    private Button buttonAdd;
    private int saved;

    /**
     * When this activity is created, inflate a layout containing some EditTexts and a Button. The
     * EditTexts will allow the user to specify the attributes of a new InventoryItem. The Button
     * will confirm the saving of the new InventoryItem to the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory_item);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_add_inventory_item_name);
        editTextQuantity = findViewById(R.id.edit_text_add_inventory_item_quantity);
        buttonAdd = findViewById(R.id.button_add_inventory_item);

        // Attach a click listener to buttonAdd.
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get attributes of the passed InventoryItem object.
                final InventoryItem passedInventoryItem = new InventoryItem(
                        editTextName.getText().toString(),
                        Integer.parseInt(editTextQuantity.getText().toString())
                );

                // If an InventoryItem object with blank attributes is passed, do not modify the object.
                if (passedInventoryItem.getName().equals("") || editTextQuantity.getText().toString().equals("")) {
                    Toast.makeText(AddInventoryItemActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                } else {

                    // Query the database under the "InventoryItems" collection for a child with the requested new name.
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query nameQuery = databaseReference.child("InventoryItems").orderByChild("name").equalTo(passedInventoryItem.getName());
                    nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // If the query returns at least one child, then an InventoryItem object already uses the name.
                            if (dataSnapshot.hasChildren()) {
                                Toast.makeText(AddInventoryItemActivity.this, R.string.toast_inventory_item_name_invalid, Toast.LENGTH_SHORT).show();
                            }
                            // Attempt to modify the Employee object in the database. Watch for a DatabaseException.
                            else {
                                saved = InventoryItemsFirebaseHelper.save(passedInventoryItem);

                                // Take action according to status of save.
                                if (saved == 0) {
                                    finish();
                                } else {
                                    Toast.makeText(AddInventoryItemActivity.this, R.string.toast_modify_inventory_item_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddInventoryItemActivity.this, R.string.toast_modify_inventory_item_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}