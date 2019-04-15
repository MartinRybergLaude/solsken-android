package com.martinryberglaude.solsken.model;

import android.content.Context;
import android.os.AsyncTask;

import com.martinryberglaude.solsken.database.LocationDatabase;
import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.interfaces.MainContract;

import java.lang.ref.WeakReference;

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
            locationDatabase.close();
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
