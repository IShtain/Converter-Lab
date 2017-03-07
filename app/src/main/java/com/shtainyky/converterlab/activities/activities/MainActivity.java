package com.shtainyky.converterlab.activities.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.fragments.OrganizationsFragment;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements OrganizationsFragment.OnOrganizationClickListener{
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
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        addFragmentWithBackStack(OrganizationsFragment.newInstance());
        logger.d(TAG, "onCreate**********************************************");
    }

    @Override
    public void onCallClick(String organizationPhone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + organizationPhone));
        startActivity(intent);
    }

    @Override
    public void onMapClick(String organizationAddress) {
        Toast.makeText(this, "ibMap", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLinkClick(String organizationLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(organizationLink));
        startActivity(intent);
    }

    @Override
    public void onDetailClick(OrganizationUI organization) {
        Toast.makeText(this, "onDetailClick", Toast.LENGTH_LONG).show();
    }
}
