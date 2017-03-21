package com.shtainyky.converterlab.activities.activities;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.fragments.DetailFragment;
import com.shtainyky.converterlab.activities.fragments.listeners.OnBackPressedListener;
import com.shtainyky.converterlab.activities.fragments.listeners.OnOrganizationClickListener;
import com.shtainyky.converterlab.activities.fragments.OrganizationsFragment;
import com.shtainyky.converterlab.activities.fragments.ShareDialogFragment;
import com.shtainyky.converterlab.activities.fragments.MapFragment;
import com.shtainyky.converterlab.activities.models.modelGeoLatLng.GeoLatLng;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.util.Util;

public class MainActivity extends BaseActivity implements OnOrganizationClickListener {
    private static final String TAG = "MainActivity";


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerResId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            addFragmentWithBackStack(OrganizationsFragment.newInstance());
        logger.d(TAG, "onCreate**********************************************");
    }

    @Override
    public void onCallClick(String organizationPhone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + organizationPhone));
        startIntentIfItIsSafe(intent);
    }

    @Override
    public void onMapClick(final String organizationAddress) {
        logger.d(TAG, "onMapClick = " + organizationAddress);
        if (Util.isOnline(getApplicationContext())) {
            final ProgressDialog dialog = getProgressDialog();
            dialog.show();
            GeoLatLng.getLatLng(this, organizationAddress,
                    new GeoLatLng.GeoPlaceListener() {
                        @Override
                        public void onSuccess(GeoLatLng geoLatLng) {
                            logger.d(TAG, "geoLatLng.getLat() = " + geoLatLng.getLat());
                            logger.d(TAG, "geoLatLng.getLng() = " + geoLatLng.getLng());
                            if (!isChangingConfigurations()) {
                                addFragmentWithBackStack(MapFragment.newInstance(geoLatLng.getLat(),
                                        geoLatLng.getLng()));
                                dialog.cancel();
                            }
                        }

                        @Override
                        public void onFailure() {
                            dialog.cancel();
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.message_not_valid_address, organizationAddress),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } else
            Toast.makeText(MainActivity.this,
                    getString(R.string.no_internet_connection_map),
                    Toast.LENGTH_LONG).show();
    }

    private ProgressDialog getProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading_lat_lng));
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

    @Override
    public void onLinkClick(String organizationLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(organizationLink));
        startIntentIfItIsSafe(intent);
    }

    @Override
    public void onDetailClick(OrganizationUI organization) {
        addFragmentWithBackStack(DetailFragment.newInstance(organization.getId()));
        logger.d(TAG, organization.getId());
    }

    @Override
    public void onShareClick(OrganizationUI organization) {
        DialogFragment shareDialogFragment = ShareDialogFragment.newInstance(organization.getId());
        shareDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
        shareDialogFragment.show(getFragmentManager(), "ShareDialogFragment");

    }

    private void startIntentIfItIsSafe(Intent intent) {
        if (Util.isIntentSafe(this, intent))
            startActivity(intent);
        else
            Toast.makeText(this, "You haven't application for view this address", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }
        if (backPressedListener != null) {
            logger.d(TAG, "backPressedListener() = >");
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
            logger.d(TAG, "onBackPressed() = >");
            changeActionBarTitleAndSubTitle();
        }
    }

    private void changeActionBarTitleAndSubTitle() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setTitle(R.string.app_name);
            ab.setSubtitle("");
        }
    }
}




