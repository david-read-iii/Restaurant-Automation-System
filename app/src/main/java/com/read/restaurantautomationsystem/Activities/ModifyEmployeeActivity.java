package com.read.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Firebase.EmployeesChildEventListener;
import com.read.restaurantautomationsystem.Firebase.EmployeesFirebaseHelper;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

public class ModifyEmployeeActivity extends AppCompatActivity {

    private Employee selected;
    private DatabaseReference databaseReference;
    private EmployeesChildEventListener childEventListener;
    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonDelete, buttonSave;
    private int deleted, modified;

    /**
     * When this activity is created, inflate a layout containing some EditTexts, a Spinner, and two
     * Buttons. The Employee object selected will represent the Employee that the user clicked on to
     * get to this activity. The EditTexts and Spinner will allow the user to change the attributes
     * of some Employee. The Button buttonSave will confirm the modification of the Employee to the
     * database. The Button buttonDelete will delete the selected Employee object from the database.
     * An EmployeesChildEventListener will be setup to close this activity when the selected Employee
     * object is changed by another user in the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_employee);

        // Receive the attributes of the selected Employee.
        Intent intent = getIntent();

        selected = new Employee(
                intent.getStringExtra("key"),
                intent.getStringExtra("firstName"),
                intent.getStringExtra("lastName"),
                intent.getStringExtra("username"),
                intent.getStringExtra("password"),
                intent.getStringExtra("role")
        );

        // Initialize DatabaseReference and EmployeesChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new EmployeesChildEventListener(this);

        // Attach the EmployeesChildEventListener to the selected Employee object in the database.
        databaseReference.child("Employees").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextFirstName = findViewById(R.id.edittext_modify_employee_first_name);
        editTextLastName = findViewById(R.id.edittext_modify_employee_last_name);
        editTextUsername = findViewById(R.id.edittext_modify_employee_username);
        editTextPassword = findViewById(R.id.edittext_modify_employee_password);
        spinnerRole = findViewById(R.id.spinner_modify_employee_role);
        buttonDelete = findViewById(R.id.button_employee_delete);
        buttonSave = findViewById(R.id.button_employee_save);

        // Define and attach ArrayAdapter to Spinner.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_role_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerAdapter);

        // Set the EditText values as the attributes of the selected Employee.
        editTextFirstName.setText(selected.getFirstName());
        editTextLastName.setText(selected.getLastName());
        editTextUsername.setText(selected.getUsername());
        editTextPassword.setText(selected.getPassword());
        spinnerRole.setSelection(spinnerAdapter.getPosition(selected.getRole()));

        // Attach a click listener to buttonDelete.
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach EmployeesChildEventListener.
                databaseReference.child("Employees").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected Employee object from the database.
                deleted = EmployeesFirebaseHelper.delete(selected);

                // Print Toast indicating status of the deletion.
                if (deleted == 0) {
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_delete_employee_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_delete_employee_failed, Toast.LENGTH_SHORT).show();
                }

                // Close this activity.
                finish();
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach EmployeesChildEventListener.
                databaseReference.child("Employees").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify Employee selected in the database with the attributes specified in the EditTexts.
                modified = EmployeesFirebaseHelper.modify(selected, new Employee(
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString()
                ));

                // Depending on the status of the modification, print a Toast and take an action.
                if (modified == 0) {
                    // Modification successful. Close this activity.
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_modify_employee_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (modified == 2) {
                    // Modification failed due to invalid attributes. Reattach UsersChildEventListener.
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_employee_invalid, Toast.LENGTH_SHORT).show();
                    databaseReference.child("Users").child(selected.getKey()).addChildEventListener(childEventListener);
                } else {
                    // Modification failed due to database error. Close this activity.
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_delete_employee_failed, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * When this activity leaves the foreground, close this activity.
     */
    @Override
    protected void onPause(){
        super.onPause();

        // Detach EmployeesChildEventListener.
        databaseReference.child("Users").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}