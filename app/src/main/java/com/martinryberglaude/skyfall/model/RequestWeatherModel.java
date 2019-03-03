package com.martinryberglaude.skyfall.model;

import android.util.Log;

import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.interfaces.GetWeatherDataService;
import com.martinryberglaude.skyfall.network.SMHIRetroWeatherData;
import com.martinryberglaude.skyfall.network.SMHIRetrofitClientInstance;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestWeatherModel implements MainContract.RequestWeatherIntractor {

    @Override
    public void getWeatherData(final OnFinishedListerner onFinishedListerner, Coordinate coordinate) {
        String lon = String.format(Locale.US, "%.6f", coordinate.getLon());
        String lat = String.format(Locale.US, "%.6f", coordinate.getLat());
        GetWeatherDataService service = SMHIRetrofitClientInstance.getRetrofitInstance().create(GetWeatherDataService.class);
        Call<SMHIRetroWeatherData> call = service.getData(lon, lat);
        call.enqueue(new Callback<SMHIRetroWeatherData>() {
            @Override
            public void onResponse(Call<SMHIRetroWeatherData> call, Response<SMHIRetroWeatherData> response) {

                onFinishedListerner.onFinishedRetrieveData(response);
            }
            @Override
            public void onFailure(Call<SMHIRetroWeatherData> call, Throwable t) {
                Log.d("FAIL", t.toString());
                onFinishedListerner.onFailureRetrieveData(t);
            }
        });
    }
}
