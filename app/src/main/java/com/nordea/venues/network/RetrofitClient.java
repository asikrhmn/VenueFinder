package com.nordea.venues.network;

import com.nordea.venues.entities.Venue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private final Api api;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(buildGsonConverter())
                .build();
        api = retrofit.create(Api.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TypeToken.getParameterized(List.class, Venue.class).getType(), new VenueListDeserializer());
        Gson myGson = gsonBuilder.create();
        return GsonConverterFactory.create(myGson);
    }

    public Api getApi() {
        return api;
    }
}