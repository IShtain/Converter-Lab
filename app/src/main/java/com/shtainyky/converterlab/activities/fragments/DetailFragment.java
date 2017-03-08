package com.shtainyky.converterlab.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.activities.MainActivity;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

public class DetailFragment extends BaseFragment<MainActivity>
        //  implements OnItemClickListener
{
    private static final String ARG_ORGANIZATION_ID = "organization_id";
    private OnOrganizationClickListener mOrganizationClickListener;

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
    }
    private OrganizationUI mOrganizationUI(String id) {
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
