package com.read.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.read.restaurantautomationsystem.Firebase.MenuItemsFirebaseHelper;
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
                // Save an MenuItem object with the specified attributes to the database.
                saved = MenuItemsFirebaseHelper.save(new MenuItem(
                        editTextName.getText().toString(),
                        editTextPrice.getText().toString(),
                        editTextCategory.getText().toString()
                ));

                // Depending on the status of the save, print a Toast and take an action.
                if (saved == 0) {
                    // Save successful. Close this activity.
                    Toast.makeText(AddMenuItemActivity.this, R.string.toast_add_menu_item_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (saved == 1) {
                    // Save failed due to database error. Close this activity.
                    Toast.makeText(AddMenuItemActivity.this, R.string.toast_add_menu_item_failed, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Save failed due to invalid attributes.
                    Toast.makeText(AddMenuItemActivity.this, R.string.toast_menu_item_invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
