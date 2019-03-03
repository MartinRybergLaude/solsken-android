package com.martinryberglaude.skyfall.interfaces;

import com.martinryberglaude.skyfall.database.Locations;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoAccess {

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
