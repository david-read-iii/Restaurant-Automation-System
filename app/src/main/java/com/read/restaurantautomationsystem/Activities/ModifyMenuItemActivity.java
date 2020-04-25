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
import com.read.restaurantautomationsystem.Firebase.MenuItemsFirebaseHelper;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.R;

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
     * An GenericChildEventListener will be setup to close this activity when the selected MenuItem
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
                intent.getStringExtra("price"),
                intent.getStringExtra("category")
        );

        // Initialize DatabaseReference and GenericChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_menu_item_changed));

        // Attach the GenericChildEventListener to the selected MenuItem object in the database.
        databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_modify_menu_item_name);
        editTextPrice = findViewById(R.id.edit_text_modify_menu_item_price);
        editTextCategory = findViewById(R.id.edit_text_modify_menu_item_category);
        buttonDelete = findViewById(R.id.button_modify_menu_item_delete);
        buttonSave = findViewById(R.id.button_modify_menu_item_save);

        // Set EditText values as the attributes of the selected MenuItem.
        editTextName.setText(selected.getName());
        editTextPrice.setText(selected.getPrice());
        editTextCategory.setText(selected.getCategory());

        // Attach a click listener to buttonDelete.
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach GenericChildEventListener.
                databaseReference.child("MenuItems").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected MenuItem object from the database.
                deleted = MenuItemsFirebaseHelper.delete(selected);

                // Print Toast indicating the status of the deletion.
                if (deleted == 0) {
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_delete_menu_item_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_delete_menu_item_failed, Toast.LENGTH_SHORT).show();
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
                databaseReference.child("MenuItems").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify MenuItem selected in the database with the attributes specified in the EditTexts.
                modified = MenuItemsFirebaseHelper.modify(selected, new MenuItem(
                        editTextName.getText().toString(),
                        editTextPrice.getText().toString(),
                        editTextCategory.getText().toString()
                ));

                // Depending on the status of the modification, print a Toast and take an action.
                if (modified == 0) {
                    // Modification successful. Close this activity.
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_modify_menu_item_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (modified == 2) {
                    // Modification failed due to invalid attributes. Reattach GenericChildEventListener.
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_menu_item_invalid, Toast.LENGTH_SHORT).show();
                    databaseReference.child("MenuItems").child(selected.getKey()).addChildEventListener(childEventListener);
                } else {
                    // Modification failed due to database error. Close this activity.
                    Toast.makeText(ModifyMenuItemActivity.this, R.string.toast_modify_menu_item_failed, Toast.LENGTH_SHORT).show();
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
        databaseReference.child("MenuItems").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}
