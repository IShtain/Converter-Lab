package com.shtainyky.converterlab.activities.models.modelRetrofit;

import com.google.gson.annotations.SerializedName;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.org_type.OrgTypeMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.organization.OrganizationUI;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;

import java.util.List;

public class RootModel {

    @SerializedName("sourceId")
    private String sourceId;

    @SerializedName("date")
    private String date;

    @SerializedName("organizations")
    private
    List<OrganizationUI> organizations;

    @SerializedName("orgTypes")
    private
    List<OrgTypeMap> orgTypes;

    @SerializedName("currencies")
    private
    List<CurrencyMap> currencies;

    @SerializedName("regions")
    private
    List<RegionMap> regions;

    @SerializedName("cities")
    private
    List<CityMap> cities;

    public String getDate() {
        return date;
    }

    public String getSourceId() {
        return sourceId;
    }

    public List<OrganizationUI> getOrganizations() {
        return organizations;
    }

    public List<OrgTypeMap> getOrgTypes() {
        return orgTypes;
    }

    public List<CurrencyMap> getCurrencies() {
        return currencies;
    }

    public List<RegionMap> getRegions() {
        return regions;
    }

    public List<CityMap> getCities() {
        return cities;
    }
}
