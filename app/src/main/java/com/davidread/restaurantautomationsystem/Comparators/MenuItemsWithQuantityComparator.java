package com.davidread.restaurantautomationsystem.Comparators;

import com.davidread.restaurantautomationsystem.Models.MenuItemWithQuantity;

import java.util.Comparator;

public class MenuItemsWithQuantityComparator implements Comparator<MenuItemWithQuantity> {

    /**
     * Defines a sorting scheme, in which MenuItemWithQuantity objects are sorted alphabetically by
     * their name.
     */
    @Override
    public int compare(MenuItemWithQuantity m1, MenuItemWithQuantity m2) {
        return m1.getMenuItem().getName().compareToIgnoreCase(m2.getMenuItem().getName());
    }
}
