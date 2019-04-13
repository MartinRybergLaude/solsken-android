package com.martinryberglaude.solsken.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;

import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.interfaces.MainContract;

public class RequestLocationModel implements MainContract.RequestLocationIntractor {

    private LocationManager locationManager;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    public RequestLocationModel(LocationManager locationManager) {
        this.locationManager = locationManager;
    }
    private int locationAgeMinutes(Location last) {
        return (int) locationAgeMillis(last) / (60*1000);
    }
    private long locationAgeSeconds(Location last) {
        return (locationAgeMillis(last) / 1000);
    }
    private long locationAgeMillis(Location last) {
        return (SystemClock.elapsedRealtimeNanos() - last
                .getElapsedRealtimeNanos()) / 1000000;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getLocation(final OnFinishedListerner onFinishedListener, Context context) {

        // Get last known location
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(c, true);
        Location lastKnownLocation;
        lastKnownLocation = locationManager.getLastKnownLocation(provider);

        // Check if last known location was recorded less than two minutes ago
        if (lastKnownLocation != null && locationAgeMillis(lastKnownLocation) < TWO_MINUTES) {
            onFinishedListener.onFinishedRetrieveLocation(new Coordinate(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude()));
        } else {
            // Last known location was too old, retreieve new location
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            final Looper looper = null;
            // Prefer gps location
            if (isGPSEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        onFinishedListener.onFinishedRetrieveLocation(new Coordinate(location.getLongitude(), location.getLatitude()));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                }, looper);
                // No network available, get location through GPS
            } else if (isNetworkEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        onFinishedListener.onFinishedRetrieveLocation(new Coordinate(location.getLongitude(), location.getLatitude()));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                }, looper);
            } else {
                onFinishedListener.onFailureRetrieveLocationn();
            }
        }
    }
}
