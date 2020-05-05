package com.read.restaurantautomationsystem.Firebase.Helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Models.Employee;

import androidx.annotation.NonNull;

public class EmployeesFirebaseHelper {

    /**
     * Saves a new Employee object to the database.
     *
     * @param employee The Employee object to be saved. Must have a null key.
     * @return The status of the save: 0 indicates successful save, 1 indicates a failed save due to
     * database error.
     */
    public static int save(final Employee employee) {
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
     * failed modification due to database error, 2 indicates a failed modification due to at least
     * one attribute being blank, 3 indicates a failed modification due to a non-unique username
     * attribute.
     */
    public static int modify(final String key, final String oldUsername, final Employee employee) {
        final int[] status = new int[1];

        // If an Employee object with blank attributes is blank, do not save the object.
        if (employee.getFirstName().equals("") || employee.getLastName().equals("") || employee.getUsername().equals("") || employee.getPassword().equals("")) {
            status[0] = 2;
        } else {

            // Query the database under the "Employees" collection for a child with the requested new username.
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query usernameQuery = databaseReference.child("Employees").orderByChild("username").equalTo(employee.getUsername());
            usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // If the query returns at least one child and the old username is not the new username, then an Employee object already uses the username.
                    if (dataSnapshot.hasChildren() && !oldUsername.equals(employee.getUsername())) {
                        status[0] = 3;
                    }
                    // Attempt to modify the Employee object in the database. Watch for a DatabaseException.
                    else {
                        try {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Employees").child(key).setValue(employee);
                            status[0] = 0;
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                            status[0] = 1;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    status[0] = 1;
                }
            });
        }
        return status[0];
    }
}
