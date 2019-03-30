package com.martinryberglaude.solsken.interfaces;

import com.martinryberglaude.solsken.database.Weathers;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoAccessWeather {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleWeather (Weathers weathers);
    @Query("SELECT * FROM Weathers WHERE weatherId = :weatherId")
    Weathers fetchWeathersById (String weatherId);

    @Query("SELECT * FROM Weathers")
    List<Weathers> fetchAllWeathers();

    @Update
    void updateWeather (Weathers weathers);
    @Delete
    void deleteWeather (Weathers weathers);

    @Query("DELETE FROM Weathers")
    void deleteAllWeathers();
}
