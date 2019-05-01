package com.martinryberglaude.solsken;

import android.app.Application;
import android.content.res.Resources;

import com.martinryberglaude.solsken.data.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static App mInstance;
    private static Resources res;
    private static List<Coordinate> boundary = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
        boundary.add(new Coordinate(2.250475, 52.500440));
        boundary.add(new Coordinate(27.392184, 52.542473));
        boundary.add(new Coordinate(37.934697, 70.742227));
        boundary.add(new Coordinate(-8.553029, 70.666011));
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getResourses() {
        return res;
    }

    public static List<Coordinate> getMapBoundaries() {
        return boundary;
    }


}
