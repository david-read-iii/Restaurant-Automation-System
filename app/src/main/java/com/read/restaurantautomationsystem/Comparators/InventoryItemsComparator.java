package com.read.restaurantautomationsystem.Comparators;

import com.read.restaurantautomationsystem.Models.InventoryItem;

import java.util.Comparator;

public class InventoryItemsComparator implements Comparator<InventoryItem> {

    /**
     * Defines a sorting scheme, in which InventoryItem objects are sorted alphabetically by their
     * name.
     */
    @Override
    public int compare(InventoryItem i1, InventoryItem i2) {
        return i1.getName().compareToIgnoreCase(i2.getName());
    }
}
