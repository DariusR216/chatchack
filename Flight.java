package com.flightpath.fullflights;

public class Flight {
    private String departure, arrival, date, notes;

    public Flight(String departure, String arrival, String date, String notes) {
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
        this.notes = notes;
    }

    public String getDeparture() { return departure; }
    public String getArrival() { return arrival; }
    public String getDate() { return date; }
    public String getNotes() { return notes; }
}
