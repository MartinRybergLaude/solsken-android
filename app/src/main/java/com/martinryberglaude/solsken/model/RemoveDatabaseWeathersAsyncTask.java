package com.martinryberglaude.solsken.model;

import android.content.Context;
import android.os.AsyncTask;

import com.martinryberglaude.solsken.database.LocationDatabase;
import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.database.WeatherDatabase;
import com.martinryberglaude.solsken.database.Weathers;
import com.martinryberglaude.solsken.interfaces.MainContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

public class RemoveDatabaseWeathersAsyncTask extends AsyncTask<Void, Boolean, String> implements MainContract.RemoveDatabaseWeathersIntractor {

    private OnFinishedListener delegate;
    private static final String DATABASE_NAME = "weathers_db";
    private WeatherDatabase weatherDatabase;
    private List<Weathers> weathers;
    private WeakReference<Context> contextRef;

    public RemoveDatabaseWeathersAsyncTask(Context context, List<Weathers> weathers, OnFinishedListener delegate) {
        this.contextRef = new WeakReference<>(context);
        this.weathers = new ArrayList<>(weathers);
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            weatherDatabase = Room.databaseBuilder((contextRef.get()),
                    WeatherDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            for (Weathers weather : weathers) {
                weatherDatabase.daoAccess().deleteWeather(weather);
            }
            weatherDatabase.close();
            return (String) weathers.get(0).getWeatherId();
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
