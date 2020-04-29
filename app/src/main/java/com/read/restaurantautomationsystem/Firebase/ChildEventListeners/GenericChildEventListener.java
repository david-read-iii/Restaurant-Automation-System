package com.read.restaurantautomationsystem.Firebase.ChildEventListeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class GenericChildEventListener implements ChildEventListener {

    private Context context;
    private AlertDialog alertDialog;
    private String toastMessage;

    /**
     * Defines a listener that executes code when a certain child in the database is added,
     * changed or removed. If this constructor is used, the activity the user is in will close on
     * execution of the listener.
     */
    public GenericChildEventListener(Context context, String toastMessage) {
        this.context = context;
        this.toastMessage = toastMessage;
    }

    /**
     * Defines a listener that executes code when a certain child in the database is added,
     * changed or removed. If this constructor is used, the dialog box the user is in will close on
     * execution of the listener.
     */
    public GenericChildEventListener(Context context, AlertDialog alertDialog, String toastMessage) {
        this.context = context;
        this.alertDialog = alertDialog;
        this.toastMessage = toastMessage;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();

        if (alertDialog != null) {
            alertDialog.hide();
        } else {
            ((Activity) context).finish();
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}