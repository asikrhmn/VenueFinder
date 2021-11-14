package com.nordea.venues.venueactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nordea.venues.R;
import com.nordea.venues.adapters.VenueAdapter;
import com.nordea.venues.entities.Venue;

import java.util.ArrayList;
import java.util.List;

public class VenueActivity extends AppCompatActivity implements VenueContract.View {
    private static final int REQUEST_LOCATION = 1;

    private VenueAdapter venueAdapter;
    private VenueContract.Presenter presenter;
    private LocationManager locationManager;
    private Toast locationWarningToast;
    private AlertDialog gpsAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        presenter = new VenuePresenter(this, new VenueModel());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initUi();
    }

    private void initUi() {
        RecyclerView rvVenueList = findViewById(R.id.rv_venue_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VenueActivity.this);
        rvVenueList.setLayoutManager(layoutManager);
        venueAdapter = new VenueAdapter();
        rvVenueList.setAdapter(venueAdapter);
        SearchView searchView = findViewById(R.id.sv_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchVenues(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchVenues(query);
                return false;
            }
        });
        searchView.setIconified(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void searchVenues(String query) {
        if (checkLocationSettings()) {
            Location currentLocation = getLastKnownLocation();
            if (currentLocation != null) {
                presenter.requestDataFromServer(currentLocation.getLatitude(), currentLocation.getLongitude(), query);
                return;
            }
        }
        if (locationWarningToast != null) {
            locationWarningToast.cancel();
        }
        locationWarningToast = Toast.makeText(this, R.string.location_not_available, Toast.LENGTH_LONG);
        locationWarningToast.show();
    }

    @Override
    public void showProgress() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateVenueList(List<Venue> venueList) {
        venueAdapter.setData(venueList);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        venueAdapter.setData(new ArrayList<>());
    }

    private boolean checkLocationSettings() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            turnOnGps();
            return false;
        } else {
            return checkLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    private void turnOnGps() {
        if (gpsAlertDialog == null || !gpsAlertDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.enable_gps).setCancelable(false).setPositiveButton(R.string.yes,
                    (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());
            gpsAlertDialog = builder.create();
            gpsAlertDialog.show();
        }
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            @SuppressLint("MissingPermission")
            Location l = locationManager.getLastKnownLocation(provider);
            if (l != null) {
                return l;
            }
        }
        return null;
    }
}