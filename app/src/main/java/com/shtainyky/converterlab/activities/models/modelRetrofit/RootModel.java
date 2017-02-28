package com.shtainyky.converterlab.activities.models.modelRetrofit;

import com.google.gson.annotations.SerializedName;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.org_type.OrgTypeMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.organization.Organization;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;

import java.util.List;

public class RootModel {

    @SerializedName("sourceId")
    private String sourceId;

    @SerializedName("date")
    private String date;

    @SerializedName("organizations")
    List<Organization> organizations;

    @SerializedName("orgTypes")
    List<OrgTypeMap> orgTypes;

    @SerializedName("currencies")
    List<CurrencyMap> currencies;

    @SerializedName("regions")
    List<RegionMap> regions;

    @SerializedName("cities")
    List<CityMap> cities;


}
