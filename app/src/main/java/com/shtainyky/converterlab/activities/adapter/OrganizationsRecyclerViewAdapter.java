package com.shtainyky.converterlab.activities.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class OrganizationsRecyclerViewAdapter extends RecyclerView.Adapter<OrganizationsRecyclerViewAdapter.OrganizationsRecyclerViewHolder> {
    private List<OrganizationUI> mOrganizationUIList;

    private OnItemClickListener mOnItemClickListener;

    public OrganizationsRecyclerViewAdapter() {
        mOrganizationUIList = new ArrayList<>();
    }

    public void setOrganizationUIList(List<OrganizationUI> organizationUIList) {
        mOrganizationUIList.clear();
        mOrganizationUIList.addAll(organizationUIList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public OrganizationsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_organization_information, parent, false);
        return new OrganizationsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrganizationsRecyclerViewHolder holder, int position) {
        holder.bindOrganization(mOrganizationUIList.get(position), mOnItemClickListener);

    }

    @Override
    public int getItemCount() {
        return mOrganizationUIList != null ? mOrganizationUIList.size() : 0;
    }

    public interface OnItemClickListener {

        void onCallClick(OrganizationUI organization);

        void onMapClick(OrganizationUI organization);

        void onLinkClick(OrganizationUI organization);

        void onDetailClick(OrganizationUI organization);
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

        OnItemClickListener mOnItemClickListener;

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

        void bindOrganization(OrganizationUI organizationUI, OnItemClickListener onItemClickListener) {
            mOrganizationUI = organizationUI;
            tvBankName.setText(organizationUI.getName());
            tvRegionName.setText(organizationUI.getRegionName());
            tvCityName.setText(organizationUI.getCityName());
            tvPhone.setText(itemView.getContext().getResources().getString(R.string.bank_phone, organizationUI.getPhone()));
            tvAddress.setText(itemView.getContext().getResources().getString(R.string.bank_address, organizationUI.getAddress()));
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibLink:
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onLinkClick(mOrganizationUI);
                    break;
                case R.id.ibMap:
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onMapClick(mOrganizationUI);
                    break;
                case R.id.ibPhone:
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onCallClick(mOrganizationUI);
                    break;
                case R.id.ibDetail:
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onDetailClick(mOrganizationUI);
                    break;
            }
        }
    }



}
