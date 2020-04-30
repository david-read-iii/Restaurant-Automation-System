package com.read.restaurantautomationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.read.restaurantautomationsystem.Comparators.MenuItemsComparator;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class ManageMenuBaseAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItem>> menuItemsByCategory;

    /**
     * Defines how MenuItem objects in the HashMap and categories in the ArrayList should be adapted to
     * be displayed in an ExpandableListView.
     */
    public ManageMenuBaseAdapter(Context context, ArrayList<String> categories, HashMap<String, ArrayList<MenuItem>> menuItemsByCategory) {
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
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
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

        // Retrieve the category of the ith category in the ArrayList.
        String selected = (String) getGroup(i);

        // Set the as the category of the ith category.
        textViewCategory.setText(selected);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        // Set the layout of a single child view of the ListView.
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_menu_item, viewGroup, false);
        }

        // Bring XML elements from the single child view to Java.
        TextView textViewName = view.findViewById(R.id.text_view_menu_item_name);
        TextView textViewPrice = view.findViewById(R.id.text_view_menu_item_price);

        // Retrieve attributes of the (ith, i1th) MenuItem object from the HashMap.
        MenuItem selected = (MenuItem) getChild(i, i1);

        // Create NumberFormat object to format currency attributes.
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // Set text as attributes of the (ith, i1th) MenuItem object.
        textViewName.setText(selected.getName());
        textViewPrice.setText(currencyFormat.format(selected.getPrice()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * Before adapting the data, sort the categories in the ArrayList alphabetically and sort the
     * MenuItem objects in each ArrayList of the HashMap alphabetically by name.
     */
    @Override
    public void notifyDataSetChanged() {

        // Sort categories in ArrayList alphabetically.
        Collections.sort(categories, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        // Sort MenuItem objects in each ArrayList in the HashMap alphabetically by name.
        for (HashMap.Entry<String, ArrayList<MenuItem>> menuItems : menuItemsByCategory.entrySet()) {
            Collections.sort(menuItems.getValue(), new MenuItemsComparator());
        }

        super.notifyDataSetChanged();
    }
}