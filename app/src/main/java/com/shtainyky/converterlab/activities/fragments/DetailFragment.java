package com.shtainyky.converterlab.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.activities.MainActivity;
import com.shtainyky.converterlab.activities.adapter.DetailOfOrganizationRecyclerViewAdapter;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.widgets.CustomDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailFragment extends BaseFragment<MainActivity>{

    private static final String TAG = "DetailFragment";
    private static Logger logger;

    public static final String ARG_ORGANIZATION_ID = "organization_id";
    private OnOrganizationClickListener mOrganizationClickListener;
    private OrganizationUI mOrganizationUI;
    private String mOrgID;
    private DetailOfOrganizationRecyclerViewAdapter mAdapter;
    private boolean mIsFabOpen = false;
    private Animation mAminFabOpen, mAnimFabClose;

    @BindView(R.id.fab_menu)
    FloatingActionButton fabMenu;
    @BindView(R.id.fab_link)
    FloatingActionButton fabLink;
    @BindView(R.id.fab_map)
    FloatingActionButton fabMap;
    @BindView(R.id.fab_phone)
    FloatingActionButton fabCall;
    @BindView(R.id.fab_link_LinearLayout)
    LinearLayout fabLinkLinearLayout;
    @BindView(R.id.fab_map_LinearLayout)
    LinearLayout fabMapLinearLayout;
    @BindView(R.id.fab_call_LinearLayout)
    LinearLayout fabCallLinearLayout;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvRegionName)
    TextView tvRegionName;
    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.swipe_refresh_for_currencies)
    SwipeRefreshLayout refreshLayout;


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
                    + " must implement OnOrganizationClickListener");
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
        ActionBar ab = getActivityGeneric().getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
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
        setupFabMenu();
        swipeRefreshListener(refreshLayout);
    }

    private void swipeRefreshListener(final SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.d(TAG, "onRefresh");
                mOrganizationUI = StoreData.getOrganizationForID(mOrgID);
                setupData();
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
        tvBankName.setText(mOrganizationUI.getName());
        tvRegionName.setText(mOrganizationUI.getRegionName());
        tvCityName.setText(mOrganizationUI.getCityName());
        tvPhone.setText(view.getContext().getResources().getString(R.string.bank_phone, mOrganizationUI.getPhone()));
        tvAddress.setText(view.getContext().getResources().getString(R.string.bank_address, mOrganizationUI.getAddress()));
    }

    private void setupRecyclerView(View view) {
        RecyclerView organizationRecyclerView = ButterKnife.findById(view, R.id.recycler_view_organization);
        organizationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DetailOfOrganizationRecyclerViewAdapter();
        organizationRecyclerView.setAdapter(mAdapter);
        organizationRecyclerView.addItemDecoration(new CustomDividerItemDecoration(getContext()));
        setupData();
    }

    private void setupData() {
        mAdapter.setOrganizationUIList(mOrganizationUI.getCurrencies());
        logger.d(TAG, "setupData size = " + mOrganizationUI.getCurrencies().size());
    }

    private void setupFabMenu() {
        mAminFabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        mAnimFabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

    }

    @OnClick({R.id.fab_link, R.id.fab_map, R.id.fab_phone, R.id.fab_menu})
    public void onFabClick(View v) {
        switch (v.getId()) {
            case R.id.fab_link:
                mOrganizationClickListener.onLinkClick(mOrganizationUI.getLink());
                break;
            case R.id.fab_map:
                String address = mOrganizationUI.getAddress() + " " + mOrganizationUI.getCityName() + " " + mOrganizationUI.getRegionName();
                mOrganizationClickListener.onMapClick(address);
                break;
            case R.id.fab_phone:
                mOrganizationClickListener.onCallClick(mOrganizationUI.getPhone());
                break;
            case R.id.fab_menu:
                animateFAB();
                break;
        }
    }

    private void animateFAB() {
        if (mIsFabOpen) {
            fabCall.setClickable(false);
            fabMap.setClickable(false);
            fabLink.setClickable(false);
            mIsFabOpen = false;
            fabMenu.setImageResource(R.drawable.ic_action_menu);
            fabLinkLinearLayout.startAnimation(mAnimFabClose);
            fabMapLinearLayout.startAnimation(mAnimFabClose);
            fabCallLinearLayout.startAnimation(mAnimFabClose);
            mScrollView.setAlpha(1f);
            logger.d(TAG, "close");
        } else {
            fabMenu.setImageResource(R.drawable.ic_action_close);
            fabCall.setClickable(true);
            fabMap.setClickable(true);
            fabLink.setClickable(true);
            mIsFabOpen = true;
            fabLinkLinearLayout.startAnimation(mAminFabOpen);
            fabMapLinearLayout.startAnimation(mAminFabOpen);
            fabCallLinearLayout.startAnimation(mAminFabOpen);
            mScrollView.setAlpha(0.15f);
            logger.d("Raj", "open");
        }
    }


    private OrganizationUI getOrganizationUI(String id) {
        return StoreData.getOrganizationForID(id);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mOrganizationClickListener.onShareClick(mOrganizationUI);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivityGeneric().getSupportFragmentManager().popBackStack();
                ActionBar ab = getActivityGeneric().getSupportActionBar();
                if (ab != null) {
                    ab.setDisplayHomeAsUpEnabled(false);
                    ab.setTitle(R.string.app_name);
                    ab.setSubtitle("");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




