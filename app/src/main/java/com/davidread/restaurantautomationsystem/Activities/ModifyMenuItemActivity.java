package com.davidread.restaurantautomationsystem.Activities;

import android.content.Intent;
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
import com.davidread.restaurantautomationsystem.Firebase.ChildEventListeners.GenericChildEventListener;
import com.davidread.restaurantautomationsystem.Firebase.Helpers.MenuItemsFirebaseHelper;
import com.davidread.restaurantautomationsystem.Models.MenuItem;
import com.davidread.restaurantautomationsystem.R;

import java.text.DecimalFormat;

public class ModifyMenuItemActivity extends AppCompatActivity {

    private MenuItem selected;
    private DatabaseReference databaseReference;
    private GenericChildEventListener childEventListener;
    private EditText editTextName, editTextPrice, editTextCategory;
    private Button buttonDelete, buttonSave;
    private int deleted, modified;

    /**
     * When this activity is created, inflate a layout containing some EditTexts and two Buttons.
     * The MenuItem object selected will represent the MenuItem that the user clicked on
     * to get to this activity. The EditTexts will allow the user to change the attributes of some
     * MenuItem. The Button buttonSave will confirm the modification of the MenuItem to the
     * database. The Button buttonDelete will delete the selected MenuItem object from the database.
     * A ChildEventListener will be setup to close this activity when the selected MenuItem
     * object is changed by another user in the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_menu_item);

        // Receive the attributes of the selected MenuItem.
        Intent intent = getIntent();

        selected = new MenuItem(
                intent.getStringExtra("key"),
                intent.getStringExtra("name"),
                Double.parseDouble(intent.getStringExtra("price")),
                intent.getStringExtra("category")
        );

        // Initialize DatabaseReference and ChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_selected_menu_item_modified), getString(R.string.toast_selected_menu_item_deleted));

        // Attach the ChildEventListener to the selected MenuItem object in the database.
        databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_modify_menu_item_name);
        editTextPrice = findViewById(R.id.edit_text_modify_menu_item_price);
        editTextCategory = findViewById(R.id.edit_text_modify_menu_item_category);
        buttonDelete = findViewById(R.id.button_modify_menu_item_delete);
        buttonSave = findViewById(R.id.button_modify_menu_item_save);

        // Create DecimalFormat object to format attributes with decimal places.
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        // Set EditText values as the attributes of the selected MenuItem.
        editTextName.setText(selected.getName());
        editTextPrice.setText(decimalFormat.format(selected.getPrice()));
        editTextCategory.setText(selected.getCategory());

        // Attach a click listener to buttonDelete.
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Detach ChildEventListener.
                databaseReference.child("MenuItems").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected MenuItem object from the database.
                deleted = MenuItemsFirebaseHelper.delete(selected.getKey());

                // If deletion successful, close this activity.
                if (deleted == 0) {
                    finish();
                }
                // If deletion failed due to database error, reattach ChildEventListener and print Toast.
                else {
                    databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_delete_menu_item_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Detach ChildEventListener.
                databaseReference.child("MenuItems").child(selected.getKey()).removeEventListener(childEventListener);

                // If a MenuItem object with blank attributes is passed, do not save the object.
                if (editTextName.getText().toString().equals("") || editTextPrice.getText().toString().equals("") || editTextCategory.getText().toString().equals("")) {
                    databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                }
                // If a MenuItem object has an invalidly formatted price attribute, do not save the object.
                else if (!priceFormattedCorrectly(editTextPrice.getText().toString())) {
                    databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_menu_item_price_invalid, Toast.LENGTH_SHORT).show();
                } else {

                    // Get attributes of the passed MenuItem object.
                    final MenuItem passedMenuItem = new MenuItem(
                            editTextName.getText().toString(),
                            Double.parseDouble(editTextPrice.getText().toString()),
                            editTextCategory.getText().toString()
                    );

                    // Query the database under the "MenuItems" collection for a child with the requested new name.
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query nameQuery = databaseReference.child("MenuItems").orderByChild("name").equalTo(passedMenuItem.getName());
                    nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // If the query returns at least one child and the old name is not the new name, then an MenuItem object already uses the name.
                            if (dataSnapshot.hasChildren() && !selected.getName().equals(passedMenuItem.getName())) {
                                databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);
                                Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_menu_item_name_invalid, Toast.LENGTH_SHORT).show();
                            }
                            // Attempt to modify the MenuItem object in the database. Watch for a DatabaseException.
                            else {
                                modified = MenuItemsFirebaseHelper.modify(selected.getKey(), passedMenuItem);

                                // Take action according to status of modification.
                                if (modified == 0) {
                                    finish();
                                } else {
                                    databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);
                                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_modify_menu_item_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);
                            Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_modify_menu_item_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
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
        databaseReference.child("MenuItems").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }

    /**
     * Returns true if the price attribute is properly formatted.
     */
    public Boolean priceFormattedCorrectly(String price) {

        if (price.contains(".")) {
            int seperatorPosition = price.indexOf('.');
            int decimalPlaces = price.length() - seperatorPosition - 1;

            if (decimalPlaces != 2) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
