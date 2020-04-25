package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Activities.ModifyMenuItemActivity;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines how MenuItem objects in the ArrayList menuItems should be adapted to be displayed
 * in an ExpandableListView.
 */
public class MenuItemsBaseAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItem>> menuItemsByCategory;

    public MenuItemsBaseAdapter(Context context, ArrayList<String> categories, HashMap<String, ArrayList<MenuItem>> menuItemsByCategory) {
        this.context = context;
        this.categories = categories;
        this.menuItemsByCategory = menuItemsByCategory;
    }

    @Override
    public int getGroupCount() {
        return this.categories.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.menuItemsByCategory.get(this.categories.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.categories.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.menuItemsByCategory.get(this.categories.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        // Set the layout of a single group view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.group_menu_item, viewGroup, false);
        }

        // Bring XML elements from the single group view to Java.
        TextView textViewCategory = view.findViewById(R.id.text_view_menu_item_category);

        // Set the text inside each view to the category of the MenuItems to appear underneath this group view.
        textViewCategory.setText((String) getGroup(i));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        // Set the layout of a single child view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_menu_item, viewGroup, false);
        }

        // Bring XML elements from the single child view to Java.
        TextView textViewName = (TextView) view.findViewById(R.id.text_view_menu_item_name);
        TextView textViewPrice = (TextView) view.findViewById(R.id.text_view_menu_item_price);

        // Get the MenuItem object corresponding to this view.
        final MenuItem menuItem = (MenuItem) getChild(i, i1);

        // Set the text inside each view to the attributes of each MenuItem.
        textViewName.setText(menuItem.getName());
        textViewPrice.setText(menuItem.getPrice());

        // Attach a click listener to each view to start the ModifyMenuItemActivity.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ModifyMenuItemActivity.class);

                // Pass the attributes of the selected MenuItem to the activity.
                intent.putExtra("key", menuItem.getKey());
                intent.putExtra("name", menuItem.getName());
                intent.putExtra("price", menuItem.getPrice());
                intent.putExtra("category", menuItem.getCategory());

                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}