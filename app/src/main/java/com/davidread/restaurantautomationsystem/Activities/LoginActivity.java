package com.davidread.restaurantautomationsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.davidread.restaurantautomationsystem.Models.Employee;
import com.davidread.restaurantautomationsystem.R;
import com.davidread.restaurantautomationsystem.Services.LoggedInService;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;

    /**
     * When this activity is created, inflate a layout containing two EditTexts and a Button. The
     * EditTexts will be fields for the user to enter their username and password. The Button will
     * log the user into the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bring XML elements to Java.
        editTextUsername = findViewById(R.id.edit_text_login_username);
        editTextPassword = findViewById(R.id.edit_text_login_password);
        final Button button = findViewById(R.id.button_login);

        // Set text in Toolbar.
        getSupportActionBar().setTitle(R.string.name_activity_login);

        // Define click behavior for "Log In" button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make Button unclickable while this code processes.
                button.setClickable(false);

                // If either EditText is blank, print a Toast.
                if (editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.toast_object_invalid_blank, Toast.LENGTH_SHORT).show();
                } else {

                    // Query the database under the "Employees" collection for a child with the username defined in the EditText.
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query usernameQuery = databaseReference.child("Employees").orderByChild("username").equalTo(editTextUsername.getText().toString());
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // Initialize a null Employee object representing the logged in Employee.
                            Employee loggedInEmployee = new Employee();
                            Boolean isLoggedIn = true;

                            // Get attributes of the Employee object returned by the query.
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                loggedInEmployee = new Employee(
                                        ds.getKey(),
                                        ds.child("firstName").getValue(String.class),
                                        ds.child("lastName").getValue(String.class),
                                        ds.child("username").getValue(String.class),
                                        ds.child("password").getValue(String.class),
                                        ds.child("role").getValue(String.class)
                                );
                                
                                isLoggedIn = ds.child("isLoggedIn").getValue(Boolean.class);
                            }

                            /* If the key of the Employee object is null, no Employee object was
                             * returned by the query. If the password of the Employee object is not
                             * equal to the password in the EditText, the user entered an invalid
                             * password. In either case, print a Toast. */
                            if (loggedInEmployee.getKey() == null || !loggedInEmployee.getPassword().equals(editTextPassword.getText().toString())) {
                                Toast.makeText(LoginActivity.this, R.string.toast_login_invalid, Toast.LENGTH_SHORT).show();
                            }
                            // If Employee is already logged in, print a Toast.
                            else if (isLoggedIn != null && isLoggedIn) {
                                Toast.makeText(LoginActivity.this, R.string.toast_employee_account_active, Toast.LENGTH_SHORT).show();
                            } else {

                                // Start LoggedInService, passing the attributes of the logged in Employee.
                                Intent intent = new Intent(LoginActivity.this, LoggedInService.class);
                                intent.putExtra("key", loggedInEmployee.getKey());
                                intent.putExtra("firstName", loggedInEmployee.getFirstName());
                                intent.putExtra("lastName", loggedInEmployee.getLastName());
                                intent.putExtra("username", loggedInEmployee.getUsername());
                                intent.putExtra("password", loggedInEmployee.getPassword());
                                intent.putExtra("role", loggedInEmployee.getRole());
                                startService(intent);

                                // Start the MainActivity, passing the attributes of the logged in Employee.
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                intent1.putExtra("key", loggedInEmployee.getKey());
                                intent1.putExtra("firstName", loggedInEmployee.getFirstName());
                                intent1.putExtra("lastName", loggedInEmployee.getLastName());
                                intent1.putExtra("username", loggedInEmployee.getUsername());
                                intent1.putExtra("password", loggedInEmployee.getPassword());
                                intent1.putExtra("role", loggedInEmployee.getRole());
                                startActivity(intent1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, R.string.toast_login_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // Make Button clickable again.
                button.setClickable(true);
            }
        });
    }

    /**
     * When this activity is in the foreground, ensure that each EditText is blank and the username
     * EditText is focused.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Set text of each EditText as the empty string.
        editTextUsername.setText("");
        editTextPassword.setText("");

        // Set username EditText as focused.
        editTextUsername.requestFocus();
    }

}