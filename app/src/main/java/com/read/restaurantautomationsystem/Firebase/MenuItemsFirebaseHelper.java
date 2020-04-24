package com.read.restaurantautomationsystem.Firebase;

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
     * @param menuItem The MenuItem object to be deleted. Must have a key defined.
     * @return The status of the deletion: 0 indicates successful deletion, 1 indicates a failed
     * deletion due to database error.
     */
    public static int delete(MenuItem menuItem) {
        int status;

        // Attempt to delete the MenuItem object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("MenuItems").child(menuItem.getKey()).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Deletes an existing MenuItem object from the database and replaces it with a new MenuItem object.
     *
     * @param oldMenuItem The MenuItem object to be deleted. Must have a key defined.
     * @param newMenuItem The MenuItem object to replace the deleted. Must have a null key.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error in the deletion step, 2 indicates a failed
     * modification due to an attribute with invalid text, 3 indicates a failed modification due to
     * database error in the save step.
     */
    public static int modify(MenuItem oldMenuItem, MenuItem newMenuItem) {
        int status;

        // Save the newMenuItem object to the database.
        status = MenuItemsFirebaseHelper.save(newMenuItem);

        // If save is successful, delete the oldMenuItem object from the database.
        if (status == 0) {
            status = MenuItemsFirebaseHelper.delete(oldMenuItem);
        } else {
            status = 3;
        }

        return status;
    }
}