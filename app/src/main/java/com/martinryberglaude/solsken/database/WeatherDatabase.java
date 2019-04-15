package com.martinryberglaude.solsken.database;

import com.martinryberglaude.solsken.interfaces.DaoAccessWeather;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Weathers.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract DaoAccessWeather daoAccess();
}