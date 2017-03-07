package com.shtainyky.converterlab.activities.fragments;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.activities.MainActivity;
import com.shtainyky.converterlab.activities.adapter.OrganizationsRecyclerViewAdapter;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.services.LoadingBindService;
import com.shtainyky.converterlab.activities.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class OrganizationsFragment extends BaseFragment<MainActivity> implements SearchView.OnQueryTextListener,
        OrganizationsRecyclerViewAdapter.OnItemClickListener {
    public static final String TAG = "OrganizationsFragment";
    private RecyclerView organizationsRecyclerView;
    private RelativeLayout relativeLayout;
    private Logger logger;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private TextView textView;

    private LoadingBindService mService;
    private boolean mBound = false;
    private OnOrganizationClickListener mOrganizationClickListener;
    private OrganizationsRecyclerViewAdapter mAdapter;

    public static OrganizationsFragment newInstance() {
        Bundle args = new Bundle();
        OrganizationsFragment fragment = new OrganizationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_organizations;
    }

    // Required empty public constructor
    public OrganizationsFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOrganizationClickListener = (OnOrganizationClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger = LogManager.getLogger();
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        organizationsRecyclerView = ButterKnife.findById(view, R.id.recycler_view_organizations);
        progressBar = ButterKnife.findById(view, R.id.progress);
        relativeLayout = ButterKnife.findById(view, R.id.main_layout);
        refreshLayout = ButterKnife.findById(view, R.id.swipe_refresh_for_organizations);
        textView = ButterKnife.findById(view, R.id.tv_no_data);
        swipeRefreshListener(refreshLayout);
    }

    private void swipeRefreshListener(final SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.d(TAG, "onRefresh");
                refreshDatabase();
            }
        });
    }

    public void refreshDatabase() {
        logger.d(TAG, "refreshDatabase");
        if (mBound) {
            mService.loadAndSaveData();
            refreshLayout.setRefreshing(false);
            logger.d(TAG, "loadAndSaveData");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mMessageReceiver, new IntentFilter(Constants.LOCAL_BROADCAST_EVENT_NAME));
        // Bind to LocalService
        Intent intent = new Intent(getContext(), LoadingBindService.class);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(
                mMessageReceiver);
        // Unbind from the service
        if (mBound) {
            getContext().unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOrganizationClickListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String gotText = newText.toLowerCase();
        List<OrganizationUI> organizationUIs = getOrganizations();
        List<OrganizationUI> filteredOrganizationUIs = new ArrayList<>();
        for (int i = 0; i < organizationUIs.size(); i++) {
            String title = organizationUIs.get(i).getName().toLowerCase();
            String region = organizationUIs.get(i).getRegionName().toLowerCase();
            String city = organizationUIs.get(i).getCityName().toLowerCase();

            if (title.contains(gotText))
                filteredOrganizationUIs.add(organizationUIs.get(i));
            else if (region.contains(gotText))
                filteredOrganizationUIs.add(organizationUIs.get(i));
            else if (city.contains(gotText))
                filteredOrganizationUIs.add(organizationUIs.get(i));
        }
        mAdapter.update(filteredOrganizationUIs);
        return true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LoadingBindService.MyBinder binder = (LoadingBindService.MyBinder) service;
            mBound = true;
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
            String message = intent.getStringExtra(Constants.SERVICE_MESSAGE);
            switch (message) {
                case Constants.SERVICE_USER_HAS_INTERNET:
                    textView.setVisibility(View.GONE);
                    setupAdapter();
                    break;
                case Constants.SERVICE_USER_HAS_NOT_INTERNET:
                    textView.setVisibility(View.GONE);
                    setupAdapter();
                    Snackbar.make(relativeLayout, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                    break;
                case Constants.SERVICE_USER_HAS_NOT_CREATED_DB_AND_INTERNET:
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(R.string.no_data);
                    Snackbar.make(relativeLayout, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                    break;
            }
            progressBar.setVisibility(View.GONE);
        }
    };

    private void setupAdapter() {
        organizationsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        mAdapter = new OrganizationsRecyclerViewAdapter(getOrganizations());
        mAdapter.setOnItemClickListener(this);
        organizationsRecyclerView.setAdapter(mAdapter);
    }

    public List<OrganizationUI> getOrganizations() {
        logger.d(TAG, "getOrganizations");
        return StoreData.getListOrganizationsUI();
    }


    @Override
    public void onCallClick(OrganizationUI organization) {
        mOrganizationClickListener.onCallClick(organization.getPhone());
    }

    @Override
    public void onMapClick(OrganizationUI organization) {
        mOrganizationClickListener.onMapClick(organization.getAddress());
    }

    @Override
    public void onLinkClick(OrganizationUI organization) {
        mOrganizationClickListener.onLinkClick(organization.getLink());
    }

    @Override
    public void onDetailClick(OrganizationUI organization) {
        mOrganizationClickListener.onDetailClick(organization);
    }

    public interface OnOrganizationClickListener {

        void onCallClick(String organizationPhone);

        void onMapClick(String organizationAddress);

        void onLinkClick(String organizationLink);

        void onDetailClick(OrganizationUI organization);

    }
}