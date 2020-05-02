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
     * database error, 2 indicates a failed save due to at least one attribute being blank, 3
     * indicates a failed save due to a non-unique username attribute.
     */
    public static int save(Employee employee) {
        int status;

        // If an Employee object with blank attributes is blank, do not save the object.
        if (employee.getFirstName().equals("") || employee.getLastName().equals("") || employee.getUsername().equals("") || employee.getPassword().equals("")) {
            status = 2;
        }
        // If an Employee object has a non-unique username, do not save the object.
        else if (!isUsernameUnique(employee.getUsername())) {
            status = 3;
        }
        // Attempt to save Employee object to the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Employees").push().setValue(employee);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
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
     * failed modification due to database error, 2 indicates a failed modification due to at least
     * one attribute being blank, 3 indicates a failed modification due to a non-unique username
     * attribute.
     */
    public static int modify(String key, String oldUsername, Employee employee) {
        int status;

        // If an Employee object with blank attributes is blank, do not save the object.
        if (employee.getFirstName().equals("") || employee.getLastName().equals("") || employee.getUsername().equals("") || employee.getPassword().equals("")) {
            status = 2;
        }
        // If an Employee object has a non-unique username, do not save the object.
        else if (!oldUsername.equals(employee.getUsername()) && !isUsernameUnique(employee.getUsername())) {
            status = 3;
        }
        // Attempt to modify the Employee object in the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Employees").child(key).setValue(employee);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }

        return status;
    }

    /**
     * Returns true if passed username is unique and not already used in the database.
     */
    public static Boolean isUsernameUnique(String username) {
        // TODO: Implement...
        return true;
    }
}
