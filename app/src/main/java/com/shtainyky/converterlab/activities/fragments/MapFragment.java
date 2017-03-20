package com.shtainyky.converterlab.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.activities.MainActivity;

import butterknife.ButterKnife;

public class MapFragment extends BaseFragment<MainActivity> implements OnMapReadyCallback {
    private static final String ARG_LATITUDE = "mLatitude";
    private static final String ARG_LONGITUDE = "mLongitude";
    private double mLatitude;
    private double mLongitude;


    public static MapFragment newInstance(double latitude, double longitude) {
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        MapView mapView = ButterKnife.findById(view, R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mLatitude = bundle.getDouble(ARG_LATITUDE);
            mLongitude = bundle.getDouble(ARG_LONGITUDE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude)));

        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(mLatitude, mLongitude)).zoom(17).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }


}
