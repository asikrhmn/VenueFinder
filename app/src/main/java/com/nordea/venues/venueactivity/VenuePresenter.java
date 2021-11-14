package com.nordea.venues.venueactivity;

import com.nordea.venues.entities.Venue;

import java.util.List;

public class VenuePresenter implements VenueContract.Presenter, VenueContract.APIListener {
    private VenueContract.View view;
    private final VenueContract.Model model;

    public VenuePresenter(VenueContract.View view, VenueModel venueModel) {
        this.view = view;
        model = venueModel;
    }

    @Override
    public void onDestroy() {
        view = null;
    }


    @Override
    public void requestDataFromServer(double latitude, double longitude, String query) {
        view.showProgress();
        model.getVenueList(latitude, longitude, query, this);
    }

    @Override
    public void onSuccess(List<Venue> venueList) {
        view.hideProgress();
        view.updateVenueList(venueList);
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.onResponseFailure(t);
    }
}
