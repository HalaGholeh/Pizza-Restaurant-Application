package com.example.a1201418_1200435_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AdminMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private String loggedInEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        toolbar.setTitle("PIZZA HUB");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_Admin);
        NavigationView navigationView = findViewById(R.id.nav_view_Admin);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            loggedInEmail = getIntent().getStringExtra("email");
            Bundle bundle = new Bundle();
            bundle.putString("email", loggedInEmail);

            AdminProfileFragment profileFragment = new AdminProfileFragment();
            profileFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_profile) {
            Bundle bundle = new Bundle();
            bundle.putString("email", loggedInEmail);

            AdminProfileFragment profileFragment = new AdminProfileFragment();
            profileFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
        } else if (itemId == R.id.nav_add_admin) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddAdminFragment()).commit();
        }else if(itemId == R.id.nav_show_orders){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowOrdersFragment()).commit();
        }else if(itemId == R.id.nav_add_offers){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddSpecialOfferFragment()).commit();
        }
        else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminMenu.this, Login.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
