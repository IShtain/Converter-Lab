package com.shtainyky.converterlab.activities.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.shtainyky.converterlab.activities.fragments.BaseFragment;
import com.shtainyky.converterlab.activities.util.logger.LogManager;
import com.shtainyky.converterlab.activities.util.logger.Logger;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity{
    private static final String TAG = "BaseActivity";
    protected Logger logger;
    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract int getFragmentContainerResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        logger = LogManager.getLogger();
    }

    protected void addFragmentWithBackStack(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentFromContainer = fragmentManager.findFragmentById(getFragmentContainerResId());
        if (fragmentFromContainer == null) {
            fragmentManager.beginTransaction()
                    .add(getFragmentContainerResId(), fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(getFragmentContainerResId(), fragment)
                    .addToBackStack(null)
                    .commit();
        }
        logger.d(TAG, "addFragmentWithBackStack");
    }
}
