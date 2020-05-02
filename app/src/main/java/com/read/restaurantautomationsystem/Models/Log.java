package com.read.restaurantautomationsystem.Models;

import java.util.Date;

public class Log {

    private String key;
    private String message;
    private Date dateTimeCompleted;

    /**
     * Represents a Log object with null attributes.
     */
    public Log() {

    }

    /**
     * Represents an Log object with the specified attributes and a null key.
     */
    public Log(String message, Date dateTimeCompleted) {
        this.message = message;
        this.dateTimeCompleted = dateTimeCompleted;
    }

    /**
     * Represents an Log object with the specified attributes.
     */
    public Log(String key, String message, Date dateTimeCompleted) {
        this.key = key;
        this.message = message;
        this.dateTimeCompleted = dateTimeCompleted;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateTimeCompleted() {
        return dateTimeCompleted;
    }

    public void setDateTimeCompleted(Date dateTimeCompleted) {
        this.dateTimeCompleted = dateTimeCompleted;
    }
}
