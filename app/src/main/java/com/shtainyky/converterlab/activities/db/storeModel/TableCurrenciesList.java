package com.shtainyky.converterlab.activities.db.storeModel;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ConverterDatabase.class)
public class TableCurrenciesList extends BaseModel {
    @Column
    @PrimaryKey()
    private
    String id;

    @Column
    private
    String organizationId;

    @Column
    private
    String currencyId;

    @Column
    private
    double ask;

    @Column
    private
    double diffAsk;

    @Column
    private
    double bid;

    @Column
    private
    double diffBid;

    public String getId() {
        return id;
    }

    public double getDiffAsk() {
        return diffAsk;
    }

    public void setDiffAsk(double diffAsk) {
        this.diffAsk = diffAsk;
    }

    public double getDiffBid() {
        return diffBid;
    }

    public void setDiffBid(double diffBid) {
        this.diffBid = diffBid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }
}