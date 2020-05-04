package com.read.restaurantautomationsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;
import com.read.restaurantautomationsystem.Services.LoggedInService;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

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
        final EditText editTextUsername = findViewById(R.id.edit_text_login_username);
        final EditText editTextPassword = findViewById(R.id.edit_text_login_password);
        Button button = findViewById(R.id.button_login);

        // Set text in Toolbar.
        getSupportActionBar().setTitle(R.string.name_activity_login);

        // Define click behavior for "Log In" button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Run task to check if username and password combination is in the database.
                Task<DataSnapshot> task = getEmployeeFromUsernamePasswordCombo(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                task.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        // If the key of the returned DataSnapshot is null or empty, the entered combo is incorrect. So print Toast.
                        // TODO: Which one of these conditions in the if statement, if any, will signify an incorrect combo?
                        if (dataSnapshot.getKey() == null || dataSnapshot.getKey().equals("")) {
                            Toast.makeText(LoginActivity.this, R.string.toast_login_invalid, Toast.LENGTH_SHORT).show();
                        }
                        // If the key of the returned DataSnapshot is not null or not empty, the entered combo is correct. So, log the Employee in.
                        else {

                            // Get attributes of the logged in Employee.
                            Employee employee = new Employee(
                                    dataSnapshot.getKey(),
                                    dataSnapshot.child("firstName").getValue(String.class),
                                    dataSnapshot.child("lastName").getValue(String.class),
                                    dataSnapshot.child("username").getValue(String.class),
                                    dataSnapshot.child("password").getValue(String.class),
                                    dataSnapshot.child("role").getValue(String.class)
                            );

                            // Start LoggedInService. Pass the key attribute of the logged in Employee in.
                            Intent intent = new Intent(LoginActivity.this, LoggedInService.class);
                            intent.putExtra("key", employee.getKey());
                            intent.putExtra("firstName", employee.getFirstName());
                            intent.putExtra("lastName", employee.getLastName());
                            intent.putExtra("username", employee.getUsername());
                            intent.putExtra("password", employee.getPassword());
                            intent.putExtra("role", employee.getRole());
                            startService(intent);

                            // Start the MainActivity. Pass the attributes of the logged in Employee in.
                            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                            intent1.putExtra("key", employee.getKey());
                            intent1.putExtra("firstName", employee.getFirstName());
                            intent1.putExtra("lastName", employee.getLastName());
                            intent1.putExtra("username", employee.getUsername());
                            intent1.putExtra("password", employee.getPassword());
                            intent1.putExtra("role", employee.getRole());
                            startActivity(intent1);
                        }
                    }
                });
            }
        });
    }

    /**
     * Returns a DataSnapshot containing the Employee object corresponding to the given username and
     * password combination. If no Employee corresponds to this combo, the fields of the DataSnapshot
     * will be empty or null.
     */
    // TODO: Is this the correct return type? If so, remove this TODO statement.
    private Task<DataSnapshot> getEmployeeFromUsernamePasswordCombo(String username, String password) {

        // Concatenate the username and password attributes.
        String usernamePasswordCombo = username + "|" + password;

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", usernamePasswordCombo);
        data.put("push", true);

        // Return the result from the Firebase function.
        FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
        return firebaseFunctions
                .getHttpsCallable("checkForUsernamePasswordCombo")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, DataSnapshot>() {
                    @Override
                    public DataSnapshot then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        DataSnapshot result = (DataSnapshot) task.getResult().getData();
                        return result;
                    }
                });
    }
}