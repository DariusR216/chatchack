package com.flightpath.fullflights;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"; // Used for saving state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Set layout file

        // Initialize MapView
        mapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = savedInstanceState != null ? savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY) : null;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        // **PROFILE ICON CLICK TO OPEN PROFILE**
        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // **Button to access flight log records**
        Button btnFlightLog = findViewById(R.id.btn_flight_log);
        btnFlightLog.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FlightLogActivity.class);
            startActivity(intent);
        });

        // **Button to access restaurant reviews**
        Button btnReviews = findViewById(R.id.btn_reviews);
        btnReviews.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReviewsActivity.class);
            startActivity(intent);
        });

        // **Search Bar Functionality**
        EditText searchBar = findViewById(R.id.search_bar);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            } else {
                Toast.makeText(MainActivity.this, "Enter an airport code", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    // Function to handle the search query
    private void performSearch(String query) {
        // Pass the search query to another activity or process the search result
        Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }

    // Handle lifecycle events for MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}
