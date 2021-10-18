package com.davidread.restaurantautomationsystem.Models;

public class MenuItemWithQuantity {

    private MenuItem menuItem;
    private int quantity;
    private double totalPrice;

    /**
     * Represents a MenuItemWithQuantity with null attributes.
     */
    public MenuItemWithQuantity() {
    }

    /**
     * Represents a MenuItemWithQuantity with the specified attributes.
     *
     * @param menuItem   The MenuItem object.
     * @param quantity   The quantity of the MenuItem object.
     * @param totalPrice The total price of the MenuItem object.
     */
    public MenuItemWithQuantity(MenuItem menuItem, int quantity, double totalPrice) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
