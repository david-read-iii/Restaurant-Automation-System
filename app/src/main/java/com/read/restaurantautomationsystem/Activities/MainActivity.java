package com.read.restaurantautomationsystem.Activities;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.read.restaurantautomationsystem.Fragments.ManageEmployeesFragment;
import com.read.restaurantautomationsystem.Fragments.ManageInventoryFragment;
import com.read.restaurantautomationsystem.Fragments.ManageMenuFragment;
import com.read.restaurantautomationsystem.Fragments.ManageTablesFragment;
import com.read.restaurantautomationsystem.Fragments.OrderQueueFragment;
import com.read.restaurantautomationsystem.Fragments.ViewEmployeeLogFragment;
import com.read.restaurantautomationsystem.Fragments.ViewOrderHistoryFragment;
import com.read.restaurantautomationsystem.Models.Employee;
import com.read.restaurantautomationsystem.R;
import com.read.restaurantautomationsystem.Fragments.ServeTablesFragment;
import com.read.restaurantautomationsystem.Services.LoggedInService;

/* TODO: Restrict certain NavigationItems from showing in the NavigationDrawer given the role of the
    logged in user. */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Employee loggedInEmployee;
    private DrawerLayout drawerLayout;
    private Menu menu;

    /**
     * When this activity is created, inflate a layout with a custom Toolbar and a NavigationDrawer.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get the attributes of the logged in employee and store them in an Employee object. If the
         * user is navigating from the LoginActivity, we will get these from intent extras. If the
         * user is navigating from a child activity, we will get them from a saved instance bundle. */
        Intent intent = getIntent();
        Bundle loginActivityExtras = intent.getExtras();

        if (loginActivityExtras != null) {
            loggedInEmployee = new Employee(
                    intent.getStringExtra("key"),
                    intent.getStringExtra("firstName"),
                    intent.getStringExtra("lastName"),
                    intent.getStringExtra("username"),
                    intent.getStringExtra("password"),
                    intent.getStringExtra("role"));
        } else if (savedInstanceState != null) {
            loggedInEmployee = new Employee(
                    savedInstanceState.getString("key"),
                    savedInstanceState.getString("firstName"),
                    savedInstanceState.getString("lastName"),
                    savedInstanceState.getString("username"),
                    savedInstanceState.getString("password"),
                    savedInstanceState.getString("role"));
        }

        // Use custom Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Initialize NavigationDrawer objects.
        drawerLayout = findViewById(R.id.drawer_layout_main);
        NavigationView navigationView = findViewById(R.id.navigation_view_main);

        // Define what NavigationItems to put in the NavigationDrawer, depending on the role of the logged in Employee.
        if (loggedInEmployee.getRole().equals("Manager")) {
            navigationView.inflateMenu(R.menu.drawer_menu);
        } else if (loggedInEmployee.getRole().equals("Cook")) {
            navigationView.inflateMenu(R.menu.drawer_menu_restricted_order_queue_only);
        } else if (loggedInEmployee.getRole().equals("Waiter")) {
            navigationView.inflateMenu(R.menu.drawer_menu_restricted_employee_functions_only);
        } else {
            navigationView.inflateMenu(R.menu.drawer_menu_restricted_serve_tables_only);
        }

        // Handle NavigationItem clicks in onNavigationItemSelected().
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize and add an ActionBarToggle to the NavigationDrawer, so it can be opened by a toggle in the Toolbar.
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // Set the drawer header text as the name and role of the logged in employee.
        TextView textViewFullName = navigationView.getHeaderView(0).findViewById(R.id.text_view_drawer_header_main_name);
        TextView textViewRole = navigationView.getHeaderView(0).findViewById(R.id.text_view_drawer_header_main_role);
        textViewFullName.setText(getString(R.string.format_full_name, loggedInEmployee.getFirstName(), loggedInEmployee.getLastName()));
        textViewRole.setText(loggedInEmployee.getRole());

        // Start a Fragment given the role of the logged in employee.
        if (loggedInEmployee.getRole().equals("Manager")) {
            startFragment(new ViewEmployeeLogFragment(), getString(R.string.name_fragment_view_employee_log));
        } else if (loggedInEmployee.getRole().equals("Cook")) {
            // Pass first and last name attributes of the logged in Employee into the fragment.
            Fragment fragment = new OrderQueueFragment();
            Bundle bundle = new Bundle();
            bundle.putString("firstName", loggedInEmployee.getFirstName());
            bundle.putString("lastName", loggedInEmployee.getLastName());
            fragment.setArguments(bundle);
            startFragment(fragment, getString(R.string.name_fragment_order_queue));
        } else {
            // Pass first and last name attributes of the logged in Employee into the fragment.
            Fragment fragment = new ServeTablesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("firstName", loggedInEmployee.getFirstName());
            bundle.putString("lastName", loggedInEmployee.getLastName());
            fragment.setArguments(bundle);
            startFragment(fragment, getString(R.string.name_fragment_serve_tables));
        }
    }

    /**
     * When prompted to save the activity instance state, save the attributes of the logged in
     * Employee to the instance state.
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("key", loggedInEmployee.getKey());
        savedInstanceState.putString("firstName", loggedInEmployee.getFirstName());
        savedInstanceState.putString("lastName", loggedInEmployee.getLastName());
        savedInstanceState.putString("username", loggedInEmployee.getUsername());
        savedInstanceState.putString("password", loggedInEmployee.getPassword());
        savedInstanceState.putString("role", loggedInEmployee.getRole());
    }

    /**
     * Define what to do in response to NavigationItem clicks in the NavigationDrawer.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Close the navigation drawer.
        closeDrawer();

        // Start the appropriate Fragment.
        if (menuItem.getItemId() == R.id.drawer_menu_serve_tables) {
            // Pass first and last name attributes of the logged in Employee into the fragment.
            Fragment fragment = new ServeTablesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("firstName", loggedInEmployee.getFirstName());
            bundle.putString("lastName", loggedInEmployee.getLastName());
            fragment.setArguments(bundle);
            startFragment(fragment, getString(R.string.name_fragment_serve_tables));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_order_queue) {
            // Pass first and last name attributes of the logged in Employee into the fragment.
            Fragment fragment = new OrderQueueFragment();
            Bundle bundle = new Bundle();
            bundle.putString("firstName", loggedInEmployee.getFirstName());
            bundle.putString("lastName", loggedInEmployee.getLastName());
            fragment.setArguments(bundle);
            startFragment(fragment, getString(R.string.name_fragment_order_queue));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_employees) {
            startFragment(new ManageEmployeesFragment(), getString(R.string.name_fragment_manage_employees), R.menu.manage_employees_menu);
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_inventory) {
            startFragment(new ManageInventoryFragment(), getString(R.string.name_fragment_manage_inventory), R.menu.manage_inventory_menu);
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_menu) {
            startFragment(new ManageMenuFragment(), getString(R.string.name_fragment_manage_menu), R.menu.manage_menu_menu);
        }
        if (menuItem.getItemId() == R.id.drawer_menu_manage_tables) {
            startFragment(new ManageTablesFragment(), getString(R.string.name_fragment_manage_tables), R.menu.manage_tables_menu);
        }
        if (menuItem.getItemId() == R.id.drawer_menu_view_employee_log) {
            startFragment(new ViewEmployeeLogFragment(), getString(R.string.name_fragment_view_employee_log));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_view_order_history) {
            startFragment(new ViewOrderHistoryFragment(), getString(R.string.name_fragment_view_order_history));
        }
        if (menuItem.getItemId() == R.id.drawer_menu_log_out) {
            // Stop LoggedInService.
            Intent intent = new Intent(MainActivity.this, LoggedInService.class);
            stopService(intent);

            // Go back to LoginActivity.
            finish();
        }
        return true;
    }

    /**
     * Instead of specifying Toolbar actions here, make the Menu object for this activity a global
     * variable. This way, the Toolbar actions can be specified each time a Fragment is started.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    /**
     * Define what to do in response to clicks in the Toolbar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_employee) {
            Intent intent = new Intent(this, AddEmployeeActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_add_inventory_item) {
            Intent intent = new Intent(this, AddInventoryItemActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_add_menu_item) {
            Intent intent = new Intent(this, AddMenuItemActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_add_table) {
            Intent intent = new Intent(this, AddTableActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Restrict user from going back to LoginActivity using the back button.
     */
    @Override
    public void onBackPressed() {
    }

    /**
     * Starts a given Fragment in the fragment container.
     *
     * @param fragment Fragment to be started in the fragment container.
     * @param title    Text to display in Toolbar.
     */
    private void startFragment(Fragment fragment, String title) {
        // Set Toolbar text.
        getSupportActionBar().setTitle(title);

        // Clear actions from Toolbar.
        if (menu != null) {
            menu.clear();
        }

        // Start Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_main, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Starts a given Fragment in the fragment container with the specified actions in the Toolbar.
     *
     * @param fragment Fragment to be started in the fragment container.
     * @param title    Text to display in Toolbar.
     * @param menuRes  Menu resource file specifying the actions to display in the Toolbar.
     */
    private void startFragment(Fragment fragment, String title, int menuRes) {
        // Set Toolbar text.
        getSupportActionBar().setTitle(title);

        // Clear actions from Toolbar.
        if (menu != null) {
            menu.clear();
        }

        // Add specified actions to Toolbar.
        getMenuInflater().inflate(menuRes, menu);

        // Start Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_main, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Closes the NavigationDrawer with an animation.
     */
    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}