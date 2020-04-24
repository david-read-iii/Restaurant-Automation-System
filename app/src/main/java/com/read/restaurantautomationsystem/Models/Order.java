package com.read.restaurantautomationsystem.Models;

public class Order {
    private String key;
    private String status;
    private String totalPrice;
    private String timeOrdered;
    private String tableOrdered;
    private MenuItemWithQuantity[] orderedItems;

    /**
     * Represents an Order with null attributes.
     */
    public Order(){
    }

    /**
     * Represents an Order with the specified attributes and a null key.
     *
     * @param status            The status of the order.
     * @param totalPrice        The total price of the order.
     * @param timeOrdered       The time the order was placed.
     * @param tableOrdered      The table the order was placed from.
     * @param orderedItems      The items ordered and their quantities.
     */

    public Order(String status, String totalPrice, String timeOrdered, String tableOrdered, MenuItemWithQuantity[] orderedItems) {
        this.status = status;
        this.totalPrice = totalPrice;
        this.timeOrdered = timeOrdered;
        this.tableOrdered = tableOrdered;
        this.orderedItems = orderedItems;
    }

    /**
     * Represents an Order with the specified attributes.
     *
     * @param key               The key of the order.
     * @param status            The status of the order.
     * @param totalPrice        The total price of the order.
     * @param timeOrdered       The time the order was placed.
     * @param tableOrdered      The table the order was placed from.
     * @param orderedItems      The items ordered and their quantities.
     */
    public Order(String key, String status, String totalPrice, String timeOrdered, String tableOrdered, MenuItemWithQuantity[] orderedItems) {
        this.key = key;
        this.status = status;
        this.totalPrice = totalPrice;
        this.timeOrdered = timeOrdered;
        this.tableOrdered = tableOrdered;
        this.orderedItems = orderedItems;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTimeOrdered() {
        return timeOrdered;
    }

    public void setTimeOrdered(String timeOrdered) {
        this.timeOrdered = timeOrdered;
    }

    public String getTableOrdered() {
        return tableOrdered;
    }

    public void setTableOrdered(String tableOrdered) {
        this.tableOrdered = tableOrdered;
    }

    public MenuItemWithQuantity[] getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(MenuItemWithQuantity[] orderedItems) {
        this.orderedItems = orderedItems;
    }
}
