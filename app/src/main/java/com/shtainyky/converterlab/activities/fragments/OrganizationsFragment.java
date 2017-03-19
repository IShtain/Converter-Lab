package com.shtainyky.converterlab.activities.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.shtainyky.converterlab.activities.service.LoadingBindService;
import com.shtainyky.converterlab.activities.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrganizationsFragment extends BaseFragment<MainActivity> implements SearchView.OnQueryTextListener,
        OrganizationsRecyclerViewAdapter.OnItemClickListener, LoadingBindService.OnDataLoadedListener {
    public static final String TAG = "OrganizationsFragment";
    private Logger logger;
    @BindView(R.id.main_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.swipe_refresh_for_currencies)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_no_data)
    TextView textView;
    @BindView(R.id.recycler_view_organizations)
    RecyclerView organizationsRecyclerView;

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
        setupRefreshListener(refreshLayout);
        setupAdapter();
    }

    private void setupAdapter() {
        organizationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new OrganizationsRecyclerViewAdapter();
        mAdapter.setOnItemClickListener(this);
        organizationsRecyclerView.setAdapter(mAdapter);
        List<OrganizationUI> organizationUI = StoreData.getInstance().getListOrganizationsUI();
        setData(organizationUI);
    }

    private void setData(List<OrganizationUI> organizationUIs) {
        logger.d(TAG, "setData" + organizationUIs.size());
        mAdapter.setOrganizationUIList(organizationUIs);
        if (organizationUIs.size() > 0)
            progressBar.setVisibility(View.GONE);
    }

    private void setupRefreshListener(SwipeRefreshLayout refreshLayout) {
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
        // Bind to LocalService
        Intent intent = new Intent(getContext(), LoadingBindService.class);
        intent.putExtra("Bind", true);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        List<OrganizationUI> organizationUIs = StoreData.getInstance().getListOrganizationsUI();
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
        mAdapter.setOrganizationUIList(filteredOrganizationUIs);
        return true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LoadingBindService.MyBinder binder = (LoadingBindService.MyBinder) service;
            mBound = true;
            mService = binder.getService();
            mService.addOnSomeListener(OrganizationsFragment.this);
            mService.loadAndSaveData();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService.removeOnSomeListener(OrganizationsFragment.this);
            mBound = false;
            mService = null;
        }
    };

    private void startAlarmManager() {
        cancelPreviousAlarmManger();
        LoadingBindService.setServiceAlarm(getContext(), true);

    }

    private void cancelPreviousAlarmManger() {
        boolean isOn = LoadingBindService.isServiceAlarmOn(getContext());
        if (isOn)
            LoadingBindService.setServiceAlarm(getContext(), false);
    }

    @Override
    public void onCallClick(OrganizationUI organization) {
        mOrganizationClickListener.onCallClick(organization.getPhone());
    }

    @Override
    public void onMapClick(OrganizationUI organization) {
        String address = organization.getRegionName() +
                " " +
                organization.getCityName() +
                " " +
                organization.getAddress();
        mOrganizationClickListener.onMapClick(address);
    }

    @Override
    public void onLinkClick(OrganizationUI organization) {
        mOrganizationClickListener.onLinkClick(organization.getLink());
    }

    @Override
    public void onDetailClick(OrganizationUI organization) {
        mOrganizationClickListener.onDetailClick(organization);
    }


    @Override
    public void onUpdateDB(List<OrganizationUI> updatedOrganizationUIs) {
        logger.d(TAG, "onUpdateDB");
        setData(updatedOrganizationUIs);
        startAlarmManager();
    }

    @Override
    public void onFailure(String message) {
        switch (message) {
            case Constants.SERVICE_MESSAGE_USER_HAS_NOT_INTERNET:
                logger.d(TAG, "SERVICE_MESSAGE_USER_HAS_NOT_INTERNET");
                Snackbar.make(relativeLayout, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                textView.setVisibility(View.GONE);
                startAlarmManager();
                break;
            case Constants.SERVICE_MESSAGE_USER_HAS_INTERNET:
                logger.d(TAG, "SERVICE_MESSAGE_USER_HAS_INTERNET");
                if (textView != null)
                    textView.setVisibility(View.GONE);
                startAlarmManager();
                break;
            case Constants.SERVICE_MESSAGE_USER_HAS_NOT_CREATED_DB_AND_INTERNET:
                logger.d(TAG, "SERVICE_MESSAGE_USER_HAS_NOT_CREATED_DB_AND_INTERNET");
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
                Snackbar.make(relativeLayout, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                startAlarmManager();
                break;
        }
    }
}
