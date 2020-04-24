package com.read.restaurantautomationsystem.Firebase;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Models.InventoryItem;

public class InventoryFirebaseHelper {
    /**
     * Saves a new Inventory object to the database.
     *
     * @param inventory The Inventory object to be saved. Must have a null key.
     * @return The status of the save:
     *              0 indicates successful save
     *              1 indicates a failed save due to database error
     *              2 indicates a failed save due to an attribute with invalid text
     */
    public static int save(InventoryItem inventory) {
        int status;

        // Verify that the User has valid attributes defined.
        if (inventory == null) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            status = 2;
        }
        // Attempt to save InventoryItem object to the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("InventoryItems").push().setValue(inventory);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }
        return status;
    }


    /**
     * Deletes an Inventory object from the database.
     *
     * @param inventory The Inventory object to be deleted. Must have a key defined.
     * @return The status of the deletion:
     *              0 indicates successful deletion
     *              1 indicates a failed deletion due to database error
     */
    public static int delete(InventoryItem inventory) {
        int status;

        // Attempt to delete the InventoryItem object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("InventoryItems").child(inventory.getKey()).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }



    /**
     * Deletes an existing InventoryItem object from the database and replaces it with a new Employee object.
     *
     * @param oldInventory The Employee object to be deleted. Must have a key defined.
     * @param newInventory The Employee object to replace the deleted. Must have a null key.
     * @return The status of the modification:
     *              0 indicates successful modification
     *              1 indicates a failed modification due to database error in the deletion step
     *              2 indicates a failed modification due to an attribute with invalid text
     *              3 indicates a failed modification due to database error in the save step
     */
    public static int modify(InventoryItem oldInventory, InventoryItem newInventory) {
        int status;

        // Save the newInventory object to the database.
        status = InventoryFirebaseHelper.save(newInventory);

        // If save is successful, delete the oldInventory object from the database.
        if (status == 0) {
            status = InventoryFirebaseHelper.delete(oldInventory);
        } else {
            status = 3;
        }

        return status;
    }
}
