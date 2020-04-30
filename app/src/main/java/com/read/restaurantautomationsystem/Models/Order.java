package com.read.restaurantautomationsystem.Models;

import java.util.ArrayList;
import java.util.Date;

public class Order {

    private String key;
    private int orderNumber;
    private String status;
    private double totalPrice;
    private Date dateTimeOrdered;
    private String tableNameOrdered;
    private ArrayList<MenuItemWithQuantity> orderedItems;

    /**
     * Represents an Order with null attributes.
     */
    public Order() {
    }

    /**
     * Represents an Order with the specified attributes and a null key. The double totalPrice represents
     * the total price of this Order object.
     *
     * @param status           The status of the order.
     * @param totalPrice       The total price of the order.
     * @param dateTimeOrdered  The time the order was placed.
     * @param tableNameOrdered The table the order was placed from.
     * @param orderedItems     The items ordered and their quantities.
     */


    public Order(int orderNumber, String status, double totalPrice, Date dateTimeOrdered, String tableNameOrdered, ArrayList<MenuItemWithQuantity> orderedItems) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.totalPrice = totalPrice;
        this.dateTimeOrdered = dateTimeOrdered;
        this.tableNameOrdered = tableNameOrdered;
        this.orderedItems = orderedItems;
    }

    /**
     * Represents an Order with the specified attributes.
     *
     * @param key              The key of the order.
     * @param status           The status of the order.
     * @param totalPrice       The total price of the order.
     * @param dateTimeOrdered  The time the order was placed.
     * @param tableNameOrdered The table the order was placed from.
     * @param orderedItems     The items ordered and their quantities.
     */
    public Order(String key, int orderNumber, String status, double totalPrice, Date dateTimeOrdered, String tableNameOrdered, ArrayList<MenuItemWithQuantity> orderedItems) {
        this.key = key;
        this.orderNumber = orderNumber;
        this.status = status;
        this.totalPrice = totalPrice;
        this.dateTimeOrdered = dateTimeOrdered;
        this.tableNameOrdered = tableNameOrdered;
        this.orderedItems = orderedItems;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDateTimeOrdered() {
        return dateTimeOrdered;
    }

    public void setDateTimeOrdered(Date dateTimeOrdered) {
        this.dateTimeOrdered = dateTimeOrdered;
    }

    public String getTableNameOrdered() {
        return tableNameOrdered;
    }

    public void setTableNameOrdered(String tableNameOrdered) {
        this.tableNameOrdered = tableNameOrdered;
    }

    public ArrayList<MenuItemWithQuantity> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(ArrayList<MenuItemWithQuantity> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
