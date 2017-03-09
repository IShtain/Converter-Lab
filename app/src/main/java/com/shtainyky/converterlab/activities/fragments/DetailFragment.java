package com.shtainyky.converterlab.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.activities.MainActivity;
import com.shtainyky.converterlab.activities.adapter.DetailOfOrganizationRecyclerViewAdapter;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import butterknife.ButterKnife;

public class DetailFragment extends BaseFragment<MainActivity>
        implements View.OnClickListener {
    private static final String TAG = "DetailFragment";
    private static Logger logger;
    public static final String ARG_ORGANIZATION_ID = "organization_id";
    private OnOrganizationClickListener mOrganizationClickListener;
    private OrganizationUI mOrganizationUI;
    private String mOrgID;
    private FloatingActionMenu fabMenu;
    private SwipeRefreshLayout refreshLayout;
    private DetailOfOrganizationRecyclerViewAdapter mAdapter;
    private RecyclerView organizationRecyclerView;

    public static DetailFragment newInstance(String organizationID) {
        Bundle args = new Bundle();
        args.putString(ARG_ORGANIZATION_ID, organizationID);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
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
    protected int getLayoutResId() {
        return R.layout.fragment_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        logger = LogManager.getLogger();
        logger.d(TAG, "onCreate = ");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logger.d(TAG, "onViewCreated = ");
        getBundle();
        showMainInformation(view);
        setupRecyclerView(view);
        setupFabMenu(view);
        refreshLayout = ButterKnife.findById(view, R.id.swipe_refresh_for_currencies);
        swipeRefreshListener(refreshLayout);
    }

    private void swipeRefreshListener(final SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.d(TAG, "onRefresh");
                mOrganizationUI = StoreData.getOrganizationForID(mOrgID);
                setupAdapter();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mOrgID = bundle.getString(ARG_ORGANIZATION_ID);
            mOrganizationUI = getOrganizationUI(mOrgID);
            logger.d(TAG, "savedInstanceState.getString(ARG_ORGANIZATION_ID) = " + mOrgID);
        }
    }

    private void showMainInformation(View view) {
        TextView tvBankName = ButterKnife.findById(view, R.id.tvBankName);
        tvBankName.setText(mOrganizationUI.getName());
        TextView tvRegionName = ButterKnife.findById(view, R.id.tvRegionName);
        tvRegionName.setText(mOrganizationUI.getRegionName());
        TextView tvCityName = ButterKnife.findById(view, R.id.tvCityName);
        tvCityName.setText(mOrganizationUI.getCityName());
        TextView tvPhone = ButterKnife.findById(view, R.id.tvPhone);
        tvPhone.setText(view.getContext().getResources().getString(R.string.bank_phone, mOrganizationUI.getPhone()));
        TextView tvAddress = ButterKnife.findById(view, R.id.tvAddress);
        tvAddress.setText(view.getContext().getResources().getString(R.string.bank_address, mOrganizationUI.getAddress()));
    }

    private void setupRecyclerView(View view) {
        organizationRecyclerView = ButterKnife.findById(view, R.id.recycler_view_organization);
        organizationRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        mAdapter = new DetailOfOrganizationRecyclerViewAdapter(mOrganizationUI.getCurrencies());
        organizationRecyclerView.setAdapter(mAdapter);

    }

    private void setupAdapter() {
        mAdapter = new DetailOfOrganizationRecyclerViewAdapter(mOrganizationUI.getCurrencies());
        organizationRecyclerView.setAdapter(mAdapter);
    }

    private void setupFabMenu(View view) {
        fabMenu = ButterKnife.findById(view, R.id.floating_action_menu);
        fabMenu.setOnClickListener(this);
        FloatingActionButton fabLink = ButterKnife.findById(view, R.id.floating_action_menu_link);
        fabLink.setOnClickListener(this);
        FloatingActionButton fabMap = ButterKnife.findById(view, R.id.floating_action_menu_map);
        fabMap.setOnClickListener(this);
        FloatingActionButton fabCall = ButterKnife.findById(view, R.id.floating_action_menu_phone);
        fabCall.setOnClickListener(this);

    }


    private OrganizationUI getOrganizationUI(String id) {
        return StoreData.getOrganizationForID(id);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_menu_link:
                mOrganizationClickListener.onLinkClick(mOrganizationUI.getLink());
                break;
            case R.id.floating_action_menu_map:
                String address = mOrganizationUI.getAddress() + " " + mOrganizationUI.getCityName() + " " + mOrganizationUI.getRegionName();
                mOrganizationClickListener.onMapClick(address);
                break;
            case R.id.floating_action_menu_phone:
                mOrganizationClickListener.onCallClick(mOrganizationUI.getPhone());
                break;
            case R.id.floating_action_menu:
                break;
        }
    }

}
