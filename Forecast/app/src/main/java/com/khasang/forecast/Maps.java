package com.khasang.forecast;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.khasang.forecast.activities.CityPickerActivity;

/**
 * Created by uoles on 03.02.2016.
 */

public class Maps {

    private com.google.android.gms.maps.GoogleMap map;
    private final String TAG = "mapLogs";
    private double currentLatitude = 0;
    private double currentLongtitude = 0;
    private float currentZoom = 0;
    private CityPickerActivity activity;
    private FragmentManager myFM;


    public Maps(CityPickerActivity activity) {
        this.activity = activity;
        if (setMap()) {
            setMapSettings();
            getCurrentLocation();
            initLogs();
            setCameraPosition(currentLatitude, currentLongtitude, 13, 0, 0);
        };
    }

    public boolean setMap() {
        myFM = activity.getSupportFragmentManager();
        map = ((SupportMapFragment) myFM.findFragmentById(R.id.map)).getMap();
        if (map == null) {
            return false;
        }
        return true;
    }

    public void setMapSettings() {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
    }

    public void setCameraPosition(double latitude, double longtitude, float zoom, float bearing, float tilt) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longtitude))
                .zoom(zoom)
                .bearing(bearing)
                .tilt(tilt)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    public void setNewLatLng(double latitude, double longtitude) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(latitude, longtitude));
        map.animateCamera(cameraUpdate);
    }

    public LatLng getCurrentLocation() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            currentLatitude = location.getLatitude();
            currentLongtitude = location.getLongitude();
            return new LatLng(currentLatitude, currentLongtitude);
        }
        return null;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongtitude() {
        return currentLongtitude;
    }

    public float getCurrentZoom() {
        return currentZoom;
    }

    public void setNewLatLngZoom(double latitude, double longtitude, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longtitude), zoom);
        map.animateCamera(cameraUpdate);
    }

    public void setNewZoom(float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(zoom);
        map.animateCamera(cameraUpdate);
    }

    private void initLogs() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick: " + latLng.latitude + "," + latLng.longitude);
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d(TAG, "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);
            }
        });

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d(TAG, "onCameraChange: lat " + camera.target.latitude + ", lng " + camera.target.longitude + ", zoom " + camera.zoom);
            }
        });
    }
}
