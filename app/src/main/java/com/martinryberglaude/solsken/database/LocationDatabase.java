package com.martinryberglaude.solsken.database;

import com.martinryberglaude.solsken.interfaces.DaoAccessLocation;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Locations.class}, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract DaoAccessLocation daoAccess();
}
