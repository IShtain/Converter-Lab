package com.shtainyky.converterlab.activities.db.converter;

import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableDate;
import com.shtainyky.converterlab.activities.db.storeModel.TableOrganization;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap;
import com.shtainyky.converterlab.activities.util.logger.LogManager;
import com.shtainyky.converterlab.activities.util.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.organization.Organization;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConvertData {
    private static Logger mLogger = LogManager.getLogger();
    private static String TAG = "ConvertData";
    private static Validator mValidator = new Validator();

    public static TableDate convertDate(String date) {
        TableDate tableDate = new TableDate();
        tableDate.setId("date");
        tableDate.setDate(date);
        return tableDate;
    }

    public static List<TableCurrencyMap> convertCurrencies(List<CurrencyMap> currencyMapList) {
        List<TableCurrencyMap> tableCurrencyList = new ArrayList<>();
        for (int i = 0; i < currencyMapList.size(); i++) {
            TableCurrencyMap tableCurrencyMap = new TableCurrencyMap();
            CurrencyMap currencyMap = mValidator.validateCurrencyMap(currencyMapList.get(i));
            tableCurrencyMap.setId(currencyMap.getId());
            tableCurrencyMap.setName(currencyMap.getCurrencyTitle());
            tableCurrencyList.add(tableCurrencyMap);
        }
        return tableCurrencyList;
    }

    public static List<TableCityMap> convertCities(List<CityMap> cityMapList) {
        List<TableCityMap> tableCityMapList = new ArrayList<>();
        for (int i = 0; i < cityMapList.size(); i++) {
            TableCityMap tableCityMap = new TableCityMap();
            CityMap cityMap = mValidator.validateCityMap(cityMapList.get(i));
            tableCityMap.setId(cityMap.getId());
            tableCityMap.setName(cityMap.getCityName());
            tableCityMapList.add(tableCityMap);
        }
        return tableCityMapList;
    }

    public static List<TableRegionMap> convertRegions(List<RegionMap> regionMapList) {
        List<TableRegionMap> tableRegionMapList = new ArrayList<>();
        for (int i = 0; i < regionMapList.size(); i++) {
            TableRegionMap tableRegionMap = new TableRegionMap();
            RegionMap regionMap = mValidator.validateRegionMap(regionMapList.get(i));
            tableRegionMap.setId(regionMap.getId());
            tableRegionMap.setName(regionMap.getRegionName());
            tableRegionMapList.add(tableRegionMap);
        }
        return tableRegionMapList;
    }

    public static List<TableOrganization> convertOrganizations(List<Organization> organizations) {
        List<TableOrganization> tableOrganizationList = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            Organization organization = mValidator.validateOrganization(organizations.get(i));
            TableOrganization tableOrganization = new TableOrganization();
            tableOrganization.setId(organization.getId());
            tableOrganization.setName(organization.getTitle());
            tableOrganization.setAddress(organization.getAddress());
            tableOrganization.setLink(organization.getLink());
            tableOrganization.setPhone(organization.getPhone());
            tableOrganization.setCityId(organization.getCityId());
            tableOrganization.setRegionId(organization.getRegionId());
            tableOrganization.setCurrenciesListId(organization.getId());
            tableOrganizationList.add(tableOrganization);
        }
        return tableOrganizationList;
    }

    public static List<TableCurrenciesList> convertListCurrenciesForOrganization(String organizationId,
                                                             Map<String, Organization.Currency> currencies) {
        List<TableCurrenciesList> tableCurrenciesLists = new ArrayList<>();
        for (String key : currencies.keySet()) {
            TableCurrenciesList currenciesList = new TableCurrenciesList();
            currenciesList.setId(organizationId + key);
            currenciesList.setOrganizationId(organizationId);
            currenciesList.setCurrencyId(key);
            currenciesList.setAsk(currencies.get(key).getAsk());
            currenciesList.setBid(currencies.get(key).getBid());
            tableCurrenciesLists.add(currenciesList);
        }
        return tableCurrenciesLists;
    }

}
