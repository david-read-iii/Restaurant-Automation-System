package com.read.restaurantautomationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;
import com.read.restaurantautomationsystem.Services.LoggedInService;

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
        EditText editTextUsername = findViewById(R.id.edit_text_login_username);
        EditText editTextPassword = findViewById(R.id.edit_text_login_password);
        Button button = findViewById(R.id.button_login);

        // Set text in Toolbar.
        getSupportActionBar().setTitle(R.string.name_activity_login);

        // Define click behavior for "Log In" button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* TODO: Call a Firebase function to see if the given username and password combo is
                 *   in the database. If it is, the function will return true and return the
                 *   attributes of the now logged in Employee object. If the combo is not in the
                 *   database, print a Toast indicating so. */

                // Assume the Firebase function returns true and returns these attributes.
                Employee employee = new Employee(
                        "0",
                        "Jane",
                        "Doe",
                        "janedoesahoe",
                        "bigjane69",
                        "Manager"
                );

                // Start LoggedInService. Pass the key attribute of the logged in Employee in.
                Intent intent = new Intent(LoginActivity.this, LoggedInService.class);
                intent.putExtra("key", "0");
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
        });
    }
}