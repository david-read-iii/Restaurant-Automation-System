package com.davidread.restaurantautomationsystem.Models;

public class InventoryItem {

    private String key;
    private String name;
    private int quantity;

    /**
     * Represents a InventoryItem object with null attributes.
     */
    public InventoryItem() {
    }

    /**
     * Represents a InventoryItem object with the specified attributes and a null key.
     *
     * @param name     The name of the InventoryItem.
     * @param quantity The quantity of the InventoryItem.
     */
    public InventoryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * Represents a InventoryItem object with the specified attributes.
     *
     * @param key      The unique key of the InventoryItem.
     * @param name     The name of the InventoryItem.
     * @param quantity The quantity of the InventoryItem.
     */
    public InventoryItem(String key, String name, int quantity) {
        this.key = key;
        this.name = name;
        this.quantity = quantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
