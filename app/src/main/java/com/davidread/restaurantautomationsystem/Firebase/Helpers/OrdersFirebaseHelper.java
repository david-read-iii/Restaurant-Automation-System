package com.davidread.restaurantautomationsystem.Firebase.Helpers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.davidread.restaurantautomationsystem.Models.Log;
import com.davidread.restaurantautomationsystem.Models.MenuItem;
import com.davidread.restaurantautomationsystem.Models.MenuItemWithQuantity;
import com.davidread.restaurantautomationsystem.Models.Order;
import com.davidread.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.Date;

public class OrdersFirebaseHelper {

    /**
     * Saves a new Order object to the database.
     *
     * @param order The Order object to be saved. Must have a null key.
     * @return The status of the save: 0 indicates successful save, 1 indicates a failed save due to
     * database error, 2 indicates a failed save due to an Order being passed with an empty
     * orderedMenuItemsWithQuantity attribute.
     */
    public static int save(final Order order, final String loggedInEmployeeFirstName, final String loggedInEmployeeLastName, final Context context) {
        final int[] status = new int[1];

        /* If the Order object is passed with an empty orderedMenuItemsWithQuantity attribute, do
         * not save the object. */
        if (order.getOrderedMenuItemsWithQuantity().isEmpty()) {
            status[0] = 2;
        }
        /* If the Order object is passed with a nonempty orderedMenuItemsWithQuantity attribute,
         * attempt to save the Order object to the database. Watch for a DatabaseException. */
        else {
            try {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("nextOrderNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get value of nextOrderNumber.
                        int orderNumber = dataSnapshot.getValue(Integer.class);

                        // Increment nextOrderNumber in the database.
                        databaseReference.child("nextOrderNumber").setValue(orderNumber + 1);

                        // Set order number of the Order object.
                        order.setNumber(orderNumber);

                        // Save an Order object with the specified attributes to the database.
                        databaseReference.child("OrderQueue").push().setValue(order);

                        // Log Employee activity.
                        LogFirebaseHelper.save(new Log(
                                context.getString(R.string.log_user_submitted_order, loggedInEmployeeFirstName, loggedInEmployeeLastName, Integer.toString(orderNumber), order.getTableNameOrdered()),
                                new Date()
                        ));

                        status[0] = 0;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        status[0] = 1;
                    }
                });

            } catch (DatabaseException e) {
                e.printStackTrace();
                status[0] = 1;
            }
        }
        return status[0];
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
     * failed modification due to database error, 2 indicates a failed modification due to an
     * attribute with invalid text.
     */
    public static int modify(String key, Order order) {
        int status;

        /* If the Order object is passed with an empty orderedMenuItemsWithQuantity attribute, do
         * not save the object. */
        if (order.getOrderedMenuItemsWithQuantity().isEmpty()) {
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

    /**
     * Moves an Order object with the specified key from the OrderQueue collection of the database
     * to the CompletedOrders collection of the database.
     *
     * @param key The key of the Order object to be moved.
     * @return The status of the modification: 0 indicates successful modification, 1 indicates a
     * failed modification due to database error.
     */
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
                status[0] = 1;
            }
        });

        return status[0];
    }
}
