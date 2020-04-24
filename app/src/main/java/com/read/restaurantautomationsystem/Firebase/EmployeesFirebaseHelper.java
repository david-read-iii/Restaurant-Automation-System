package com.read.restaurantautomationsystem.Firebase;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Models.Employee;

public class EmployeesFirebaseHelper {

    /**
     * Saves a new Employee object to the database.
     *
     * @param employee The Employee object to be saved. Must have a null key.
     * @return The status of the save:
     *              0 indicates successful save
     *              1 indicates a failed save due to database error
     *              2 indicates a failed save due to an attribute with invalid text
     */
    public static int save(Employee employee) {
        int status;

        // Verify that the User has valid attributes defined.
        if (employee == null) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            status = 2;
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
     * @param employee The Employee object to be deleted. Must have a key defined.
     * @return The status of the deletion:
     *              0 indicates successful deletion
     *              1 indicates a failed deletion due to database error
     */
    public static int delete(Employee employee) {
        int status;

        // Attempt to delete the Employee object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Employees").child(employee.getKey()).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Deletes an existing Employee object from the database and replaces it with a new Employee object.
     *
     * @param oldEmployee The Employee object to be deleted. Must have a key defined.
     * @param newEmployee The Employee object to replace the deleted. Must have a null key.
     * @return The status of the modification:
     *              0 indicates successful modification
     *              1 indicates a failed modification due to database error in the deletion step
     *              2 indicates a failed modification due to an attribute with invalid text
     *              3 indicates a failed modification due to database error in the save step
     */
    public static int modify(Employee oldEmployee, Employee newEmployee) {
        int status;

        // Save the newEmployee object to the database.
        status = EmployeesFirebaseHelper.save(newEmployee);

        // If save is successful, delete the oldEmployee object from the database.
        if (status == 0) {
            status = EmployeesFirebaseHelper.delete(oldEmployee);
        } else {
            status = 3;
        }

        return status;
    }
}
