package com.read.restaurantautomationsystem.Firebase.Helpers;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.Models.MenuItemWithQuantity;
import com.read.restaurantautomationsystem.Models.Order;

import java.util.ArrayList;
import java.util.Date;

public class OrdersFirebaseHelper {

    /**
     * Saves a new Order object to the database.
     *
     * @param order The Order object to be saved. Must have a null key.
     * @return The status of the save: 0 indicates successful save, 1 indicates a failed save due to
     * database error, 2 indicates a failed save due to an attribute with invalid text.
     */
    public static int save(Order order) {
        int status;

        // Verify that the MenuItem object has valid attributes defined.
        if (order.getOrderedMenuItemsWithQuantity().isEmpty()) {
            status = 2;
        }
        // Attempt to save Order object to the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("OrderQueue").push().setValue(order);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }
        return status;
    }

    /**
     * Deletes an Order object from the database.
     *
     * @param key The key of the Order object to be deleted.
     * @return The status of the deletion: 0 indicates successful deletion, 1 indicates a failed
     * deletion due to database error.
     */
    public static int delete(String key) {
        int status;

        // Attempt to delete the MenuItem object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("OrderQueue").child(key).removeValue();
            status = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            status = 1;
        }

        return status;
    }

    /**
     * Modifies an existing Order object with new attributes.
     *
     * @param key The key of the Order object to be modified.
     * @param order The Order object with the new attributes defined. Must have a null key.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error, 2 indicates a failed
     * modification due to an attribute with invalid text.
     */
    public static int modify(String key, Order order) {
        int status;

        // Verify that the Order object has valid attributes defined.
        if (order.getOrderedMenuItemsWithQuantity().isEmpty()) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            status = 2;
        }
        // Attempt to modify the Order object in the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("OrderQueue").child(key).setValue(order);
                status = 0;
            } catch (DatabaseException e) {
                e.printStackTrace();
                status = 1;
            }
        }

        return status;
    }

    /**
     * Modifies the status of an existing Order object.
     *
     * @param key The key of the Order object to be modified.
     * @param status The status to be set upon the Order object.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error.
     */
    public static int modifyStatus(String key, String status) {
        int returnStatus;

        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("OrderQueue").child(key).child("status").setValue(status);
            returnStatus = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            returnStatus = 1;
        }

        return returnStatus;
    }

    public static int moveToCompletedOrders(final String key) {
        final int[] status = new int[1];
        final Order order = new Order();

        // Get attributes of Order object from OrderQueue collection of the database.
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("OrderQueue").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                order.setNumber(dataSnapshot.child("number").getValue(Integer.class));
                order.setStatus(dataSnapshot.child("status").getValue(String.class));
                order.setTotalPrice(dataSnapshot.child("totalPrice").getValue(Double.class));
                order.setDateTimeOrdered(dataSnapshot.child("dateTimeOrdered").getValue(Date.class));
                order.setTableNameOrdered(dataSnapshot.child("tableNameOrdered").getValue(String.class));

                ArrayList<MenuItemWithQuantity> orderedMenuItemsWithQuantity = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.child("orderedMenuItemsWithQuantity").getChildren()) {
                    MenuItemWithQuantity menuItemWithQuantity = new MenuItemWithQuantity(
                            new MenuItem(
                                    ds.child("menuItem").child("key").getValue(String.class),
                                    ds.child("menuItem").child("name").getValue(String.class),
                                    ds.child("menuItem").child("price").getValue(Double.class),
                                    ds.child("menuItem").child("category").getValue(String.class)
                            ),
                            ds.child("quantity").getValue(Integer.class),
                            ds.child("totalPrice").getValue(Double.class)
                    );
                    orderedMenuItemsWithQuantity.add(menuItemWithQuantity);
                }

                order.setOrderedMenuItemsWithQuantity(orderedMenuItemsWithQuantity);

                // Delete the Order object from the database.
                OrdersFirebaseHelper.delete(key);

                // Attempt to save Order object to the database. Watch for a DatabaseException.
                try {
                    databaseReference.child("CompletedOrders").child(key).setValue(order);
                    status[0] = 0;
                } catch (DatabaseException e) {
                    e.printStackTrace();
                    status[0] = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return status[0];
    }
}
