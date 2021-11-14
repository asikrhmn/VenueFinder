package com.nordea.venues.venueactivity;

import com.nordea.venues.entities.Venue;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VenuePresenterTest {
    @Mock
    private VenueContract.View view;

    @Mock
    private VenueModel venueModel;

    private VenuePresenter presenter;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new VenuePresenter(view, venueModel);
    }

    @Test
    public void requestDataFromServerSuccess() {
        presenter.requestDataFromServer(65.946472, 27.246094, "query");
        verify(venueModel, times(1)).getVenueList(65.946472, 27.246094, "query", presenter);
        presenter.onSuccess(getList());
        verify(view, times(1)).hideProgress();
        ArgumentCaptor<List> entityArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(view).updateVenueList(entityArgumentCaptor.capture());
        Assert.assertTrue(entityArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void requestDataFromServerError() {
        presenter.requestDataFromServer(65.946472, 27.246094, "query");
        verify(venueModel, times(1)).getVenueList(65.946472, 27.246094, "query", presenter);
        Throwable t = new Throwable();
        presenter.onFailure(t);
        verify(view, times(1)).hideProgress();
        verify(view).onResponseFailure(t);
    }

    private List<Venue> getList() {
        ArrayList<Venue> venues = new ArrayList<>();
        venues.add( new Venue());
        venues.add( new Venue());
        return venues;
    }
}