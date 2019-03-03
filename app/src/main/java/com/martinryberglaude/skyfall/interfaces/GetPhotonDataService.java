package com.martinryberglaude.skyfall.interfaces;

import com.martinryberglaude.skyfall.network.PhotonRetroLocations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPhotonDataService {
    @GET("/api/")
    Call<PhotonRetroLocations> getData(@Query("q") String search, @Query("limit") int limit);
}
