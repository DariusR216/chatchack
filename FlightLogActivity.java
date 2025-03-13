package com.flightpath.fullflights;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;

public class FlightLogActivity extends AppCompatActivity {
    private FlightLogDatabase db;
    private FlightLogAdapter adapter;
    private EditText departureInput, arrivalInput, dateInput, notesInput;
    private RecyclerView recyclerViewFlights;
    private List<FlightLog> flightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_log);

        // Initialize database
        db = FlightLogDatabase.getInstance(this);

        // Setup RecyclerView
        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));

        // Initialize flight list & adapter
        flightList = new ArrayList<>();
        adapter = new FlightLogAdapter(flightList , db);
        recyclerViewFlights.setAdapter(adapter);

        // Fetch flights from database
        loadFlightsFromDatabase();

        // Initialize UI components
        departureInput = findViewById(R.id.input_departure);
        arrivalInput = findViewById(R.id.input_arrival);
        dateInput = findViewById(R.id.input_date);
        notesInput = findViewById(R.id.input_notes);
        Button addFlightButton = findViewById(R.id.btn_add_flight);

        // Set button click listener with confirmation dialog
        addFlightButton.setOnClickListener(v -> confirmAddFlight());

        // Enable swipe-to-delete
        setupSwipeToDelete();
    }

    private void loadFlightsFromDatabase() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<FlightLog> flights = db.flightLogDao().getAllFlights();

            // Sort flights by date (assuming consistent date format)
            Collections.sort(flights, Comparator.comparing(FlightLog::getDate).reversed());

            runOnUiThread(() -> {
                flightList.clear();
                flightList.addAll(flights);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private void confirmAddFlight() {
        new AlertDialog.Builder(this)
                .setTitle("Add Flight")
                .setMessage("Are you sure you want to add this flight?")
                .setPositiveButton("Yes", (dialog, which) -> addFlight())
                .setNegativeButton("No", null)
                .show();
    }

    private void addFlight() {
        String departure = departureInput.getText().toString().trim();
        String arrival = arrivalInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String notes = notesInput.getText().toString().trim();

        if (departure.isEmpty() || arrival.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        FlightLog newFlight = new FlightLog(departure, arrival, date, notes);

        Executors.newSingleThreadExecutor().execute(() -> {
            db.flightLogDao().insert(newFlight);
            runOnUiThread(() -> {
                flightList.add(0, newFlight);  // Add new flight at the top
                adapter.notifyItemInserted(0);
                recyclerViewFlights.smoothScrollToPosition(0);
                Toast.makeText(this, "Flight added!", Toast.LENGTH_SHORT).show();
                clearInputFields();
            });
        });
    }

    private void clearInputFields() {
        departureInput.setText("");
        arrivalInput.setText("");
        dateInput.setText("");
        notesInput.setText("");
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FlightLog flightToDelete = flightList.get(position);

                Executors.newSingleThreadExecutor().execute(() -> {
                    db.flightLogDao().delete(flightToDelete);
                    runOnUiThread(() -> {
                        flightList.remove(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(FlightLogActivity.this, "Flight deleted!", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewFlights);
    }
}
