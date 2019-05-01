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

import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.data.NamedCoordinate;
import com.martinryberglaude.solsken.interfaces.MainContract;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RequestLocationModel  implements MainContract.RequestLocationIntractor {

    @SuppressLint("MissingPermission")
    public void getLocation(final OnFinishedListerner onFinishedListener, final Context context) {
        // Get last known location
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(c, true);

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
                    GetAdress getAdress = new GetAdress(context, new Coordinate(location.getLongitude(), location.getLatitude()), onFinishedListener);
                    getAdress.execute();
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
                    GetAdress getAdress = new GetAdress(context, new Coordinate(location.getLongitude(), location.getLatitude()), onFinishedListener);
                    getAdress.execute();
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

    public class GetAdress extends AsyncTask<Void, Void, String> {
        private Context context;
        private Coordinate coordinate;
        private OnFinishedListerner onFinishedListener;

        public GetAdress(Context context, Coordinate coordinate, OnFinishedListerner onFinishedListerner) {
            this.context = context;
            this.coordinate = coordinate;
            this.onFinishedListener = onFinishedListerner;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            String finalLocality;
            try {
                addresses = geocoder.getFromLocation(coordinate.getLat(), coordinate.getLon(), 1);
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
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            onFinishedListener.onFinishedRetrieveLocation(new NamedCoordinate(coordinate.getLon(), coordinate.getLat(), result));
        }
    }
}
