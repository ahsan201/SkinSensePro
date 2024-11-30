package com.example.skinsensepro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.skinsensepro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up Bottom Navigation
        setupBottomNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_filled);
        }

        // Set a click listener for the Floating Action Button
        binding.fab.setOnClickListener(view -> {
            // Open the BarcodeScannerActivity when the FAB is clicked
            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Reset all icons to default (holo)
            resetMenuIcons();

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                item.setIcon(R.drawable.home_filled);
            } else if (item.getItemId() == R.id.nav_scanned) {
                selectedFragment = new ScannedItemsFragment();
                item.setIcon(R.drawable.scanned_filled);
            } else if (item.getItemId() == R.id.nav_liked) {
                selectedFragment = new LikedFragment();
                item.setIcon(R.drawable.liked_filled);
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                item.setIcon(R.drawable.profile_filled);
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void resetMenuIcons() {
        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_holo);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_scanned).setIcon(R.drawable.scanned_holo);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_liked).setIcon(R.drawable.liked_holo);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_profile).setIcon(R.drawable.profile_holo);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.fragmentContainer.getId(), fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int selectedItemId = binding.bottomNavigation.getSelectedItemId();
        if (selectedItemId != R.id.nav_home) {
            // Set HomeFragment as the selected fragment
            binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        } else {
            super.onBackPressed(); // Exit the app
        }
    }
}
