package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.innovation.mobileemployer.databinding.ActivityNavigationBinding;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityNavigationBinding binding;
    private DrawerLayout drawerLayout;
    Toolbar bar;
    ActionBarDrawerToggle toggle;
    MenuItem selectedDrawerItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bar = findViewById(R.id.toolbar);
        // Set Home as the default fragment
        showDrawerLayoutFragment(new Home());
        setSupportActionBar(bar);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Use the existing ActionBarDrawerToggle if it's declared in the layout
        toggle = new ActionBarDrawerToggle(this, drawerLayout, bar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            MenuItem bottomNavigationItem = binding.bottomNavigationView.getMenu().findItem(itemID);
            highlightSelectedItem(itemID);
            if (itemID == R.id.home) {
                showDrawerLayoutFragment(new Home());
            } else if (itemID == R.id.book) {
                showDrawerLayoutFragment(new Booking());
            } else if (itemID == R.id.chats) {
                showDrawerLayoutFragment(new Chats());
            } else if (itemID == R.id.account) {
                showDrawerLayoutFragment(new Settings());
            }
            return true;
        });

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                binding.bottomNavigationView.setVisibility((int) (View.VISIBLE - slideOffset));
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                super.onDrawerClosed(drawerView);
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showDrawerLayoutFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer_fragment_container, fragment)
                .commit();
        hideBottomNavigationFragment();

        // Check if the fragment is present in the BottomNavigationView
        int itemId = getBottomNavigationItemId(fragment);
        MenuItem bottomNavigationItem = binding.bottomNavigationView.getMenu().findItem(itemId);

        // Set the visibility state for the BottomNavigationView
        if (bottomNavigationItem != null) {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationItem.setChecked(true); // Select the item in the bottom navigation
        } else {
            binding.bottomNavigationView.setVisibility(View.GONE);
        }
    }

    private void hideBottomNavigationFragment() {
        Fragment bottomNavFragment = getSupportFragmentManager().findFragmentById(R.id.bottom_nav_fragment_container);
        if (bottomNavFragment != null && bottomNavFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .remove(bottomNavFragment)
                    .commit();
        }
    }

    private int getBottomNavigationItemId(Fragment fragment) {
        if (fragment instanceof Home) {
            return R.id.home;
        } else if (fragment instanceof Booking) {
            return R.id.book;
        } else if (fragment instanceof Chats) {
            return R.id.chats;
        } else if (fragment instanceof Settings) {
            return R.id.account;
        }
        return -1;
    }

    private void highlightSelectedItem(int itemId) {
        if (selectedDrawerItem != null) {
            selectedDrawerItem.setChecked(false);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        selectedDrawerItem = navigationView.getMenu().findItem(itemId);
        if (selectedDrawerItem != null) {
            selectedDrawerItem.setChecked(true);
        }

        MenuItem bottomNavItem = binding.bottomNavigationView.getMenu().findItem(itemId);
        if (bottomNavItem != null) {
            bottomNavItem.setChecked(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        highlightSelectedItem(itemId);
        if (itemId == R.id.home1) {
            showDrawerLayoutFragment(new Home());
        } else if (itemId == R.id.prof) {
            showDrawerLayoutFragment(new ProfileFragment());
        }  else if (itemId == R.id.bookings) {
            showDrawerLayoutFragment(new Booking());
        }
        else if (itemId == R.id.about) {
            showDrawerLayoutFragment(new AboutUs());
        } else if (itemId == R.id.settings) {
            showDrawerLayoutFragment(new Settings());
        } else if (itemId == R.id.nav_logoout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
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

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottom_nav_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
