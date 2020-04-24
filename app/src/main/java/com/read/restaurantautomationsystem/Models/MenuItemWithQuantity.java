package com.read.restaurantautomationsystem.Models;

public class MenuItemWithQuantity {
    MenuItem menuItem;
    String quantity;
    String key;

    /**
     * Represents a MenuItemWithQuantity with null attributes.
     */
    public MenuItemWithQuantity(){
    }

    /**
     * Represents a MenuItemWithQuantity with the specified attributes and a null key.
     *
     * @param menuItem      The MenuItem.
     * @param quantity      The quantity of the item.
     */
    public MenuItemWithQuantity(MenuItem menuItem, String quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    /**
     * Represents a MenuItemWithQuantity with the specified attributes.
     *
     * @param menuItem      The MenuItem.
     * @param quantity      The quantity of the item.
     * @param key           the key of the MenuItemWithQuantity.
     */
    public MenuItemWithQuantity(MenuItem menuItem, String quantity, String key) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.key = key;
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
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
