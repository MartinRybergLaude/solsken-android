package com.martinryberglaude.solsken.model;

import android.util.Log;

import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.data.NamedCoordinate;
import com.martinryberglaude.solsken.interfaces.GetYRDataService;
import com.martinryberglaude.solsken.interfaces.MainContract;
import com.martinryberglaude.solsken.networkYR.YRRetroWeatherData;
import com.martinryberglaude.solsken.networkYR.YRRetrofitClientInstance;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestYRWeatherModel implements MainContract.RequestYRWeatherIntractor {

    @Override
    public void getYRWeatherData(final OnFinishedListerner onFinishedListerner, NamedCoordinate coordinate) {
        String lon = String.format(Locale.US, "%.6f", coordinate.getLon());
        String lat = String.format(Locale.US, "%.6f", coordinate.getLat());
        GetYRDataService service = YRRetrofitClientInstance.getRetrofitInstance().create(GetYRDataService.class);
        Call<YRRetroWeatherData> call = service.getData(lon, lat);
        call.enqueue(new Callback<YRRetroWeatherData>() {
            @Override
            public void onResponse(Call<YRRetroWeatherData> call, Response<YRRetroWeatherData> response) {
                onFinishedListerner.onFinishedRetrieveYRData(response);
            }
            @Override
            public void onFailure(Call<YRRetroWeatherData> call, Throwable t) {
                Log.d("FAIL", t.toString());
                onFinishedListerner.onFailureRetrieveYRData(t);
            }
        });
    }
}
