package com.martinryberglaude.solsken.interfaces;

import com.martinryberglaude.solsken.networkYR.YRRetroWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetYRDataService {

    @GET(".json")
    Call<YRRetroWeatherData> getData(@Query("lon") String lon, @Query("lat") String lat);
}
