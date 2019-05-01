package com.martinryberglaude.solsken.model;

import android.util.Log;

import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.data.NamedCoordinate;
import com.martinryberglaude.solsken.interfaces.MainContract;
import com.martinryberglaude.solsken.interfaces.GetSMHIDataService;
import com.martinryberglaude.solsken.networkSMHI.SMHIRetroWeatherData;
import com.martinryberglaude.solsken.networkSMHI.SMHIRetrofitClientInstance;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestSMHIWeatherModel implements MainContract.RequestSMHIWeatherIntractor {

    @Override
    public void getSMHIWeatherData(final OnFinishedListerner onFinishedListerner, NamedCoordinate coordinate) {
        String lon = String.format(Locale.US, "%.6f", coordinate.getLon());
        String lat = String.format(Locale.US, "%.6f", coordinate.getLat());
        GetSMHIDataService service = SMHIRetrofitClientInstance.getRetrofitInstance().create(GetSMHIDataService.class);
        Call<SMHIRetroWeatherData> call = service.getData(lon, lat);
        call.enqueue(new Callback<SMHIRetroWeatherData>() {
            @Override
            public void onResponse(Call<SMHIRetroWeatherData> call, Response<SMHIRetroWeatherData> response) {
                onFinishedListerner.onFinishedRetrieveSMHIData(response);
            }
            @Override
            public void onFailure(Call<SMHIRetroWeatherData> call, Throwable t) {
                Log.d("FAIL", t.toString());
                onFinishedListerner.onFailureRetrieveSMHIData(t);
            }
        });
    }
}
