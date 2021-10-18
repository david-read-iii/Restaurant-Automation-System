package com.davidread.restaurantautomationsystem.Comparators;

import com.davidread.restaurantautomationsystem.Models.Log;

import java.util.Comparator;

public class LogComparator implements Comparator<Log> {

    /**
     * Defines a sorting scheme, in which Log objects are sorted reverse chronologically.
     */
    @Override
    public int compare(Log l1, Log l2) {
        return l2.getDateTimeCompleted().compareTo(l1.getDateTimeCompleted());
    }
}
