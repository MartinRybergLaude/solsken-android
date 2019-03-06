package com.martinryberglaude.solsken.interfaces;

import com.martinryberglaude.solsken.network.SMHIRetroWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetWeatherDataService {

    @GET("lon/{lon}/lat/{lat}/data.json")
    Call<SMHIRetroWeatherData> getData(@Path("lon") String lon, @Path("lat") String lat);
}
