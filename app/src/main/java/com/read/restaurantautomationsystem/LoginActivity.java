package com.read.restaurantautomationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    /**
     * Setup an activity with two EditTexts and a Button when this activity is created. The
     * EditTexts will be fields for the user to enter their username and password. The Button will
     * log the user into the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bring XML elements to Java.
        EditText editTextUsername = findViewById(R.id.edittext_username);
        EditText editTextPassword = findViewById(R.id.edittext_password);
        Button button = findViewById(R.id.button_login);

        /* Attach a click listener to button to contact the database to see if this user has the
         * correct credentials. If they are correct, receive their full name and role from the
         * database and start the MainActivity. */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* TODO: Call a Firebase function to see if the given username and password
                    combination is in the database. If it is, the function will return true. It
                    will also return the user's full name and role. If the combination is not
                    in the database, print a Toast indicating so. */

                /* Until the function is implemented, assume a user named Jane Doe with a role of
                 * Manager has been returned by the function. */
                String firstName = "Jane";
                String lastName = "Doe";
                String role = "Manager";

                // Start the MainActivity. Pass the user's full name and role as extras.
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });
    }
}