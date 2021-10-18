package com.davidread.restaurantautomationsystem.Models;

public class MenuItem {

    private String key;
    private String name;
    private double price;
    private String category;

    /**
     * Represents a MenuItem object with null attributes.
     */
    public MenuItem() {
    }

    /**
     * Represents a MenuItem object with the specified attributes and a null key.
     *
     * @param name     The name of the item.
     * @param price    The price of the item.
     * @param category The category of the item.
     */
    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    /**
     * Represents a MenuItem object with the specified attributes.
     *
     * @param key      The key of the item.
     * @param name     The name of the item.
     * @param price    The price of the item.
     * @param category The category of the item.
     */
    public MenuItem(String key, String name, double price, String category) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
