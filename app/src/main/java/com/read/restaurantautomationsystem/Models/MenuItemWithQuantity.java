package com.read.restaurantautomationsystem.Models;

public class MenuItemWithQuantity {

    MenuItem menuItem;
    String quantity;
    String totalPrice;

    /**
     * Represents a MenuItemWithQuantity with null attributes.
     */
    public MenuItemWithQuantity() {
    }

    /**
     * Represents a MenuItemWithQuantity with the specified attributes.
     *
     * @param menuItem The MenuItem.
     * @param quantity The quantity of the item.
     */
    public MenuItemWithQuantity(MenuItem menuItem, String quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.totalPrice = Double.toString(Double.parseDouble(quantity) * Double.parseDouble(menuItem.getPrice()));
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        this.totalPrice = Double.toString(Double.parseDouble(quantity) * Double.parseDouble(menuItem.getPrice()));
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
