package com.read.restaurantautomationsystem.Activities;

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
import com.read.restaurantautomationsystem.Firebase.Helpers.MenuItemsFirebaseHelper;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.R;

public class AddMenuItemActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice, editTextCategory;
    private Button buttonAdd;
    private int saved;

    /**
     * When this activity is created, inflate a layout containing some EditTexts and a Button. The
     * EditTexts will allow the user to specify the attributes of a new MenuItem. The Button
     * will confirm the saving of the new MenuItem to the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_add_menu_item_name);
        editTextPrice = findViewById(R.id.edit_text_add_menu_item_price);
        editTextCategory = findViewById(R.id.edit_text_add_menu_item_category);
        buttonAdd = findViewById(R.id.button_add_menu_item);

        // Attach a click listener to buttonAdd.
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If a MenuItem object with blank attributes is passed, do not save the object.
                if (editTextName.getText().toString().equals("") || editTextPrice.getText().toString().equals("") || editTextCategory.getText().toString().equals("")) {
                    Toast.makeText(AddMenuItemActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                }
                // If a MenuItem object has an invalidly formatted price attribute, do not save the object.
                else if (!priceFormattedCorrectly(editTextPrice.getText().toString())) {
                    Toast.makeText(AddMenuItemActivity.this, R.string.toast_menu_item_price_invalid, Toast.LENGTH_SHORT).show();
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
                            if (dataSnapshot.hasChildren()) {
                                Toast.makeText(AddMenuItemActivity.this, R.string.toast_menu_item_name_invalid, Toast.LENGTH_SHORT).show();
                            }
                            // Attempt to save the MenuItem object in the database. Watch for a DatabaseException.
                            else {
                                saved = MenuItemsFirebaseHelper.save(passedMenuItem);

                                // Take action according to status of modification.
                                if (saved == 0) {
                                    finish();
                                } else {
                                    Toast.makeText(AddMenuItemActivity.this, R.string.toast_add_menu_item_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddMenuItemActivity.this, R.string.toast_add_menu_item_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
