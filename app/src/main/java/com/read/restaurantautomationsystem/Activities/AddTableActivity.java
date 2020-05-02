package com.read.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.read.restaurantautomationsystem.Firebase.Helpers.TablesFirebaseHelper;
import com.read.restaurantautomationsystem.Models.Table;
import com.read.restaurantautomationsystem.R;

public class AddTableActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonAdd;
    private int saved;

    /**
     * When this activity is created, inflate a layout containing an EditText and a Button. The
     * EditText will allow the user to specify the attribute of a new Table. The Button
     * will confirm the saving of the new Table to the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);

        // Bring XML elements to Java.
        editTextName = findViewById(R.id.edit_text_add_table_name);
        buttonAdd = findViewById(R.id.button_add_table);

        // Attach a click listener to buttonAdd.
        buttonAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // Save a Table object with the specified attributes to the database.
                saved = TablesFirebaseHelper.save(new Table(
                        editTextName.getText().toString(),
                        "Ready"
                ));

                // If save successful, close this activity.
                if (saved == 0) {
                    finish();
                }
                // If save failed due to database error, print Toast.
                else if (saved == 1) {
                    Toast.makeText(AddTableActivity.this, R.string.toast_add_table_failed, Toast.LENGTH_SHORT).show();
                }
                // If save failed due to the object having blank attributes, print Toast.
                else if (saved == 2){
                    Toast.makeText(AddTableActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                }
                // If save failed due to a non-unique name attribute, print Toast.
                else {
                    Toast.makeText(AddTableActivity.this, R.string.toast_table_name_invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
