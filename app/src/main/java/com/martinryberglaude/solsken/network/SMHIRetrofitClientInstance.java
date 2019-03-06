package com.martinryberglaude.solsken.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SMHIRetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
