package com.read.restaurantautomationsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class TableListFragment extends Fragment {

    //----------------------------------------------------------------------------------------------
    // Inflates the fragment_table_list.xml layout file.
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table_list, container, false);
    }
}
