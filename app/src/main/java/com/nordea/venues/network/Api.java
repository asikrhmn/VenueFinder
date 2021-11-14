package com.nordea.venues.network;

import com.nordea.venues.entities.Venue;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface Api {
    String BASE_URL = "https://api.foursquare.com/v2/";
    String CLIENT_ID = "";
    String CLIENT_SECRET = "";

    @GET("venues/search")
    Call<List<Venue>> getVenues(@QueryMap Map<String, String> params);
}
