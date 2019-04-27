package com.martinryberglaude.solsken.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;

import com.martinryberglaude.solsken.MainActivity;
import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.data.LocationItem;
import com.martinryberglaude.solsken.interfaces.MainContract;
import com.martinryberglaude.solsken.interfaces.SearchContract;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RequestLocationModel extends AsyncTask<Object, Integer, Coordinate> implements MainContract.RequestLocationIntractor {

    private LocationManager locationManager;
    private Context context;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public MainContract.RequestLocationIntractor.OnFinishedListerner delegate = null;

    public RequestLocationModel(LocationManager locationManager, Context context) {
        this.locationManager = locationManager;
        this.context = context;
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
    protected Coordinate doInBackground(Object... objects) {
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
            delegate.onFinishedRetrieveLocation(new Coordinate(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude()));
        } else {
            // Last known location was too old, retrieve new location
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
                        return new Coordinate(location.getLongitude(), location.getLatitude(), requestAdressString(location.getLatitude(), location.getLongitude()));
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
                onFinishedListener.onFailureRetrieveLocation();
            }
        }
    }
    private String requestAdressString(double lon, double lat) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String finalLocality;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() < 1) {
                return "?";
            }
            String city = addresses.get(0).getLocality();
            String subCity = addresses.get(0).getSubLocality();
            String subAdminArea = addresses.get(0).getSubAdminArea();
            String streetAdress = addresses.get(0).getFeatureName();

            if (subCity == null) {
                if (city == null) {
                    if (subAdminArea == null) {
                        if (streetAdress == null) {
                            finalLocality = "?";
                        } else {
                            finalLocality = streetAdress;
                        }
                    } else {
                        finalLocality = subAdminArea;
                    }
                } else {
                    finalLocality = city;
                }
            } else {
                finalLocality = subCity;
            }
            return finalLocality;
        } catch (IOException e) {
            e.printStackTrace();
            return "?";
        }
    }
}
