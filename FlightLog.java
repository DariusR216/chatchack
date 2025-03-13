package com.flightpath.fullflights;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flight_log")
public class FlightLog {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String departure;
    private String arrival;
    private String date;
    private String notes;

    // Constructor
    public FlightLog(String departure, String arrival, String date, String notes) {
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDeparture() { return departure; }
    public String getArrival() { return arrival; }
    public String getDate() { return date; }
    public String getNotes() { return notes; }
}
