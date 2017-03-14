package com.shtainyky.converterlab.activities.activities;


import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.fragments.DetailFragment;
import com.shtainyky.converterlab.activities.fragments.OnOrganizationClickListener;
import com.shtainyky.converterlab.activities.fragments.OrganizationsFragment;
import com.shtainyky.converterlab.activities.fragments.ShareDialogFragment;
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
    public void onMapClick(String organizationAddress) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + organizationAddress));
        startIntentIfItIsSafe(intent);
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
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(organization.getName());
            ab.setSubtitle(organization.getCityName());
        }
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
        super.onBackPressed();
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.app_name);
            ab.setSubtitle("");
            ab.setDisplayHomeAsUpEnabled(false);
        }
    }
}
