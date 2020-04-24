package com.read.restaurantautomationsystem.Firebase;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Models.Table;

public class TableFirebaseHelper {
    /**
     * Saves a new Table object to the database.
     *
     * @param table The Table object to be saved. Must have a null key.
     * @return The status of the save:
     *              0 indicates successful save
     *              1 indicates a failed save due to database error
     *              2 indicates a failed save due to an attribute with invalid text
     */
    public static int save(Table table) {
        int status;

        // Verify that the User has valid attributes defined.
        if (table == null) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            status = 2;
        }
        // Attempt to save Table object to the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Tables").push().setValue(table);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }
        return status;
    }


    /**
     * Deletes a Table object from the database.
     *
     * @param table The Table object to be deleted. Must have a key defined.
     * @return The status of the deletion:
     *              0 indicates successful deletion
     *              1 indicates a failed deletion due to database error
     */
    public static int delete(Table table) {
        int status;

        // Attempt to delete the Table object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Tables").child(table.getKey()).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }



    /**
     * Deletes an existing Table object from the database and replaces it with a new Table object.
     *
     * @param oldTable The Table object to be deleted. Must have a key defined.
     * @param newTable The Table object to replace the deleted. Must have a null key.
     * @return The status of the modification:
     *              0 indicates successful modification
     *              1 indicates a failed modification due to database error in the deletion step
     *              2 indicates a failed modification due to an attribute with invalid text
     *              3 indicates a failed modification due to database error in the save step
     */
    public static int modify(Table oldTable, Table newTable) {
        int status;

        // Save the newTable object to the database.
        status = TableFirebaseHelper.save(newTable);

        // If save is successful, delete the oldTable object from the database.
        if (status == 0) {
            status = TableFirebaseHelper.delete(oldTable);
        } else {
            status = 3;
        }

        return status;
    }
}