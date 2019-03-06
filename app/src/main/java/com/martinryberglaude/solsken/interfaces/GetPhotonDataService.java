package com.martinryberglaude.solsken.interfaces;

import com.martinryberglaude.solsken.network.PhotonRetroLocations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPhotonDataService {
    @GET("/api/")
    Call<PhotonRetroLocations> getData(@Query("q") String search, @Query("limit") int limit);
}
