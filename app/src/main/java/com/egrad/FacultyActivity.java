package com.egrad;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class FacultyActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_back);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        final NavigationView navigationView = findViewById(R.id.navigationView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.facultyFrameLayout, new FacultyHomeFragment()).commit();
        } else {
            backStackChangedListener.onBackStackChanged();
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch(item.getItemId()) {
                // Cases for different menu items
            }

            drawerLayout.closeDrawer(GravityCompat.START, true);
            return true;
        });
        getSupportFragmentManager().addOnBackStackChangedListener(backStackChangedListener);

/*
        String fullName = CurrentUserData.getFirstName() + " " + CurrentUserData.getLastName();
        TextView userFullName = navigationView.getHeaderView(0).findViewById(R.id.fullName);
        userFullName.setText(fullName);

        String pName = CurrentUserData.getFirstName().substring(0, 1) + CurrentUserData.getLastName().charAt(0);
        TextView profileName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        profileName.setText(pName);

        navigationView.getHeaderView(0).setOnClickListener(view ->
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homeFrameLayout, new MyProfileFragment())
                        .addToBackStack(null).commit());
*/

    }


    final FragmentManager.OnBackStackChangedListener backStackChangedListener = () -> {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            drawerToggle.setDrawerIndicatorEnabled(false);
            drawerToggle.setToolbarNavigationClickListener(view -> onBackPressed());
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerToggle.setDrawerIndicatorEnabled(true);
        }
    };


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START, true);
        } else {
            super.onBackPressed();
        }
    }

}