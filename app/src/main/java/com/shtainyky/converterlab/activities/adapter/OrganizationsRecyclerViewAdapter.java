package com.shtainyky.converterlab.activities.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import java.util.List;

import butterknife.BindView;
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

    public static class OrganizationsRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvBankName_RV)
        TextView tvBankName;

        @BindView(R.id.tvRegionName)
        TextView tvRegionName;

        @BindView(R.id.tvCityName)
        TextView tvCityName;

        @BindView(R.id.tvPhone)
        TextView tvPhone;

        @BindView(R.id.tvAddress)
        TextView tvAddress;

        public OrganizationsRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvBankName = ButterKnife.findById(itemView, R.id.tvBankName_RV);
            tvRegionName = ButterKnife.findById(itemView, R.id.tvRegionName);
            tvCityName = ButterKnife.findById(itemView, R.id.tvCityName);
            tvPhone = ButterKnife.findById(itemView, R.id.tvPhone);
            tvAddress = ButterKnife.findById(itemView, R.id.tvAddress);
        }

        void bindOrganization(OrganizationUI organizationUI) {
            tvBankName.setText(organizationUI.getName());
            tvRegionName.setText(organizationUI.getRegionName());
            tvCityName.setText(organizationUI.getCityName());
            tvPhone.setText(itemView.getContext().getResources().getString(R.string.bank_phone, organizationUI.getPhone()));
            tvAddress.setText(itemView.getContext().getResources().getString(R.string.bank_address, organizationUI.getAddress()));
        }


    }
}
