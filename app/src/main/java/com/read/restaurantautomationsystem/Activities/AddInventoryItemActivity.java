package com.read.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.read.restaurantautomationsystem.Firebase.Helpers.InventoryItemsFirebaseHelper;
import com.read.restaurantautomationsystem.Models.InventoryItem;
import com.read.restaurantautomationsystem.R;

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

                // Save an InventoryItem object with the specified attributes to the database.
                saved = InventoryItemsFirebaseHelper.save(new InventoryItem(
                        editTextName.getText().toString(),
                        Integer.parseInt(editTextQuantity.getText().toString())
                ));

                // If save successful, close this activity.
                if (saved == 0) {
                    finish();
                }
                // If save failed due to database error, print Toast.
                else if (saved == 1) {
                    Toast.makeText(AddInventoryItemActivity.this, R.string.toast_add_inventory_item_failed, Toast.LENGTH_SHORT).show();
                }
                // If save failed due to the object having invalid attributes, print Toast.
                else {
                    Toast.makeText(AddInventoryItemActivity.this, R.string.toast_inventory_item_invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}