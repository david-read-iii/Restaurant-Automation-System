package com.read.restaurantautomationsystem.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.restaurantautomationsystem.Adapters.MenuItemsBaseAdapter;
import com.read.restaurantautomationsystem.Firebase.ValueEventListeners.MenuItemsValueEventListener;
import com.read.restaurantautomationsystem.Models.MenuItem;
import com.read.restaurantautomationsystem.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageMenuFragment extends Fragment {

    private ExpandableListView listView;
    private TextView textViewEmpty;
    private ArrayList<String> categories;
    private HashMap<String, ArrayList<MenuItem>> menuItemsByCategory;
    private MenuItemsBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private MenuItemsValueEventListener valueEventListener;

    /**
     * When this fragment is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_menu, container, false);

        // Bring XML elements to Java.
        listView = rootView.findViewById(R.id.list_view_manage_menu);
        textViewEmpty = rootView.findViewById(R.id.text_view_manage_menu_empty);

        // Initialize ArrayList, HashMap, and MenuItemsBaseAdapter.
        categories = new ArrayList<>();
        menuItemsByCategory = new HashMap<>();
        baseAdapter = new MenuItemsBaseAdapter(rootView.getContext(), categories, menuItemsByCategory);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new MenuItemsValueEventListener(categories, menuItemsByCategory, baseAdapter, textViewEmpty);

        // Set adapter of ListView.
        listView.setAdapter(baseAdapter);

        return rootView;
    }

    /**
     * When this fragment is in the foreground, start syncing the ListView with MenuItem objects
     * from the database.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Add MenuItemValueEventListener to part of database that contains MenuItem objects.
        databaseReference.child("MenuItems").addValueEventListener(valueEventListener);
    }

    /**
     * When this fragment is taken out of the foreground, stop syncing the ListView.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Detach MenuItemValueEventListener from the database.
        databaseReference.child("MenuItems").removeEventListener(valueEventListener);
    }
}