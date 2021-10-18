package com.davidread.restaurantautomationsystem.Comparators;

import com.davidread.restaurantautomationsystem.Models.Employee;

import java.util.Comparator;

public class EmployeesComparator implements Comparator<Employee> {

    /**
     * Defines a sorting scheme, in which Employee objects are sorted alphabetically by their last
     * name, then alphabetically by their last name.
     */
    @Override
    public int compare(Employee e1, Employee e2) {

        int lastNameCompareValue = e1.getLastName().compareToIgnoreCase(e2.getLastName());

        if (lastNameCompareValue != 0) {
            return lastNameCompareValue;
        } else {
            return e1.getFirstName().compareToIgnoreCase(e2.getLastName());
        }
    }
}
