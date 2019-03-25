package com.martinryberglaude.solsken.model;

import android.content.Context;
import android.os.AsyncTask;

import com.martinryberglaude.solsken.database.LocationDatabase;
import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.interfaces.MainContract;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.room.Room;

public class RetrieveDatabaseLocationsAsyncTask extends AsyncTask<Object, Integer, List<Locations>> implements MainContract.RetrieveDatabaseLocationsIntractor {

    public MainContract.RetrieveDatabaseLocationsIntractor.OnFinishedListener delegate = null;
    private static final String DATABASE_NAME = "locations_db";
    private LocationDatabase locationDatabase;
    private WeakReference<Context> contextRef;

    public RetrieveDatabaseLocationsAsyncTask(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    @Override
    protected List<Locations> doInBackground(Object... objects) {

        locationDatabase = Room.databaseBuilder((contextRef.get()),
                LocationDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        List<Locations> allLocations = locationDatabase.daoAccess().fetchAllLocations();

        return allLocations;
    }

    // Formatting finished
    @Override
    protected void onPostExecute(List<Locations> result) {
        super.onPostExecute(result);
        delegate.onFinishedRetrieveDatabaseLocations(result);
    }
}
