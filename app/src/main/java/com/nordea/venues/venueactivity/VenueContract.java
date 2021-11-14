package com.nordea.venues.venueactivity;


import com.nordea.venues.entities.Venue;

import java.util.List;

public interface VenueContract {

    interface Presenter {

        void onDestroy();

        void requestDataFromServer(double latitude, double longitude, String query);

    }

    interface View {

        void showProgress();

        void hideProgress();

        void updateVenueList(List<Venue> venueList);

        void onResponseFailure(Throwable throwable);

    }

    interface Model {
        void getVenueList(double latitude, double longitude, String query, APIListener apiListener);
    }

    interface APIListener {
        void onSuccess(List<Venue> venueList);

        void onFailure(Throwable t);
    }

}