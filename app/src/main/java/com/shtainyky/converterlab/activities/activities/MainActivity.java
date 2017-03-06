package com.shtainyky.converterlab.activities.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.fragments.OrganizationsFragment;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.services.LoadingBindService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private LoadingBindService mService;
    private boolean mBound = false;
    private boolean mIsLoaded;

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

        if (savedInstanceState == null) {
            OrganizationsFragment organizationFragment = new OrganizationsFragment();
            addFragment(organizationFragment);
        }
        logger.d(TAG, "onCreate**********************************************");
    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("custom-event-name"));
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LoadingBindService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//
//        return super.onOptionsItemSelected(item);
//    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LoadingBindService.MyBinder binder = (LoadingBindService.MyBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService = null;
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mIsLoaded = true;
        }
    };

    public List<OrganizationUI> getOrganizations() {
        logger.d(TAG, "getOrganizationDataFromDB");
        if (!mIsLoaded)
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return StoreData.getListOrganizationsUI();
    }

}
