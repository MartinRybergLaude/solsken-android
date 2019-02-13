package com.martinryberglaude.skyfall.model;

import android.util.Log;

import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.data.SMHIWeatherSymbol;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.data.EventItem;
import com.martinryberglaude.skyfall.data.HeaderItem;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.network.GetWeatherDataService;
import com.martinryberglaude.skyfall.network.RetroParameter;
import com.martinryberglaude.skyfall.network.RetroTimeSeries;
import com.martinryberglaude.skyfall.network.RetroWeatherData;
import com.martinryberglaude.skyfall.network.RetrofitClientInstance;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestWeatherModel implements MainContract.RequestWeatherIntractor {

    @Override
    public void getWeatherData(final OnFinishedListerner onFinishedListerner, Coordinate coordinate) {
        String lon = String.format(Locale.US, "%.6f", coordinate.getLon());
        String lat = String.format(Locale.US, "%.6f", coordinate.getLat());
        GetWeatherDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetWeatherDataService.class);
        Call<RetroWeatherData> call = service.getData(lon, lat);
        call.enqueue(new Callback<RetroWeatherData>() {
            @Override
            public void onResponse(Call<RetroWeatherData> call, Response<RetroWeatherData> response) {

                onFinishedListerner.onFinishedRetrieveData(response);
            }
            @Override
            public void onFailure(Call<RetroWeatherData> call, Throwable t) {
                Log.d("FAIL", t.toString());
                onFinishedListerner.onFailureRetrieveData(t);
            }
        });
    }
}
