package com.shtainyky.converterlab.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.models.modelUI.OrganizationUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    static class OrganizationsRecyclerViewHolder extends RecyclerView.ViewHolder {
        private OrganizationUI mOrganizationUI;
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

        @BindView(R.id.ibLink)
        ImageButton ibLink;
        @BindView(R.id.ibMap)
        ImageButton ibMap;
        @BindView(R.id.ibPhone)
        ImageButton ibPhone;
        @BindView(R.id.ibDetail)
        ImageButton ibDetail;

        private OnItemClickListener mOnItemClickListener;

        OrganizationsRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

        @OnClick({R.id.ibLink, R.id.ibMap, R.id.ibPhone, R.id.ibDetail})
        void onOrganizationCardClick(View v) {
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
