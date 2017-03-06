package com.shtainyky.converterlab.activities.adapter;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import java.util.List;

import butterknife.ButterKnife;

public class OrganizationsRecyclerViewAdapter extends RecyclerView.Adapter<OrganizationsRecyclerViewAdapter.OrganizationsRecyclerViewHolder> {
    private List<OrganizationUI> mOrganizationUIList;


    public OrganizationsRecyclerViewAdapter(List<OrganizationUI> organizationUIList) {
        mOrganizationUIList = organizationUIList;
    }

    @Override
    public OrganizationsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_bank_information, parent, false);
        return new OrganizationsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrganizationsRecyclerViewHolder holder, int position) {
        holder.bindOrganization(mOrganizationUIList.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrganizationUIList != null ? mOrganizationUIList.size() : 0;
    }

    static class OrganizationsRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OrganizationUI mOrganizationUI;

        TextView tvBankName;
        TextView tvRegionName;
        TextView tvCityName;
        TextView tvPhone;
        TextView tvAddress;

        ImageButton ibLink;
        ImageButton ibMap;
        ImageButton ibPhone;
        ImageButton ibDetail;

        OrganizationsRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvBankName = ButterKnife.findById(itemView, R.id.tvBankName_RV);
            tvRegionName = ButterKnife.findById(itemView, R.id.tvRegionName);
            tvCityName = ButterKnife.findById(itemView, R.id.tvCityName);
            tvPhone = ButterKnife.findById(itemView, R.id.tvPhone);
            tvAddress = ButterKnife.findById(itemView, R.id.tvAddress);

            ibLink = ButterKnife.findById(itemView, R.id.ibLink);
            ibLink.setOnClickListener(this);
            ibMap = ButterKnife.findById(itemView, R.id.ibMap);
            ibMap.setOnClickListener(this);
            ibPhone = ButterKnife.findById(itemView, R.id.ibPhone);
            ibPhone.setOnClickListener(this);
            ibDetail = ButterKnife.findById(itemView, R.id.ibDetail);
            ibDetail.setOnClickListener(this);
        }

        void bindOrganization(OrganizationUI organizationUI) {
            mOrganizationUI = organizationUI;
            tvBankName.setText(organizationUI.getName());
            tvRegionName.setText(organizationUI.getRegionName());
            tvCityName.setText(organizationUI.getCityName());
            tvPhone.setText(itemView.getContext().getResources().getString(R.string.bank_phone, organizationUI.getPhone()));
            tvAddress.setText(itemView.getContext().getResources().getString(R.string.bank_address, organizationUI.getAddress()));

        }


        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.ibLink:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mOrganizationUI.getLink()));
                    v.getContext().startActivity(intent);
                    break;
                case R.id.ibMap:
                    Toast.makeText(itemView.getContext(), "ibMap", Toast.LENGTH_LONG).show();
                    break;
                case R.id.ibPhone:
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mOrganizationUI.getPhone()));
                    v.getContext().startActivity(intent);
                    break;
                case R.id.ibDetail:
                    onDetailClickListener(mOrganizationUI);
                    break;
            }

        }

        @NonNull
        private View.OnClickListener onDetailClickListener(final OrganizationUI organizationUI) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "onDetailClickListener", Toast.LENGTH_SHORT).show();
                }
            };
        }

    }
}
