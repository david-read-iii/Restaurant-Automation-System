package com.read.restaurantautomationsystem.Firebase.ChildEventListeners;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class AddOrderChildEventListener implements ChildEventListener {

    private Context context;

    /**
     * Defines a listener that closes the AddOrderActivity if the MenuItems collection of the
     * database is changed.
     */
    public AddOrderChildEventListener(Context context) {
        this.context = context;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Toast.makeText(context, "Changed", Toast.LENGTH_SHORT).show();
        ((Activity) context).finish();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
        ((Activity) context).finish();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Toast.makeText(context, "Moved", Toast.LENGTH_SHORT).show();
        ((Activity) context).finish();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
