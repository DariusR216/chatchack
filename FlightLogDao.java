package com.flightpath.fullflights;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface FlightLogDao {
    @Insert
    void insert(FlightLog flightLog);

    @Query("SELECT * FROM flight_log ORDER BY date DESC")
    List<FlightLog> getAllFlights();

    @Delete
    void delete(FlightLog flightLog);
}
