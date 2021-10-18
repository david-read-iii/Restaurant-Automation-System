package com.davidread.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.davidread.restaurantautomationsystem.Firebase.Helpers.TablesFirebaseHelper;
import com.davidread.restaurantautomationsystem.Models.Table;
import com.davidread.restaurantautomationsystem.R;

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

                // Get the attributes of the passed Table object.
                final Table passedTable = new Table(
                        editTextName.getText().toString(),
                        "Ready"
                );

                // If a Table object with blank attributes is passed, do not save the object.
                if (passedTable.getName().equals("")) {
                    Toast.makeText(AddTableActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                } else {

                    // Query the database under the "Tables" collection for a child with the requested new name.
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query nameQuery = databaseReference.child("Tables").orderByChild("name").equalTo(passedTable.getName());
                    nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // If the query returns at least one child, then a Table object already uses the name.
                            if (dataSnapshot.hasChildren()) {
                                Toast.makeText(AddTableActivity.this, R.string.toast_table_name_invalid, Toast.LENGTH_SHORT).show();
                            }
                            // Attempt to save the Table object in the database. Watch for a DatabaseException.
                            else {
                                saved = TablesFirebaseHelper.save(passedTable);

                                // Take action according to status of save.
                                if (saved == 0) {
                                    finish();
                                } else {
                                    Toast.makeText(AddTableActivity.this, R.string.toast_add_table_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddTableActivity.this, R.string.toast_add_table_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
