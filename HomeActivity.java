package com.flightpath.fullflights;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private EditText searchBar;
    private Button btnFindRestaurants, btnFlightLog, btnReviews;
    private ImageView profileIcon;
    private Switch darkModeToggle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI Components
        searchBar = findViewById(R.id.search_bar);
        mapView = findViewById(R.id.mapView);
        btnFindRestaurants = findViewById(R.id.btn_nearby_restaurants);
        btnFlightLog = findViewById(R.id.btn_flight_log);
        btnReviews = findViewById(R.id.btn_reviews);
        profileIcon = findViewById(R.id.profile_icon);
        darkModeToggle = findViewById(R.id.dark_mode_toggle);

        // Initialize SharedPreferences for dark mode
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);
        darkModeToggle.setChecked(isDarkMode);
        //applyDarkMode(isDarkMode);

        // Initialize Google Maps
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Button Click Listeners
        btnFindRestaurants.setOnClickListener(v -> searchNearbyRestaurants());
        btnFlightLog.setOnClickListener(v -> openFlightLog());
       btnReviews.setOnClickListener(v -> openReviews());
       profileIcon.setOnClickListener(v -> openProfile());

       //  Dark Mode Toggle
        darkModeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("DarkMode", isChecked).apply();
            applyDarkMode(isChecked);
        });
    }

    /** Initialize Google Maps */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Customize map settings here
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    /** Function to search for nearby restaurants */
    private void searchNearbyRestaurants() {
        String query = searchBar.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Enter an airport code or location!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Searching near: " + query, Toast.LENGTH_SHORT).show();
            // Add Google Places API call here to fetch restaurants
        }
    }

    /** Open Flight Log Activity */
    private void openFlightLog() {
        Intent intent = new Intent(this, FlightLogActivity.class);
        startActivity(intent);
    }

    /** Open Reviews Activity */
    private void openReviews() {
        Intent intent = new Intent(this, ReviewsActivity.class);
        startActivity(intent);
    }

    /** Open Profile Activity */
    private void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /** Apply Dark Mode  */
    private void applyDarkMode(boolean isDarkMode) {
   //     if (isDarkMode) {
     //       setTheme(R.style.Theme_AppCompat_DayNight);
     //   } else {
      //      setTheme(R.style.Theme_AppCompat_Light);
      //  }
      //  recreate(); // Refresh the activity
    }

    /** Handle MapView Lifecycle */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}