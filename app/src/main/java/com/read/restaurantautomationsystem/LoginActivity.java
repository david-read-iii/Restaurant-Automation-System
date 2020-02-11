//*************************************************************************************************
// This class... <describe what class does>
//*************************************************************************************************

package com.read.restaurantautomationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bring XML elements to Java.
        EditText editTextUsername = findViewById(R.id.activity_login_edittext_username);
        EditText editTextPassword = findViewById(R.id.activity_login_edittext_password);
        Button button = findViewById(R.id.activity_login_button);

        // Set listener to respond to login button clicks.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* IMPLEMENT: Call a Firebase function to check if this username and password
                 *            combination is present in the "users" collection. If it is, send
                 *            the user to the MainActivity. If not, prompt the user with an error
                 *            Toast.
                 * */

                // Assume this combination of username and password is present in the database, send
                // user to the MainActivity.
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
