package com.martinryberglaude.solsken.interfaces;

import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.database.Weathers;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoAccessWeather {
    @Insert
    void insertSingleWeather (Weathers weathers);
    @Query("SELECT * FROM Weathers WHERE weatherId = :weatherId")
    Locations fetchWeathersById (int weatherId);

    @Query("SELECT * FROM Weathers")
    List<Weathers> fetchAllWeathers();

    @Update
    void updateWeather (Weathers weathers);
    @Delete
    void deleteWeather (Weathers weathers);
}
