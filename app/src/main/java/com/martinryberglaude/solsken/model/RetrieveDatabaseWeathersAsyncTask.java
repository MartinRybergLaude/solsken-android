package com.martinryberglaude.solsken.model;

import android.content.Context;
import android.os.AsyncTask;

import com.martinryberglaude.solsken.database.WeatherDatabase;
import com.martinryberglaude.solsken.database.Weathers;
import com.martinryberglaude.solsken.interfaces.MainContract;

import java.lang.ref.WeakReference;

import androidx.room.Room;

public class RetrieveDatabaseWeathersAsyncTask extends AsyncTask<Object, Integer, Weathers> implements MainContract.RetrieveDatabaseWeathersIntractor {

    private OnFinishedListener delegate;
    private static final String DATABASE_NAME = "weathers_db";
    private WeatherDatabase weatherDatabase;
    private WeakReference<Context> contextRef;
    private String id;

    public RetrieveDatabaseWeathersAsyncTask(Context context, String id, OnFinishedListener delegate) {
        this.contextRef = new WeakReference<>(context);
        this.id = id;
        this.delegate = delegate;
    }

    @Override
    protected Weathers doInBackground(Object... objects) {

        weatherDatabase = Room.databaseBuilder((contextRef.get()),
                WeatherDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        Weathers weathers = weatherDatabase.daoAccess().fetchWeathersById(id);
        weatherDatabase.close();

        return weathers;
    }

    // Formatting finished
    @Override
    protected void onPostExecute(Weathers result) {
        super.onPostExecute(result);
        delegate.onFinishedRetrieveDatabaseWeathers(result);
    }
}
