package com.example.myapplication.Screen;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragment.CartFragment;
import com.example.myapplication.Fragment.FavoriteFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Fragment.ProfileFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNav extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public static String tagName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
//        defaultFragment = new HomeFragment();
        bottomNavigationView = findViewById(R.id.navigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_body, new HomeFragment(), "Home").commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment defaultFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        defaultFragment = new HomeFragment();
                        tagName = "Home";
                        break;
                    case R.id.nav_cart:
                        defaultFragment = new CartFragment();
                        tagName = "Cart";
                        break;
                    case R.id.nav_favorite:
                        defaultFragment = new FavoriteFragment();
                        tagName = "Favorite";
                        break;
                    case R.id.nav_profile:
                        defaultFragment = new ProfileFragment();
                        tagName = "Profile";
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.navigation_body, defaultFragment, tagName).commit();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tagName.equals("Profile")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.navigation_body, new ProfileFragment(), "Profile").commit();
        }
        if (tagName.equals("Cart")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.navigation_body, new CartFragment(), "Cart").commit();
        }
        if (tagName.equals("Favorite")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.navigation_body, new FavoriteFragment(), "Favorite").commit();
        }

    }
}