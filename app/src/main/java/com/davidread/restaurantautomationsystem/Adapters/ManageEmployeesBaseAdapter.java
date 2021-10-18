package com.davidread.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.davidread.restaurantautomationsystem.Comparators.EmployeesComparator;
import com.davidread.restaurantautomationsystem.Models.Employee;
import com.davidread.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.Collections;

public class ManageEmployeesBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Employee> employees;

    /**
     * Defines how Employee objects in the ArrayList employees should be adapted to be displayed
     * in a ListView.
     */
    public ManageEmployeesBaseAdapter(Context context, ArrayList<Employee> employees) {
        this.context = context;
        this.employees = employees;
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int i) {
        return employees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_employee, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView name = view.findViewById(R.id.text_view_employee_name);
        TextView role = view.findViewById(R.id.text_view_employee_role);

        // Retrieve attributes of the ith Employee object in the ArrayList.
        Employee selected = (Employee) getItem(i);

        // Set text as the attributes of the ith Employee object.
        name.setText(context.getString(R.string.format_full_name, selected.getFirstName(), selected.getLastName()));
        role.setText(selected.getRole());

        return view;
    }

    /**
     * Before adapting the data, sort the Employee objects in the ArrayList alphabetically by last
     * name, then by first name.
     */
    @Override
    public void notifyDataSetChanged() {
        Collections.sort(employees, new EmployeesComparator());
        super.notifyDataSetChanged();
    }
}
