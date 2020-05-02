package com.read.restaurantautomationsystem.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

                // Save an Employee object with the specified attributes to the database.
                saved = EmployeesFirebaseHelper.save(new Employee(
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString()
                ));

                // If save successful, close this activity.
                if (saved == 0) {
                    finish();
                }
                // If save failed due to database error, print Toast.
                else if (saved == 1) {
                    Toast.makeText(AddEmployeeActivity.this, R.string.toast_add_employee_failed, Toast.LENGTH_SHORT).show();
                }
                // If save failed due to the object having blank attributes, print Toast.
                else if (saved == 2){
                    Toast.makeText(AddEmployeeActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                }
                // If save failed due to a non-unique username attribute, print Toast.
                else {
                    Toast.makeText(AddEmployeeActivity.this, R.string.toast_employee_username_invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
