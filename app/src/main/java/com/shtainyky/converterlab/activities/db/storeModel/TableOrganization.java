package com.shtainyky.converterlab.activities.db.storeModel;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ConverterDatabase.class)
public class TableOrganization extends BaseModel {
    @Column
    @PrimaryKey
    private
    String id;

    @Column
    private
    String name;

    @Column
    private
    String phone;

    @Column
    private
    String address;

    @Column
    private
    String link;

    @Column
    private
    String regionId;

    @Column
    private
    String cityId;

    @Column
    private
    String currenciesListId;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCurrenciesListId() {
        return currenciesListId;
    }

    public void setCurrenciesListId(String currenciesListId) {
        this.currenciesListId = currenciesListId;
    }
}