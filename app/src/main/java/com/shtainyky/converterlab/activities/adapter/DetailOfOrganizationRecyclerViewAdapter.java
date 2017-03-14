package com.shtainyky.converterlab.activities.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.widgets.CustomImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailOfOrganizationRecyclerViewAdapter extends
        RecyclerView.Adapter<DetailOfOrganizationRecyclerViewAdapter.DetailOfOrganizationRecyclerViewHolder> {

    private List<OrganizationUI.CurrencyUI> mCurrencyUIList;

    public DetailOfOrganizationRecyclerViewAdapter() {
        mCurrencyUIList = new ArrayList<>();
    }

    public void setOrganizationUIList(List<OrganizationUI.CurrencyUI> currencyUIList) {
        mCurrencyUIList.clear();
        mCurrencyUIList.addAll(currencyUIList);
        notifyDataSetChanged();
    }

    @Override
    public DetailOfOrganizationRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_organization_detail, parent, false);
        return new DetailOfOrganizationRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailOfOrganizationRecyclerViewHolder holder, int position) {
        holder.bindCurrency(mCurrencyUIList.get(position));

    }

    @Override
    public int getItemCount() {
        return mCurrencyUIList == null ? 0 : mCurrencyUIList.size();
    }

    static class DetailOfOrganizationRecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCurrencyName)
        TextView tvCurrencyName;
        @BindView(R.id.tvAsk)
        TextView tvAsk;
        @BindView(R.id.tvBid)
        TextView tvBid;
        @BindView(R.id.ivAsk)
        CustomImageView ivAsk;
        @BindView(R.id.ivBid)
        CustomImageView ivBid;


        DetailOfOrganizationRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindCurrency(OrganizationUI.CurrencyUI currencyUI) {
            DecimalFormat format = new DecimalFormat("0.0000");
            tvCurrencyName.setText(currencyUI.getName());
            tvAsk.setText(format.format(currencyUI.getAsk()));
            tvBid.setText(format.format(currencyUI.getAsk()));
            ivAsk.setIncrease(currencyUI.getDiffAsk() >= 0);
            ivBid.setIncrease(currencyUI.getDiffBid() >= 0);
        }
    }
}

