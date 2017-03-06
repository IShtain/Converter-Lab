package com.shtainyky.converterlab.activities.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.activities.MainActivity;
import com.shtainyky.converterlab.activities.adapter.OrganizationsRecyclerViewAdapter;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;

import butterknife.ButterKnife;

public class OrganizationsFragment extends BaseFragment<MainActivity> implements SearchView.OnQueryTextListener {
    public static final String TAG = "OrganizationsFragment";
    private RecyclerView organizationsRecyclerView;
    private Logger logger;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger = LogManager.getLogger();
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        organizationsRecyclerView = ButterKnife.findById(view, R.id.recycler_view_organizations);
        SwipeRefreshLayout refreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_for_organizations);

        swipeRefreshListener(refreshLayout);
        setupAdapter();
    }

    private void swipeRefreshListener(final SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.d(TAG, "onRefresh");
                getActivityGeneric().refreshDatabase();
                refreshLayout.setRefreshing(false);
            }
        });
    }


    private void setupAdapter() {
        organizationsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        OrganizationsRecyclerViewAdapter adapter = new OrganizationsRecyclerViewAdapter(getActivityGeneric().getOrganizations());
        organizationsRecyclerView.setAdapter(adapter);

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
        return false;
    }
}
