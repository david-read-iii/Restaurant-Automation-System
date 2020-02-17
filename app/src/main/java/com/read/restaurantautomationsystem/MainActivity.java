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

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    //----------------------------------------------------------------------------------------------
    // Inflates the activity_main.xml layout file. Sets up a custom toolbar and navigation drawer.
    // Launches a TableListFragment initially.
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bring XML toolbar element to Java and set it up.
        toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // Bring XML navigation drawer elements to Java and set them up.
        navigationView = findViewById(R.id.activity_main_navigation_view);
        drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.activity_main_drawer_open, R.string.activity_main_drawer_closed);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // Launch a TableListFragment.
        getSupportActionBar().setTitle(R.string.fragment_table_list_name);
        loadFragment(new TableListFragment());
    }

    //----------------------------------------------------------------------------------------------
    // Responds to navigation drawer clicks.
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        closeDrawer();
        if (menuItem.getItemId() == R.id.activity_main_drawer_menu_table_list) {
            getSupportActionBar().setTitle(R.string.fragment_table_list_name);
            loadFragment(new TableListFragment());
        }
        if (menuItem.getItemId() == R.id.activity_main_drawer_menu_order_queue) {
            getSupportActionBar().setTitle(R.string.fragment_order_queue_name);
            loadFragment(new OrderQueueFragment());
        }
        if (menuItem.getItemId() == R.id.activity_main_drawer_menu_manage_employees) {
            Toast.makeText(this, "Manage employees clicked", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.activity_main_drawer_menu_manage_inventory) {
            Toast.makeText(this, "Manage inventory clicked", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.activity_main_drawer_menu_view_reports) {
            Toast.makeText(this, "View reports clicked", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.activity_main_drawer_menu_manage_tables) {
            Toast.makeText(this, "Manage tables clicked", Toast.LENGTH_SHORT).show();
        }
        if (menuItem.getItemId() == R.id.activity_main_drawer_menu_manage_menu) {
            Toast.makeText(this, "Manage menu clicked", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //----------------------------------------------------------------------------------------------
    // Populates the fragment container with a given fragment.
    //----------------------------------------------------------------------------------------------
    private void loadFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    //----------------------------------------------------------------------------------------------
    // Closes the navigation drawer with an animation.
    //----------------------------------------------------------------------------------------------
    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
