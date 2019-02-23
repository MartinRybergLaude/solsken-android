package com.martinryberglaude.skyfall.model;
import android.location.Address;
import android.location.Geocoder;

import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.data.TimeOfDay;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainModel {

    public TimeOfDay getTimeOfDay(Coordinate coordinate) {
        String lon = String.format(Locale.US, "%.6f", coordinate.getLon());
        String lat = String.format(Locale.US, "%.6f", coordinate.getLat());
        Calendar[] sunriseSunset = ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(Calendar.getInstance(), coordinate.getLat(), coordinate.getLon());
        Calendar current = Calendar.getInstance();
        Calendar sunriseStart = (Calendar) sunriseSunset[0].clone();
        sunriseStart.add(Calendar.HOUR_OF_DAY, -1);
        Calendar sunriseEnd = (Calendar) sunriseSunset[0].clone();
        sunriseEnd.add(Calendar.HOUR_OF_DAY, 1);
        Calendar sunsetStart = (Calendar) sunriseSunset[1].clone();
        sunsetStart.add(Calendar.HOUR_OF_DAY, -1);
        Calendar sunsetEnd = (Calendar) sunriseSunset[1].clone();
        sunsetEnd.add(Calendar.HOUR_OF_DAY, 1);
        Calendar sunriseNext = (Calendar) sunriseSunset[0].clone();
        sunriseNext.add(Calendar.DAY_OF_MONTH, -1);

        if (current.after(sunriseStart) && current.before(sunriseEnd)) {
            return TimeOfDay.SUNRISE;
        }
        else if (current.after(sunsetStart) && current.before(sunsetEnd)) {
            return TimeOfDay.SUNSET;
        }
        else if (!ca.rmen.sunrisesunset.SunriseSunset.isDay(coordinate.getLat(), coordinate.getLon())) {
            return TimeOfDay.NIGHT;
        }
        else {
            return TimeOfDay.DAY;
        }
    }
    public Calendar[] getSunriseSunset(Coordinate coordinate, Calendar calendar) {
        return ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(Calendar.getInstance(), coordinate.getLat(), coordinate.getLon());
    }
}
