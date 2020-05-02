package com.read.restaurantautomationsystem.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoggedInService extends Service {

    String key;

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
        key = intent.getStringExtra("key");
        Toast.makeText(this, "Employee logged in: Key: " + key, Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this, "Employee logged out (app killed): Key: " + key, Toast.LENGTH_SHORT).show();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Employee logged out: Key: " + key, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
