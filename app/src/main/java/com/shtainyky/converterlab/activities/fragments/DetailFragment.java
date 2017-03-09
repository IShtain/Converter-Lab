package com.shtainyky.converterlab.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.activities.MainActivity;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import butterknife.ButterKnife;

public class DetailFragment extends BaseFragment<MainActivity>
        //  implements OnItemClickListener
{
    private static final String TAG = "DetailFragment";
    private static Logger logger ;
    public static final String ARG_ORGANIZATION_ID = "organization_id";
    private OnOrganizationClickListener mOrganizationClickListener;
    private OrganizationUI mOrganizationUI;
    private RecyclerView organizationRecyclerView;

    TextView tvBankName;
    TextView tvRegionName;
    TextView tvCityName;
    TextView tvPhone;
    TextView tvAddress;

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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mOrganizationUI = getOrganizationUI(bundle.getString(ARG_ORGANIZATION_ID));
            logger.d(TAG, "savedInstanceState.getString(ARG_ORGANIZATION_ID) = " + bundle.getString(ARG_ORGANIZATION_ID));
        }

        organizationRecyclerView = ButterKnife.findById(view, R.id.recycler_view_organization);

        tvBankName = ButterKnife.findById(view, R.id.tvBankName);
        tvBankName.setText(mOrganizationUI.getName());
        tvRegionName = ButterKnife.findById(view, R.id.tvRegionName);
        tvRegionName.setText(mOrganizationUI.getRegionName());
        tvCityName = ButterKnife.findById(view, R.id.tvCityName);
        tvCityName.setText(mOrganizationUI.getCityName());
        tvPhone = ButterKnife.findById(view, R.id.tvPhone);
        tvPhone.setText(view.getContext().getResources().getString(R.string.bank_phone, mOrganizationUI.getPhone()));
        tvAddress = ButterKnife.findById(view, R.id.tvAddress);
        tvAddress.setText(view.getContext().getResources().getString(R.string.bank_address, mOrganizationUI.getAddress()));
    }


    private OrganizationUI getOrganizationUI(String id) {
        return StoreData.getOrganizationForID(id);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }
//
//    @Override
//    public void onCallClick(OrganizationUI organization) {
//        mOrganizationClickListener.onCallClick(organization.getPhone());
//    }
//
//    @Override
//    public void onMapClick(OrganizationUI organization) {
//        mOrganizationClickListener.onMapClick(organization);
//    }
//
//    @Override
//    public void onLinkClick(OrganizationUI organization) {
//        mOrganizationClickListener.onLinkClick(organization.getLink());
//    }
//
//    @Override
//    public void onDetailClick(OrganizationUI organization) {
//
//    }
}
