package com.davidread.restaurantautomationsystem.Comparators;

import com.davidread.restaurantautomationsystem.Models.Order;

import java.util.Comparator;

public class OrdersComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        return o1.getNumber() - o2.getNumber();
    }
}
