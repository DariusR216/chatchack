package com.flightpath.fullflights;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executors;

public class FlightLogAdapter extends RecyclerView.Adapter<FlightLogAdapter.ViewHolder> {
    private List<FlightLog> flightLogs;
    private FlightLogDatabase db;

    // Constructor
    public FlightLogAdapter(List<FlightLog> flightLogs, FlightLogDatabase db) {
        this.flightLogs = flightLogs;
        this.db = db;
    }

    // Create ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flight_log, parent, false);
        return new ViewHolder(view);
    }

    // Bind Data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlightLog flight = flightLogs.get(position);
        holder.departure.setText("Departure: " + flight.getDeparture());
        holder.arrival.setText("Arrival: " + flight.getArrival());
        holder.date.setText("Date: " + flight.getDate());
        holder.notes.setText("Notes: " + flight.getNotes());

        // Delete flight log on long press
        holder.itemView.setOnLongClickListener(v -> {
            removeFlight(position, flight);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return flightLogs.size();
    }

    // 🛠 Efficiently remove a flight
    private void removeFlight(int position, FlightLog flight) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.flightLogDao().delete(flight);
            flightLogs.remove(position);
            notifyItemRemoved(position); // 🚀 Smooth UI update
        });
    }

    // ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView departure, arrival, date, notes;

        ViewHolder(View itemView) {
            super(itemView);
            departure = itemView.findViewById(R.id.text_departure);
            arrival = itemView.findViewById(R.id.text_arrival);
            date = itemView.findViewById(R.id.text_date);
            notes = itemView.findViewById(R.id.text_notes);
        }
    }
}
