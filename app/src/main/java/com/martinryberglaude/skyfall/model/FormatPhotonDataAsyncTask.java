package com.martinryberglaude.skyfall.model;

import android.os.AsyncTask;
import android.util.Log;

import com.martinryberglaude.skyfall.data.LocationItem;
import com.martinryberglaude.skyfall.interfaces.SearchContract;
import com.martinryberglaude.skyfall.network.PhotonRetroFeature;
import com.martinryberglaude.skyfall.network.PhotonRetroLocations;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FormatPhotonDataAsyncTask extends AsyncTask<Object, Integer, List<LocationItem>> implements SearchContract.FormatLocationIntractor {

    public SearchContract.FormatLocationIntractor.OnFinishedListener delegate = null;
    private List<LocationItem> locationList = new ArrayList<>();

    @Override
    protected List<LocationItem> doInBackground(Object... objects) {

        Response<PhotonRetroLocations> response = (Response<PhotonRetroLocations>) objects[0];
        Log.d("ASYNC", response.body().toString());

        for (PhotonRetroFeature retroFeature : response.body().getFeatures()) {
            LocationItem item = new LocationItem();
            item.setCityString(retroFeature.getProperties().getName());
            item.setCountryString(retroFeature.getProperties().getCountry());
            item.setLon(retroFeature.getGeometry().getCoordinates().get(0));
            item.setLat(retroFeature.getGeometry().getCoordinates().get(1));
            locationList.add(item);
        }

        return locationList;
    }

    // Formatting finished
    @Override
    protected void onPostExecute(List<LocationItem> result) {
        super.onPostExecute(result);
        delegate.onFinishedFormatLocations(result);
    }
}
