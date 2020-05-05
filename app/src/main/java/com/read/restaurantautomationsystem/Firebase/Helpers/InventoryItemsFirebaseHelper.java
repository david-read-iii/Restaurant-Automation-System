package com.read.restaurantautomationsystem.Firebase.Helpers;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.read.restaurantautomationsystem.Models.InventoryItem;

import java.util.HashMap;
import java.util.Map;

public class InventoryItemsFirebaseHelper {

    /**
     * Saves a new InventoryItem object to the database.
     *
     * @param inventoryItem The InventoryItem object to be saved. Must have a null key.
     * @return The status of the save: 0 indicates successful save, 1 indicates a failed save due to
     * database error.
     */
    public static int save(final InventoryItem inventoryItem) {
        int status;

        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("InventoryItems").push().setValue(inventoryItem);
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Deletes an InventoryItem object from the database.
     *
     * @param key The key of the InventoryItem object to be deleted.
     * @return The status of the deletion: 0 indicates successful deletion, 1 indicates a failed
     * deletion due to database error.
     */
    public static int delete(String key) {
        int status;

        // Attempt to delete the InventoryItem object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("InventoryItems").child(key).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Modifies an existing InventoryItem object with new attributes.
     *
     * @param key           The key of the InventoryItem object to be modified.
     * @param inventoryItem The InventoryItem object with the new attributes defined. Must have a null key.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error.
     */
    public static int modify(String key, InventoryItem inventoryItem) {
        int status;

        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("InventoryItems").child(key).setValue(inventoryItem);
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }
}

