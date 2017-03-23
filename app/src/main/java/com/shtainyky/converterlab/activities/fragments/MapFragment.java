package com.shtainyky.converterlab.activities.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
        setHasOptionsMenu(true);
        setupActionBar();
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
        if (getContext() == null) return;
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude)));

        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(mLatitude, mLongitude)).zoom(17).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    private void setupActionBar() {
        ActionBar ab = getActivityGeneric().getSupportActionBar();
        Drawable drawable = ResourcesCompat.getDrawable(getActivityGeneric().getResources(), R.drawable.ic_action_arrow, null);
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(drawable);
            ab.setTitle(getContext().getString(R.string.app_name));
            ab.setSubtitle("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivityGeneric().getSupportFragmentManager().popBackStack();
                if (getActivityGeneric().getSupportActionBar() != null)
                    getActivityGeneric().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
