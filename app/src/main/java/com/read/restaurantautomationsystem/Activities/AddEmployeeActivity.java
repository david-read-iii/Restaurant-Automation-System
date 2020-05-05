package com.read.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Firebase.Helpers.EmployeesFirebaseHelper;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonAdd;
    private int saved;

    /**
     * When this activity is created, inflate a layout containing some EditTexts, a Spinner, and a
     * Button. The EditTexts and Spinner will allow the user to specify the attributes of a new
     * Employee. The Button will confirm the saving of the new Employee to the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        // Bring XML elements to Java.
        editTextFirstName = findViewById(R.id.edit_text_add_employee_first_name);
        editTextLastName = findViewById(R.id.edit_text_add_employee_last_name);
        editTextUsername = findViewById(R.id.edit_text_add_employee_username);
        editTextPassword = findViewById(R.id.edit_text_add_employee_password);
        spinnerRole = findViewById(R.id.spinner_add_employee_role);
        buttonAdd = findViewById(R.id.button_add_employee);

        // Define and attach ArrayAdapter to Spinner.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_options_role, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerAdapter);

        // Attach a click listener to buttonAdd.
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get attributes of the passed Employee object.
                final Employee passedEmployee = new Employee(
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString()
                );

                // If an Employee object with blank attributes is passed, do not save the object.
                if (passedEmployee.getFirstName().equals("") || passedEmployee.getLastName().equals("") || passedEmployee.getUsername().equals("") || passedEmployee.getPassword().equals("")) {
                    Toast.makeText(AddEmployeeActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                } else {

                    // Query the database under the "Employees" collection for a child with the requested new username.
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query usernameQuery = databaseReference.child("Employees").orderByChild("username").equalTo(passedEmployee.getUsername());
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // If the query returns at least one child, then an Employee object already uses the username.
                            if (dataSnapshot.hasChildren()) {
                                Toast.makeText(AddEmployeeActivity.this, R.string.toast_employee_username_invalid, Toast.LENGTH_SHORT).show();
                            }
                            // Attempt to save the Employee object in the database. Watch for a DatabaseException.
                            else {
                                saved = EmployeesFirebaseHelper.save(passedEmployee);

                                // Take action according to status of save.
                                if (saved == 0) {
                                    finish();
                                } else {
                                    Toast.makeText(AddEmployeeActivity.this, R.string.toast_add_employee_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddEmployeeActivity.this, R.string.toast_add_employee_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
