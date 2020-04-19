package com.read.restaurantautomationsystem.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.read.restaurantautomationsystem.Adapters.UsersBaseAdapter;
import com.read.restaurantautomationsystem.R;

public class ManageEmployeesFragment extends Fragment {

    private ListView listView;

    /**
     * Setup a fragment with an undefined layout when this fragment is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_employees, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Bring XML elements to Java.
        // listView = findViewById(R.id.listview_users);

        // Attach custom base adapter to the list view.
        // listView.setAdapter(new UsersBaseAdapter(this.getContext()));

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}