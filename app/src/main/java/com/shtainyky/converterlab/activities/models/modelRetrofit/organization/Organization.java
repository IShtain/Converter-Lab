package com.shtainyky.converterlab.activities.models.modelRetrofit.organization;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Organization {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("regionId")
    private String regionId;

    @SerializedName("cityId")
    private String cityId;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("link")
    private String link;

    @SerializedName("currencies")
    private Map<String, Currency> currencies;

    public class Currency {
        @SerializedName("ask")
        private double ask;
        @SerializedName("bid")
        private double bid;

        public double getAsk() {
            return ask;
        }

        public double getBid() {
            return bid;
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getLink() {
        return link;
    }

    public Map<String, Currency> getCurrencies() {
        return currencies;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCurrencies(Map<String, Currency> currencies) {
        this.currencies = currencies;
    }
}
