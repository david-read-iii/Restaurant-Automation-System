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
     * database error, 2 indicates a failed save due to at least one attribute being blank, 3
     * indicates a failed save due to a non-unique name attribute.
     */
    public static int save(InventoryItem inventoryItem) {
        int status;

        // If an InventoryItem object with blank attributes is blank, do not save the object.
        if (inventoryItem.getName().equals("") || inventoryItem.getQuantity() == -1) {
            status = 2;
        }
        // If an InventoryItem object has a non-unique name, do not save the object.
        else if (!isNameUnique(inventoryItem.getName())) {
            status = 3;
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
     * @param key The key of the InventoryItem object to be modified.
     * @param inventoryItem The InventoryItem object with the new attributes defined. Must have a null key.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error, 2 indicates a failed modification due to at least
     * one attribute being blank, 3 indicates a failed modification due to a non-unique name attribute.
     */
    public static int modify(String key, String oldName, InventoryItem inventoryItem) {
        int status;

        // If an InventoryItem object with blank attributes is blank, do not save the object.
        if (inventoryItem.getName().equals("") || inventoryItem.getQuantity() == -1) {
            status = 2;
        }
        // If an InventoryItem object has a non-unique name, do not save the object.
        else if (!oldName.equals(inventoryItem.getName()) && !isNameUnique(inventoryItem.getName())) {
            status = 3;
        }
        // Attempt to modify the InventoryItem object in the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("InventoryItems").child(key).setValue(inventoryItem);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }

        return status;
    }

    /**
     * Returns true if passed name is unique and not already used in the database.
     */
    public static Boolean isNameUnique(String name) {
        // TODO: Implement...
        return true;
    }
}
