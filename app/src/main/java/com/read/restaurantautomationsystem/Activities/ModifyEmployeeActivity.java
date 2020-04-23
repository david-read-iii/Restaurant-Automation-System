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
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

public class ModifyEmployeeActivity extends AppCompatActivity {

    private Employee selected;
    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonDelete, buttonSave;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    /**
     * When this activity is created, inflate a layout containing some EditTexts, a Spinner, and two
     * Buttons. The EditTexts and Spinner will allow the user to change the attributes of some
     * Employee. The Button buttonSave will confirm the modification of the Employee to the database.
     * The Button buttonDelete will delete the selected Employee object from the database.
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

        // Initialize the DatabaseReference.
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Define ChildEventListener.
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            /**
             * If the Employee object is changed, print a Toast indicating so and finish the activity.
             */
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_employee_changed, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        // Attach the ChildEventListener to the selected Employee object in the database.
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
                // Detach ChildEventListener.
                databaseReference.child("Employees").child(selected.getKey()).removeEventListener(childEventListener);

                try {
                    // Delete the selected Employee object from the database.
                    databaseReference.child("Employees").child(selected.getKey()).removeValue();

                    // Print success Toast.
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_delete_employee_success, Toast.LENGTH_SHORT).show();

                } catch (DatabaseException e) {
                    e.printStackTrace();

                    // Print error Toast.
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_delete_employee_failed, Toast.LENGTH_SHORT).show();
                }

                // Finish the activity.
                finish();
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach ChildEventListener.
                databaseReference.child("Employees").child(selected.getKey()).removeEventListener(childEventListener);

                try {
                    // Delete the old selected Employee object from the database.
                    databaseReference.child("Employees").child(selected.getKey()).removeValue();

                    // Auto-generate new key from database.
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
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_modify_employee_success, Toast.LENGTH_SHORT).show();

                } catch (DatabaseException e) {
                    e.printStackTrace();

                    // Print error Toast.
                    Toast.makeText(ModifyEmployeeActivity.this, R.string.toast_modify_employee_failed, Toast.LENGTH_SHORT).show();
                }

                // Finish the activity.
                finish();
            }
        });
    }

    /**
     * When this activity leaves the foreground, close this activity.
     */
    @Override
    protected void onPause(){
        super.onPause();

        // Detach database listener.
        databaseReference.child("Users").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}