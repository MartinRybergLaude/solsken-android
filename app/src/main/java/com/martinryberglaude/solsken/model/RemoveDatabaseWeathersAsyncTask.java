package com.martinryberglaude.solsken.model;

import android.content.Context;
import android.os.AsyncTask;

import com.martinryberglaude.solsken.database.LocationDatabase;
import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.database.WeatherDatabase;
import com.martinryberglaude.solsken.database.Weathers;
import com.martinryberglaude.solsken.interfaces.MainContract;

import java.lang.ref.WeakReference;

import androidx.room.Room;

public class RemoveDatabaseWeathersAsyncTask extends AsyncTask<Void, Boolean, String> implements MainContract.RemoveDatabaseWeathersIntractor {

    public OnFinishedListener delegate = null;
    private static final String DATABASE_NAME = "weathers_db";
    private WeatherDatabase weatherDatabase;
    private Weathers weather;
    private WeakReference<Context> contextRef;

    public RemoveDatabaseWeathersAsyncTask(Context context, Weathers weather) {
        this.contextRef = new WeakReference<>(context);
        this.weather = weather;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            weatherDatabase = Room.databaseBuilder((contextRef.get()),
                    WeatherDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            weatherDatabase.daoAccess().deleteWeather(weather);
            return (String) weather.getWeatherId();
        } catch (Exception e) {
            return null;
        }
    }

    // Formatting finished
    @Override
    protected void onPostExecute(String identifier) {
        super.onPostExecute(identifier);
        if (identifier != null) delegate.onFinishedRemoveDatabaseWeathers(identifier);
        else delegate.onFailureRemoveDatabaseWeathers();
    }
}
