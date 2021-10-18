package com.davidread.restaurantautomationsystem.Firebase.ChildEventListeners;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class GenericChildEventListener implements ChildEventListener {

    private Context context;
    private String toastMessageChildModified, toastMessageChildDeleted;
    private Boolean ranOnce;

    /**
     * Defines a listener that, when attached to a particular child of the database, will close the
     * activity the user is in if that child is changed or removed.
     */
    public GenericChildEventListener(Context context, String toastMessageChildModified, String toastMessageChildDeleted) {
        this.context = context;
        this.toastMessageChildModified = toastMessageChildModified;
        this.toastMessageChildDeleted = toastMessageChildDeleted;
        this.ranOnce = false;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (!ranOnce) {
            ((Activity) context).finish();
            Toast.makeText(context, toastMessageChildModified, Toast.LENGTH_SHORT).show();
            ranOnce = true;
        }

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        if (!ranOnce) {
            ((Activity) context).finish();
            Toast.makeText(context, toastMessageChildDeleted, Toast.LENGTH_SHORT).show();
            ranOnce = true;
        }

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}