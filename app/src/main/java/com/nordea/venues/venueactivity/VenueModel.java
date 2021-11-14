package com.nordea.venues.venueactivity;

import androidx.annotation.NonNull;

import com.nordea.venues.entities.Venue;
import com.nordea.venues.network.Api;
import com.nordea.venues.network.RetrofitClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VenueModel implements VenueContract.Model {
    @Override
    public void getVenueList(double latitude, double longitude, String query, VenueContract.APIListener apiListener) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", Api.CLIENT_ID);
        params.put("client_secret", Api.CLIENT_SECRET);
        params.put("ll", latitude + "," + longitude);
        params.put("query", query);
        params.put("limit", "100");
        params.put("v", "20211113");// App supports API changes up to this date
        Call<List<Venue>> call = RetrofitClient.getInstance().getApi().getVenues(params);
        call.enqueue(new Callback<List<Venue>>() {
            @Override
            public void onResponse(@NonNull Call<List<Venue>> call, @NonNull Response<List<Venue>> response) {
                List<Venue> venues = response.body();
                apiListener.onSuccess(venues);
            }

            @Override
            public void onFailure(Call<List<Venue>> call, Throwable t) {
                apiListener.onFailure(t);
            }
        });
    }
}
