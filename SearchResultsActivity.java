package com.flightpath.fullflights;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchResultsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView searchResultsText;
    private GoogleMap mMap;
    private String searchQuery;
    private double airportLat = 0.0;
    private double airportLng = 0.0;
    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyAr_sP2gDdK0oFEKlgPdIpgBNyB2FIv2OQ";
    private static final String AIRPORT_API_URL = "https://soa.smext.faa.gov/asws/api/airport/status/";
    // Replace with a better API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        searchResultsText = findViewById(R.id.search_results_text);
        Intent intent = getIntent();
        searchQuery = intent.getStringExtra("SEARCH_QUERY");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            new FetchAirportData().execute(searchQuery);
        } else {
            searchResultsText.setText("No results found.");
        }

        // Initialize Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // Async Task to Fetch Airport Data
    private class FetchAirportData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String airportCode = params[0];

            try {
                URL url = new URL(AIRPORT_API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                parseAirportData(result);
            } else {
                searchResultsText.setText("Error retrieving data.");
            }
        }
    }

    // Parse API Response
    private void parseAirportData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray states = jsonObject.getJSONArray("states");

            for (int i = 0; i < states.length(); i++) {
                JSONArray state = states.getJSONArray(i);
                String icaoCode = state.getString(0);
                if (icaoCode.equalsIgnoreCase(searchQuery)) {
                    airportLat = state.getDouble(6); // Latitude index
                    airportLng = state.getDouble(5); // Longitude index
                    String airportInfo = "ICAO: " + icaoCode +
                            "\nCallsign: " + state.getString(1) +
                            "\nCountry: " + state.getString(2);
                    searchResultsText.setText(airportInfo);

                    if (mMap != null) {
                        LatLng airportLocation = new LatLng(airportLat, airportLng);
                        mMap.addMarker(new MarkerOptions().position(airportLocation).title("Airport: " + icaoCode));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airportLocation, 12));

                        fetchNearbyRestaurants();
                    }
                    return;
                }
            }
            searchResultsText.setText("Airport not found.");

        } catch (Exception e) {
            e.printStackTrace();
            searchResultsText.setText("Error parsing data.");
        }
    }

    // Fetch Nearby Restaurants using Google Places API
    private void fetchNearbyRestaurants() {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + airportLat + "," + airportLng +
                "&radius=5000&type=restaurant&key=" + GOOGLE_PLACES_API_KEY;

        new FetchPlacesData().execute(url);
    }

    private class FetchPlacesData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray results = jsonObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject place = results.getJSONObject(i);
                        JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        String name = place.getString("name");

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
