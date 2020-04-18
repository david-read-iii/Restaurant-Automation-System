package com.read.restaurantautomationsystem.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.read.restaurantautomationsystem.R;

public class ViewReportsFragment extends Fragment {

    /**
     * Setup a fragment with an undefined layout when this fragment is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_reports, container, false);
    }
}