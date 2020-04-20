package com.read.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonAdd;
    private DatabaseReference databaseReference;

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
        editTextFirstName = findViewById(R.id.edittext_add_employee_first_name);
        editTextLastName = findViewById(R.id.edittext_add_employee_last_name);
        editTextUsername = findViewById(R.id.edittext_add_employee_username);
        editTextPassword = findViewById(R.id.edittext_add_employee_password);
        spinnerRole = findViewById(R.id.spinner_add_employee_role);
        buttonAdd = findViewById(R.id.button_employee_add);

        // Define and attach ArrayAdapter to Spinner.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_role_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerAdapter);

        // Attach a click listener to buttonAdd.
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize the DatabaseReference.
                databaseReference = FirebaseDatabase.getInstance().getReference();

                try {
                    // Auto-generate new key from the database.
                    String key = databaseReference.child("Employees").push().getKey();

                    // Save Employee object to the database.
                    databaseReference.child("Employees").child(key).setValue(new Employee(
                            key,
                            editTextFirstName.getText().toString(),
                            editTextLastName.getText().toString(),
                            editTextUsername.getText().toString(),
                            editTextPassword.getText().toString(),
                            spinnerRole.getSelectedItem().toString()
                    ));

                    // Print success Toast.
                    Toast.makeText(AddEmployeeActivity.this, R.string.toast_add_employee_success, Toast.LENGTH_SHORT).show();

                } catch (DatabaseException e) {
                    e.printStackTrace();

                    // Print error Toast.
                    Toast.makeText(AddEmployeeActivity.this, R.string.toast_add_employee_failed, Toast.LENGTH_SHORT).show();
                }

                // Finish the activity.
                finish();
            }
        });
    }
}
