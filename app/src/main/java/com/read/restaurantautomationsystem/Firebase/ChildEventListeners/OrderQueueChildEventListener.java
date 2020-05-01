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

public class OrderQueueChildEventListener implements ChildEventListener {

    private Context context;
    private AlertDialog orderOptionsDialog, orderStatusDialog;
    private String toastMessageChildModified, toastMessageChildDeleted, selectedKey;
    private DatabaseReference databaseReference;

    /**
     * Defines a listener that, when attached to a particular Order object of the database, will
     * close the dialogs the user has open if the object is changed or removed. This listener
     * will remove itself after executing. However, if not executed, it still needs to be removed
     * manually.
     */
    public OrderQueueChildEventListener(Context context, AlertDialog orderOptionsDialog, AlertDialog orderStatusDialog, String toastMessageChildModified, String toastMessageChildDeleted, String selectedKey, DatabaseReference databaseReference) {
        this.context = context;
        this.orderOptionsDialog = orderOptionsDialog;
        this.orderStatusDialog = orderStatusDialog;
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
        if (orderOptionsDialog != null) {
            orderOptionsDialog.hide();
        }
        if (orderStatusDialog != null) {
            orderStatusDialog.hide();
        }
        Toast.makeText(context, toastMessageChildModified, Toast.LENGTH_SHORT).show();
        databaseReference.child("OrderQueue").child(selectedKey).removeEventListener(this);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        if (orderOptionsDialog != null) {
            orderOptionsDialog.hide();
        }
        if (orderStatusDialog != null) {
            orderStatusDialog.hide();
        }
        Toast.makeText(context, toastMessageChildDeleted, Toast.LENGTH_SHORT).show();
        databaseReference.child("OrderQueue").child(selectedKey).removeEventListener(this);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
