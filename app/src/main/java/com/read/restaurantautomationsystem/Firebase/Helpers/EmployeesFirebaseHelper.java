package com.read.restaurantautomationsystem.Firebase.Helpers;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Models.Employee;

public class EmployeesFirebaseHelper {

    /**
     * Saves a new Employee object to the database.
     *
     * @param employee The Employee object to be saved. Must have a null key.
     * @return The status of the save: 0 indicates successful save, 1 indicates a failed save due to
     * database error.
     */
    public static int save(Employee employee) {
        int status;

        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Employees").push().setValue(employee);
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Deletes an Employee object from the database.
     *
     * @param key The key of the Employee object to be deleted.
     * @return The status of the deletion: 0 indicates successful deletion, 1 indicates a failed
     * deletion due to database error
     */
    public static int delete(String key) {
        int status;

        // Attempt to delete the Employee object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Employees").child(key).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Modifies an existing Employee object with new attributes.
     *
     * @param key      The key of the Employee object to be modified.
     * @param employee The Employee object with the new attributes defined. Must have a null key.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error.
     */
    public static int modify(String key, Employee employee) {
        int status;

        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Employees").child(key).setValue(employee);
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }
}
