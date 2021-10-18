package com.davidread.restaurantautomationsystem.Comparators;

import com.davidread.restaurantautomationsystem.Models.MenuItem;

import java.util.Comparator;

public class MenuItemsComparator implements Comparator<MenuItem> {

    /**
     * Defines a sorting scheme, in which MenuItem objects are sorted alphabetically by their name.
     */
    @Override
    public int compare(MenuItem m1, MenuItem m2) {
        return m1.getName().compareToIgnoreCase(m2.getName());
    }
}
