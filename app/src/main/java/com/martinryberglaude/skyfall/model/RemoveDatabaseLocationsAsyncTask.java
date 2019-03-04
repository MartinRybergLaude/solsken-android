package com.martinryberglaude.skyfall.model;

import android.content.Context;
import android.os.AsyncTask;

import com.martinryberglaude.skyfall.database.LocationDatabase;
import com.martinryberglaude.skyfall.database.Locations;
import com.martinryberglaude.skyfall.interfaces.MainContract;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.room.Room;

public class RemoveDatabaseLocationsAsyncTask extends AsyncTask<Void, Boolean, Long> implements MainContract.RemoveDatabaseLocationsIntractor {

    public MainContract.RemoveDatabaseLocationsIntractor.OnFinishedListener delegate = null;
    private static final String DATABASE_NAME = "locations_db";
    private LocationDatabase locationDatabase;
    private Locations location;
    private WeakReference<Context> contextRef;

    public RemoveDatabaseLocationsAsyncTask(Context context, Locations location) {
        this.contextRef = new WeakReference<>(context);
        this.location = location;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        try {
            locationDatabase = Room.databaseBuilder((contextRef.get()),
                    LocationDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            locationDatabase.daoAccess().deleteLocation(location);
            return (long) location.getLocationId();
        } catch (Exception e) {
            return null;
        }
    }

    // Formatting finished
    @Override
    protected void onPostExecute(Long identifier) {
        super.onPostExecute(identifier);
        if (identifier != null) delegate.onFinishedRemoveDatabaseLocations(identifier);
        else delegate.onFailureRemoveDatabaseLocations();
    }
}
