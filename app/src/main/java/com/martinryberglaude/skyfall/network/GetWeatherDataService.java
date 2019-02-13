package com.martinryberglaude.skyfall.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetWeatherDataService {

    @GET("lon/{lon}/lat/{lat}/data.json")
    Call<RetroWeatherData> getData(@Path("lon") String lon, @Path("lat") String lat);
}
