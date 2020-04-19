package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Models.User;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;

public class UsersBaseAdapter extends BaseAdapter {

    private ArrayList<User> users = new ArrayList<>();
    private Context context;

    public UsersBaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Set layout of a single view of the list view.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.view_user, viewGroup, false);
        }

        // Bring XML elements from the single view to Java.
        TextView name = view.findViewById(R.id.view_user_name);
        TextView role = view.findViewById(R.id.view_user_role);

        // Set the text inside each view to the attributes of each User.
        name.setText(context.getString(R.string.format_full_name, users.get(i).getFirstName(), users.get(i).getLastName()));

        // Attach click listener to each view to start the ModifyEmployeeActivity.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Start ModifyEmployeeActivity, passing the appropriate parameters to the activity.
            }
        });
        return view;
    }
}
