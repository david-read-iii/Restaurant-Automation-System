package com.read.restaurantautomationsystem.Models;

public class Employee {

    private String key;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;

    /**
     * Represents an Employee with null attributes.
     */
    public Employee() {
    }

    /**
     * Represents a Employee with the specified attributes and a null key.
     *
     * @param firstName The first name of the Employee.
     * @param lastName  The last name of the Employee.
     * @param username  The username of the Employee.
     * @param password  The password of the Employee.
     * @param role      The role of the Employee.
     */
    public Employee(String firstName, String lastName, String username, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Represents a Employee with the specified attributes.
     *
     * @param key       The unique key of the Employee.
     * @param firstName The first name of the Employee.
     * @param lastName  The last name of the Employee.
     * @param username  The username of the Employee.
     * @param password  The password of the Employee.
     * @param role      The role of the Employee.
     */
    public Employee(String key, String firstName, String lastName, String username, String password, String role) {
        this.key = key;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
