package com.read.restaurantautomationsystem.Firebase.ChildEventListeners;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.read.restaurantautomationsystem.Models.Table;

public class ServeTablesChildEventListener implements ChildEventListener {

    private Context context;
    private AlertDialog tableOptionsDialog, tableStatusDialog;
    private String toastMessageChildModified, toastMessageChildDeleted, selectedKey;
    private DatabaseReference databaseReference;

    /**
     * Defines a listener that, when attached to a particular Table object of the database, will
     * close the dialogs the user has open if the object is changed or removed. This listener
     * will remove itself after executing. However, if not executed, it still needs to be removed
     * manually.
     */
    public ServeTablesChildEventListener(Context context, AlertDialog tableOptionsDialog, AlertDialog tableStatusDialog, String toastMessageChildModified, String toastMessageChildDeleted, String selectedKey, DatabaseReference databaseReference) {
        this.context = context;
        this.tableOptionsDialog = tableOptionsDialog;
        this.tableStatusDialog = tableStatusDialog;
        this.toastMessageChildModified = toastMessageChildModified;
        this.toastMessageChildDeleted = toastMessageChildDeleted;
        this.selectedKey = selectedKey;
        this.databaseReference = databaseReference;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (tableOptionsDialog != null) {
            tableOptionsDialog.hide();
        }
        if (tableStatusDialog != null) {
            tableStatusDialog.hide();
        }
        Toast.makeText(context, toastMessageChildModified, Toast.LENGTH_SHORT).show();
        databaseReference.child("Tables").child(selectedKey).removeEventListener(this);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        if (tableOptionsDialog != null) {
            tableOptionsDialog.hide();
        }
        if (tableStatusDialog != null) {
            tableStatusDialog.hide();
        }
        Toast.makeText(context, toastMessageChildDeleted, Toast.LENGTH_SHORT).show();
        databaseReference.child("Tables").child(selectedKey).removeEventListener(this);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
