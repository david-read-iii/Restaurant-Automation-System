package com.read.restaurantautomationsystem.Firebase.Helpers;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Models.InventoryItem;

public class InventoryItemsFirebaseHelper {

    /**
     * Saves a new InventoryItem object to the database.
     *
     * @param inventoryItem The InventoryItem object to be saved. Must have a null key.
     * @return The status of the save: 0 indicates successful save, 1 indicates a failed save due to
     * database error, 2 indicates a failed save due to an attribute with invalid text.
     */
    public static int save(InventoryItem inventoryItem) {
        int status;

        // Verify that the InventoryItem object has valid attributes defined.
        if (inventoryItem == null) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            status = 2;
        }
        // Attempt to save InventoryItem object to the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("InventoryItems").push().setValue(inventoryItem);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }
        return status;
    }

    /**
     * Deletes an InventoryItem object from the database.
     *
     * @param inventoryItem The InventoryItem object to be deleted. Must have a key defined.
     * @return The status of the deletion: 0 indicates successful deletion, 1 indicates a failed
     * deletion due to database error.
     */
    public static int delete(InventoryItem inventoryItem) {
        int status;

        // Attempt to delete the InventoryItem object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("InventoryItems").child(inventoryItem.getKey()).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Deletes an existing InventoryItem object from the database and replaces it with a new InventoryItem object.
     *
     * @param oldInventoryItem The InventoryItem object to be deleted. Must have a key defined.
     * @param newInventoryItem The InventoryItem object to replace the deleted. Must have a null key.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error in the deletion step, 2 indicates a failed
     * modification due to an attribute with invalid text, 3 indicates a failed modification due to
     * database error in the save step.
     */
    public static int modify(InventoryItem oldInventoryItem, InventoryItem newInventoryItem) {
        int status;

        // Save the newInventoryItem object to the database.
        status = InventoryItemsFirebaseHelper.save(newInventoryItem);

        // If save is successful, delete the oldInventoryItem object from the database.
        if (status == 0) {
            status = InventoryItemsFirebaseHelper.delete(oldInventoryItem);
        } else {
            status = 3;
        }

        return status;
    }
}
