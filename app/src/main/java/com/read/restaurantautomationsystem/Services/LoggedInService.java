package com.read.restaurantautomationsystem.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.read.restaurantautomationsystem.Firebase.Helpers.LogFirebaseHelper;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.Models.Log;
import com.read.restaurantautomationsystem.R;

import java.util.Date;

public class LoggedInService extends Service {

    Employee loggedInEmployee;

    // TODO: Implement this Service once Firebase function is defined and working.

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

        // Log the Employee's login in the database..
        LogFirebaseHelper.save(new Log(
                getString(R.string.log_user_login, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                new Date()
        ));

        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        // Log the Employee's logout in the database.
        LogFirebaseHelper.save(new Log(
                getString(R.string.log_user_logout, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                new Date()
        ));

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {

        // Log the Employee's logout in the database.
        LogFirebaseHelper.save(new Log(
                getString(R.string.log_user_logout, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()),
                new Date()
        ));

        super.onDestroy();
    }
}
