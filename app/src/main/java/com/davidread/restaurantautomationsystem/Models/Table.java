package com.davidread.restaurantautomationsystem.Models;

public class Table {

    private String key;
    private String status;
    private String name;

    /**
     * Represents a Table with null attributes.
     */
    public Table() {
    }

    /**
     * Represents a Table with the specified attributes and a null key.
     *
     * @param name   The name of the table.
     * @param status The status of the table.
     */

    public Table(String name, String status) {
        this.name = name;
        this.status = status;
    }

    /**
     * Represents a Table with the specified attributes.
     *
     * @param key    The key of the table.
     * @param name   The name of the table.
     * @param status The status of the table.
     */
    public Table(String key, String name, String status) {
        this.key = key;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
