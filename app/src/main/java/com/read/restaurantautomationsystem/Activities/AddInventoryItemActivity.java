package com.read.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.read.restaurantautomationsystem.Firebase.InventoryItemsFirebaseHelper;
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
        editTextName = findViewById(R.id.edittext_add_inventory_item_name);
        editTextQuantity = findViewById(R.id.edittext_add_inventory_item_quantity);
        buttonAdd = findViewById(R.id.button_add_inventory_item);

        // Attach a click listener to buttonAdd.
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save an InventoryItem object with the specified attributes to the database.
                saved = InventoryItemsFirebaseHelper.save(new InventoryItem(
                        editTextName.getText().toString(),
                        editTextQuantity.getText().toString()
                ));

                // Depending on the status of the save, print a Toast and take an action.
                if (saved == 0) {
                    // Save successful. Close this activity.
                    Toast.makeText(AddInventoryItemActivity.this, R.string.toast_add_inventory_item_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (saved == 1) {
                    // Save failed due to database error. Close this activity.
                    Toast.makeText(AddInventoryItemActivity.this, R.string.toast_add_inventory_item_failed, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Save failed due to invalid attributes.
                    Toast.makeText(AddInventoryItemActivity.this, R.string.toast_inventory_item_invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
