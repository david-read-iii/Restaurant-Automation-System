package com.davidread.restaurantautomationsystem.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.davidread.restaurantautomationsystem.Activities.LoginActivity;
import com.davidread.restaurantautomationsystem.Firebase.Helpers.LogFirebaseHelper;
import com.davidread.restaurantautomationsystem.Models.Employee;
import com.davidread.restaurantautomationsystem.Models.Log;
import com.davidread.restaurantautomationsystem.R;

import java.util.Date;

public class LoggedInService extends Service {

    private Employee loggedInEmployee;
    private ChildEventListener changedEmployeeListener;
    private DatabaseReference databaseReference;

    /**
     * Defines a service that will log the logged in user out of the application if their account
     * is modified or deleted. A ChildEventListener is set up to listen for these events in the
     * database.
     */
    public LoggedInService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /* Get the attributes of the logged in Employee from intent extras and store them in an
         * Employee object */
            loggedInEmployee = new Employee(
                intent.getStringExtra("key"),
                intent.getStringExtra("firstName"),
                intent.getStringExtra("lastName"),
                intent.getStringExtra("username"),
                intent.getStringExtra("password"),
                intent.getStringExtra("role"));

        // Log the Employee's login in the database.
        LogFirebaseHelper.save(new Log(
                getString(R.string.log_user_login, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                new Date()
        ));

        // Initialize DatabaseReference.
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Set isLoggedIn attribute of the logged in Employee in the database as true.
        databaseReference.child("Employees").child(loggedInEmployee.getKey()).child("isLoggedIn").setValue(true);

        /* Define and attach a ChildEventListener to the selected Employee object in the database to
         * go back to the LoginActivity when the selected Employee object is modified or deleted from
         * the database. */
        changedEmployeeListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // Log the Employee's logout in the database.
                LogFirebaseHelper.save(new Log(
                        getString(R.string.log_user_logout, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                        new Date()
                ));

                // Set isLoggedIn attribute of the logged in Employee in the database as false.
                databaseReference.child("Employees").child(loggedInEmployee.getKey()).child("isLoggedIn").setValue(false);

                // Print Toast.
                Toast.makeText(LoggedInService.this, R.string.toast_employee_account_modified, Toast.LENGTH_SHORT).show();

                // Go back to LoginActivity.
                Intent intent1 = new Intent(LoggedInService.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                // Log the Employee's logout in the database.
                LogFirebaseHelper.save(new Log(
                        getString(R.string.log_user_logout, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                        new Date()
                ));

                // Set isLoggedIn attribute of the logged in Employee in the database as false.
                databaseReference.child("Employees").child(loggedInEmployee.getKey()).child("isLoggedIn").setValue(false);

                // Print Toast.
                Toast.makeText(LoggedInService.this, R.string.toast_employee_account_deleted, Toast.LENGTH_SHORT).show();

                // Go back to LoginActivity.
                Intent intent1 = new Intent(LoggedInService.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Employees").child(loggedInEmployee.getKey()).addChildEventListener(changedEmployeeListener);

        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        // Log the Employee's logout in the database.
        LogFirebaseHelper.save(new Log(
                getString(R.string.log_user_logout, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                new Date()
        ));

        // Remove ChildEventListener.
        databaseReference.child("Employees").child(loggedInEmployee.getKey()).removeEventListener(changedEmployeeListener);

        // Set isLoggedIn attribute of the logged in Employee in the database as false.
        databaseReference.child("Employees").child(loggedInEmployee.getKey()).child("isLoggedIn").setValue(false);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {

        // Log the Employee's logout in the database.
        LogFirebaseHelper.save(new Log(
                getString(R.string.log_user_logout, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                new Date()
        ));

        // Remove ChildEventListener.
        databaseReference.child("Employees").child(loggedInEmployee.getKey()).removeEventListener(changedEmployeeListener);

        // Set isLoggedIn attribute of the logged in Employee in the database as false.
        databaseReference.child("Employees").child(loggedInEmployee.getKey()).child("isLoggedIn").setValue(false);

        super.onDestroy();
    }
}
