package com.martinryberglaude.skyfall.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.interfaces.MainContract;

public class RequestLocationModel implements MainContract.RequestLocationIntractor {

    private LocationManager locationManager;

    public RequestLocationModel(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getLocation(final OnFinishedListerner onFinishedListener, Context context) {
        Log.d("getLocation", "CALLED");
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final Looper looper = null;
        if (isNetworkEnabled) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("NETWORK", "LOCATION CHANGED");
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
        } else if (isGPSEnabled) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("GPS", "LOCATION CHANGED");
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
        }
    }
}
