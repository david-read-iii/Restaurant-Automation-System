package com.read.restaurantautomationsystem.Firebase.Helpers;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Models.MenuItem;

public class MenuItemsFirebaseHelper {

    /**
     * Saves a new MenuItem object to the database.
     *
     * @param menuItem The MenuItem object to be saved. Must have a null key.
     * @return The status of the save: 0 indicates successful save, 1 indicates a failed save due to
     * database error, 2 indicates a failed save due to an attribute with invalid text.
     */
    public static int save(MenuItem menuItem) {
        int status;

        // Verify that the MenuItem object has valid attributes defined.
        if (menuItem == null) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            status = 2;
        }
        // Attempt to save MenuItem object to the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("MenuItems").push().setValue(menuItem);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }
        return status;
    }

    /**
     * Deletes a MenuItem object from the database.
     *
     * @param key The key of the MenuItem object to be deleted.
     * @return The status of the deletion: 0 indicates successful deletion, 1 indicates a failed
     * deletion due to database error.
     */
    public static int delete(String key) {
        int status;

        // Attempt to delete the MenuItem object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("MenuItems").child(key).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Modifies an existing MenuItem object with new attributes.
     *
     * @param key The key of the MenuItem object to be modified.
     * @param menuItem The MenuItem object with the new attributes defined. Must have a null key.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error, 2 indicates a failed
     * modification due to an attribute with invalid text.
     */
    public static int modify(String key, MenuItem menuItem) {
        int status;

        // Verify that the MenuItem object has valid attributes defined.
        if (menuItem == null) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            status = 2;
        }
        // Attempt to modify the MenuItem object in the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("MenuItems").child(key).setValue(menuItem);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }

        return status;
    }
}