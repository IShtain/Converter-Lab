package com.shtainyky.converterlab.activities.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import java.util.List;

public class DetailActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected int getFragmentContainerResId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
}
