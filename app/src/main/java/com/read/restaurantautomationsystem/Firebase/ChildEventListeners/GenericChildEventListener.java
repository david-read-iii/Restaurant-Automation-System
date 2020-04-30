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
    private String toastMessageChildModified, toastMessageChildDeleted;

    /**
     * Defines a listener that executes code when a certain child in the database changed or removed.
     * If this object is created with this constructor, the activity the user is in will close on
     * execution of this listener.
     */
    public GenericChildEventListener(Context context, String toastMessageChildModified, String toastMessageChildDeleted) {
        this.context = context;
        this.toastMessageChildModified = toastMessageChildModified;
        this.toastMessageChildDeleted = toastMessageChildDeleted;
    }

    /**
     * Defines a listener that executes code when a certain child in the database changed or removed.
     * If this object is created with this constructor, the dialog the user is in will close on
     * execution of this listener.
     */
    public GenericChildEventListener(Context context, AlertDialog alertDialog, String toastMessageChildModified, String toastMessageChildDeleted) {
        this.context = context;
        this.alertDialog = alertDialog;
        this.toastMessageChildModified = toastMessageChildModified;
        this.toastMessageChildDeleted = toastMessageChildDeleted;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        Toast.makeText(context, toastMessageChildModified, Toast.LENGTH_SHORT).show();

        if (alertDialog != null) {
            alertDialog.hide();
        } else {
            ((Activity) context).finish();
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        Toast.makeText(context, toastMessageChildDeleted, Toast.LENGTH_SHORT).show();

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