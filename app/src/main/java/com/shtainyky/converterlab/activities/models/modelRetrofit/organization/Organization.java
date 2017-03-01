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

    private class Currency {
        @SerializedName("ask")
        private double ask;
        @SerializedName("bid")
        private double bid;
    }


}
