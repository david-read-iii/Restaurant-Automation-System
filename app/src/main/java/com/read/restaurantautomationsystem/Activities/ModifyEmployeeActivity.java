package com.read.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Firebase.ChildEventListeners.GenericChildEventListener;
import com.read.restaurantautomationsystem.Firebase.Helpers.EmployeesFirebaseHelper;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

public class ModifyEmployeeActivity extends AppCompatActivity {

    private Employee selected;
    private DatabaseReference databaseReference;
    private GenericChildEventListener childEventListener;
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
     * An ChildEventListener will be setup to close this activity when the selected Employee
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

        // Initialize DatabaseReference and ChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new GenericChildEventListener(this, getString(R.string.toast_selected_employee_modified), getString(R.string.toast_selected_employee_deleted));

        // Attach the ChildEventListener to the selected Employee object in the database.
        databaseReference.child("Employees").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextFirstName = findViewById(R.id.edit_text_modify_employee_first_name);
        editTextLastName = findViewById(R.id.edit_text_modify_employee_last_name);
        editTextUsername = findViewById(R.id.edit_text_modify_employee_username);
        editTextPassword = findViewById(R.id.edit_text_modify_employee_password);
        spinnerRole = findViewById(R.id.spinner_modify_employee_role);
        buttonDelete = findViewById(R.id.button_modify_employee_delete);
        buttonSave = findViewById(R.id.button_modify_employee_save);

        // Define and attach ArrayAdapter to Spinner.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_options_role, android.R.layout.simple_spinner_item);
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
                // Detach ChildEventListener.
                databaseReference.child("Employees").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected Employee object from the database.
                deleted = EmployeesFirebaseHelper.delete(selected.getKey());

                // If deletion successful, close this activity.
                if (deleted == 0) {
                    finish();
                }
                // If deletion failed due to database error, reattach ChildEventListener and print Toast.
                else {
                    databaseReference.child("Employees").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_delete_employee_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Detach ChildEventListener.
                databaseReference.child("Employees").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify Employee selected in the database with the attributes specified in the EditTexts.
                modified = EmployeesFirebaseHelper.modify(selected.getKey(), selected.getUsername(), new Employee(
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString()
                ));

                // If modification successful, close this activity.
                if (modified == 0) {
                    finish();
                }
                // If modification failed due to database error, reattach ChildEventListener and print Toast.
                else if (modified == 1) {
                    databaseReference.child("Employees").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_delete_employee_failed, Toast.LENGTH_SHORT).show();
                }
                // If modification failed due to the object having blank attributes, reattach ChildEventListener and print Toast.
                else if (modified == 2){
                    databaseReference.child("Employees").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                }
                // If modification failed due to a non-unique username attribute, reattach ChildEventListener and print Toast.
                else if (modified == 3) {
                    databaseReference.child("Employees").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_employee_username_invalid, Toast.LENGTH_SHORT).show();
                }
                // If modification failed due username or password attribute containing the delimiter, reattach ChildEventListener and print Toast.
                else {
                    databaseReference.child("Employees").child(selected.getKey()).addChildEventListener(childEventListener);
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_employee_username_or_password_invalid, Toast.LENGTH_SHORT).show();
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

        // Detach ChildEventListener.
        databaseReference.child("Employees").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}