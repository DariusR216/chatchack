package com.flightpath.fullflights;

import android.content.Context;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.util.*;

@Database(entities = {FlightLog.class}, version = 1)
public abstract class FlightLogDatabase extends RoomDatabase {
    public abstract FlightLogDao flightLogDao();

    private static volatile FlightLogDatabase INSTANCE;

    public static FlightLogDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FlightLogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FlightLogDatabase.class, "flight_log_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
