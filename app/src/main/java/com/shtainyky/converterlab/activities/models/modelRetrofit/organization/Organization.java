package com.shtainyky.converterlab.activities.models.modelRetrofit.organization;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Organization {
    @SerializedName("id")
    private String id;

    @SerializedName("orgType")
    private int orgType;

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

    public int getOrgType() {
        return orgType;
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
}
