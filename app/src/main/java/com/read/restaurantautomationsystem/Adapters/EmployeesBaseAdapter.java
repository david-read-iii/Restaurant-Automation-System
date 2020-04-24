package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Activities.ModifyEmployeeActivity;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class EmployeesBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Employee> employees = new ArrayList<>();

    /**
     * Defines how Employee objects in the ArrayList employees should be adapted to be displayed
     * in a ListView.
     */
    public EmployeesBaseAdapter(Context context, ArrayList<Employee> employees) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the list view.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.view_employee, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView name = view.findViewById(R.id.view_employee_name);
        TextView role = view.findViewById(R.id.view_employee_role);

        // Set the text inside each view to the attributes of each Employee.
        name.setText(context.getString(R.string.format_full_name, employees.get(i).getFirstName(), employees.get(i).getLastName()));
        role.setText(employees.get(i).getRole());

        // Attach a click listener to each view to start the ModifyEmployeeActivity.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ModifyEmployeeActivity.class);

                // Pass the attributes of the selected Employee to the activity.
                intent.putExtra("key", employees.get(i).getKey());
                intent.putExtra("firstName", employees.get(i).getFirstName());
                intent.putExtra("lastName", employees.get(i).getLastName());
                intent.putExtra("username", employees.get(i).getUsername());
                intent.putExtra("password", employees.get(i).getPassword());
                intent.putExtra("role", employees.get(i).getRole());

                context.startActivity(intent);
            }
        });
        return view;
    }
}
