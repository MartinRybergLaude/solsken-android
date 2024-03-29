package com.martinryberglaude.solsken.interfaces;

import com.martinryberglaude.solsken.database.Locations;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoAccessLocation {

    @Insert
    void insertSingleLocation (Locations locations);
    @Query("SELECT * FROM Locations WHERE locationId = :locationId")
    Locations fetchLocationById (int locationId);

    @Query("SELECT * FROM Locations")
    List<Locations> fetchAllLocations();

    @Update
    void updateLocation (Locations locations);
    @Delete
    void deleteLocation (Locations locations);
}
