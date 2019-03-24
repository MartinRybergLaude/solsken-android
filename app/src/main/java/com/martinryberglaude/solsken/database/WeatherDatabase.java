package com.martinryberglaude.solsken.database;

import com.martinryberglaude.solsken.interfaces.DaoAccessWeather;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Locations.class}, version = 1, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract DaoAccessWeather daoAccess();
}