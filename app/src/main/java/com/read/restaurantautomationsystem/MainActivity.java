package com.read.restaurantautomationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

/* TODO: Restrict certain NavigationItems from showing in the NavigationDrawer given the role of the
    logged in user. */

/* TODO: Toolbar actions and their click listeners for each fragment will need to be defined in this
    activity. In addition, they will also need to be swapped out when a new fragment is started.
    These will likely be defined in the startFragment() method or a helper method to it. */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    /**
     * Setup an activity with a custom Toolbar and NavigationDrawer when this activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use custom Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup the NavigationDrawer.
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        /* Receive the full name and role of the logged in user from the LoginActivity and show them
         * in the header of the NavigationDrawer. */
        TextView textViewFullName = navigationView.getHeaderView(0).findViewById(R.id.textview_drawer_name);
        TextView textViewRole = navigationView.getHeaderView(0).findViewById(R.id.textview_drawer_role);

        Intent intent = getIntent();
        textViewFullName.setText(getString(R.string.format_full_name, intent.getStringExtra("firstName"), intent.getStringExtra("lastName")));
        textViewRole.setText(intent.getStringExtra("role"));

        // Start a Fragment given the role of the logged in user.
        if(intent.getStringExtra("role").equals("Manager")) {
            startFragment(new ViewReportsFragment(), getString(R.string.fragment_view_reports_name));
        } else if(intent.getStringExtra("role").equals("Cook")) {
            startFragment(new OrderQueueFragment(), getString(R.string.fragment_order_queue_name));
        } else {
            startFragment(new TableListFragment(), getString(R.string.fragment_table_list_name));
        }
    }

    /**
     * Define what to do in response to NavigationItem clicks in the NavigationDrawer.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        closeDrawer();
        if (menuItem.getItemId() == R.id.drawer_menu_table_list) {
            startFragment(new TableListFragment(), getString(R.string.fragment_table_list_name));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_order_queue) {
            startFragment(new OrderQueueFragment(), getString(R.string.fragment_order_queue_name));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_employees) {
            startFragment(new ManageEmployeesFragment(), getString(R.string.fragment_manage_employees_name));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_inventory) {
            startFragment(new ManageInventoryFragment(), getString(R.string.fragment_manage_inventory_name));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_view_reports) {
            startFragment(new ViewReportsFragment(), getString(R.string.fragment_view_reports_name));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_tables) {
            startFragment(new ManageTablesFragment(), getString(R.string.fragment_manage_tables_name));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_menu) {
            startFragment(new ManageMenuFragment(), getString(R.string.fragment_manage_menu_name));
        }
        return true;
    }

    /**
     * Starts a given Fragment in the fragment container.
     * @param fragment Fragment to be started in the fragment container.
     * @param title The title to be set in the toolbar once the fragment is started.
     */
    private void startFragment(Fragment fragment, String title) {
        getSupportActionBar().setTitle(title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Closes the NavigationDrawer with an animation
     */
    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}