package com.shtainyky.converterlab.db.converter;

import com.shtainyky.converterlab.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.models.modelRetrofit.organization.Organization;
import com.shtainyky.converterlab.models.modelRetrofit.region.RegionMap;

class Validator {
    private static Validator sValidator;

    private Validator() {
    }

    public static Validator getInstance() {
        if (sValidator == null) {
            sValidator = new Validator();
        } else {
            return sValidator;
        }
        return sValidator;
    }

    CurrencyMap validateCurrencyMap(CurrencyMap currencyMap) {
        if (currencyMap.getCurrencyTitle() == null) currencyMap.setCurrencyTitle("Нет данных");
        return currencyMap;
    }

    CityMap validateCityMap(CityMap cityMap) {
        if (cityMap.getCityName() == null) cityMap.setCityName("Нет данных");
        return cityMap;
    }

    RegionMap validateRegionMap(RegionMap regionMap) {
        if (regionMap.getRegionName() == null) regionMap.setRegionName("Нет данных");
        return regionMap;
    }

    Organization validateOrganization(Organization organization) {
        if (organization.getTitle() == null) organization.setTitle("Нет данных");
        if (organization.getAddress() == null || organization.getAddress().equals(" "))
            organization.setAddress("Нет данных");
        if (organization.getCityId() == null) organization.setCityId("Нет данных");
        if (organization.getRegionId() == null) organization.setRegionId("Нет данных");
        if (organization.getPhone() == null) organization.setPhone("Нет данных");
        if (organization.getLink() == null) organization.setLink("Нет данных");
        return organization;
    }
}
