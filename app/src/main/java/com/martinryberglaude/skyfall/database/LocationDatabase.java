package com.martinryberglaude.skyfall.database;

import com.martinryberglaude.skyfall.interfaces.DaoAccess;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Locations.class}, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
