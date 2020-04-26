package com.read.restaurantautomationsystem.Firebase.ChildEventListeners;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class DialogChildEventListener implements ChildEventListener {

    private Context context;
    private AlertDialog alertDialog;
    private String toastMessage;

    /**
     * Defines a listener that executes code when a certain child in the database is changed. When
     * this happens, the dialog the user has open will close and an indication Toast will be printed.
     */
    public DialogChildEventListener(Context context, AlertDialog alertDialog, String toastMessage) {
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
        alertDialog.hide();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
