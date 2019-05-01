package com.martinryberglaude.solsken.model;

import android.os.AsyncTask;

import com.martinryberglaude.solsken.App;
import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.data.LocationItem;
import com.martinryberglaude.solsken.interfaces.SearchContract;
import com.martinryberglaude.solsken.networkPHOTON.PhotonRetroFeature;
import com.martinryberglaude.solsken.networkPHOTON.PhotonRetroLocations;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Response;

public class FormatPhotonDataAsyncTask extends AsyncTask<Object, Integer, List<LocationItem>> implements SearchContract.FormatLocationIntractor {

    private SearchContract.FormatLocationIntractor.OnFinishedListener delegate;

    public FormatPhotonDataAsyncTask(SearchContract.FormatLocationIntractor.OnFinishedListener delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<LocationItem> doInBackground(Object... objects) {

        Response<PhotonRetroLocations> response = (Response<PhotonRetroLocations>) objects[0];

        Set<LocationItem> locationList = new LinkedHashSet<>();
        for (PhotonRetroFeature retroFeature : response.body().getFeatures()) {
            if (isCoordinateInBoundaries( new Coordinate(retroFeature.getGeometry().getCoordinates().get(0), retroFeature.getGeometry().getCoordinates().get(1)))
                    && retroFeature.getProperties().getName() != null && retroFeature.getProperties().getCountry() != null && retroFeature.getGeometry().getCoordinates() != null) {
                LocationItem item = new LocationItem();
                item.setCityString(retroFeature.getProperties().getName());
                item.setCountryString(retroFeature.getProperties().getCountry());
                item.setLon(retroFeature.getGeometry().getCoordinates().get(0));
                item.setLat(retroFeature.getGeometry().getCoordinates().get(1));
                locationList.add(item);
            }
        }
        return new ArrayList<>(locationList);
    }

    // Formatting finished
    @Override
    protected void onPostExecute(List<LocationItem> result) {
        super.onPostExecute(result);
        delegate.onFinishedFormatLocations(result);
    }

    private boolean isCoordinateInBoundaries(Coordinate testPoint) {
        List<Coordinate> polygon = new ArrayList<>(App.getMapBoundaries());
        boolean result = false;
        int j = polygon.size() - 1;
        for (int i = 0; i < polygon.size(); i++) {
            if (polygon.get(i).getLon() < testPoint.getLon() && polygon.get(j).getLon() >= testPoint.getLon() || polygon.get(j).getLon() < testPoint.getLon() && polygon.get(i).getLon() >= testPoint.getLon()) {
                if (polygon.get(j).getLat() + (testPoint.getLon() - polygon.get(j).getLon()) / (polygon.get(j).getLon() - polygon.get(i).getLon()) * (polygon.get(j).getLat() - polygon.get(i).getLat()) < testPoint.getLat()) {
                    result = !result;
                }
            }
            j = i;
        }
        return result;
    }
}
