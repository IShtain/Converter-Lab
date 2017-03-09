package com.shtainyky.converterlab.activities.adapter;


import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;

public class DetailOfOrganizationRecyclerViewAdapter extends
        RecyclerView.Adapter<DetailOfOrganizationRecyclerViewAdapter.DetailOfOrganizationRecyclerViewHolder> {
    private List<OrganizationUI.CurrencyUI> mCurrencyUIList;

    public DetailOfOrganizationRecyclerViewAdapter(List<OrganizationUI.CurrencyUI> currencyUIList) {
        mCurrencyUIList = currencyUIList;
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
        TextView tvCurrencyName;
        TextView tvAsk;
        TextView tvBid;

        ImageButton ivAsk;
        ImageButton ivBid;

        public DetailOfOrganizationRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvCurrencyName = ButterKnife.findById(itemView, R.id.tvCurrencyName);
            tvAsk = ButterKnife.findById(itemView, R.id.tvAsk);
            tvBid = ButterKnife.findById(itemView, R.id.tvBid);

            ivAsk = ButterKnife.findById(itemView, R.id.ivAsk);
            ivBid = ButterKnife.findById(itemView, R.id.ivBid);

        }

        public void bindCurrency(OrganizationUI.CurrencyUI currencyUI) {
            DecimalFormat format = new DecimalFormat("0.0000");
            tvCurrencyName.setText(currencyUI.getName());
            tvAsk.setText(format.format(currencyUI.getAsk()));
            tvBid.setText(format.format(currencyUI.getAsk()));
        }
    }
}

