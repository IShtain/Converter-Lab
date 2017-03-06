package com.shtainyky.converterlab.activities.db.converter;

import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableDate;
import com.shtainyky.converterlab.activities.db.storeModel.TableOrganization;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.organization.Organization;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConvertData {
    private static Logger mLogger = LogManager.getLogger();
    private static String TAG = "StoreData";
    private static List<TableCurrencyMap> tableCurrencyList = new ArrayList<>();
    private static List<TableCityMap> tableCityMapList = new ArrayList<>();
    private static List<TableRegionMap> tableRegionMapList = new ArrayList<>();
    private static List<TableOrganization> tableOrganizationList = new ArrayList<>();
    private static List<TableCurrenciesList> tableCurrenciesLists = new ArrayList<>();
    private static TableDate tableDate = new TableDate();

    private static Validator mValidator = new Validator();

    public static void convertRootModelForStoring(RootModel rootModel) {
        mLogger.d(TAG, "convertRootModelForStoring");
        convertDate(rootModel.getDate());
        convertCurrencies(rootModel.getCurrencies());
        convertCities(rootModel.getCities());
        convertRegions(rootModel.getRegions());
        convertOrganizations(rootModel.getOrganizations());
    }

    public static void convertDate(String date) {
        tableDate.setId("date");
        tableDate.setDate(date);
    }

    private static void convertCurrencies(List<CurrencyMap> currencyMapList) {
        for (int i = 0; i < currencyMapList.size(); i++) {
            TableCurrencyMap tableCurrencyMap = new TableCurrencyMap();
            CurrencyMap currencyMap = mValidator.validateCurrencyMap(currencyMapList.get(i));
            tableCurrencyMap.setId(currencyMap.getId());
            tableCurrencyMap.setName(currencyMap.getCurrencyTitle());
            tableCurrencyList.add(tableCurrencyMap);
        }
    }

    private static void convertCities(List<CityMap> cityMapList) {
        for (int i = 0; i < cityMapList.size(); i++) {
            TableCityMap tableCityMap = new TableCityMap();
            CityMap cityMap = mValidator.validateCityMap(cityMapList.get(i));
            tableCityMap.setId(cityMap.getId());
            tableCityMap.setName(cityMap.getCityName());
            tableCityMapList.add(tableCityMap);
        }
    }

    private static void convertRegions(List<RegionMap> regionMapList) {
        for (int i = 0; i < regionMapList.size(); i++) {
            TableRegionMap tableRegionMap = new TableRegionMap();
            RegionMap regionMap = mValidator.validateRegionMap(regionMapList.get(i));
            tableRegionMap.setId(regionMap.getId());
            tableRegionMap.setName(regionMap.getRegionName());
            tableRegionMapList.add(tableRegionMap);
        }
    }

    private static void convertOrganizations(List<Organization> organizations) {
        for (int i = 0; i < organizations.size(); i++) {
            Organization organization = mValidator.validateOrganization(organizations.get(i));
            Map<String, Organization.Currency> organizationCurrenciesList = organization.getCurrencies();
            TableOrganization tableOrganization = new TableOrganization();
            tableOrganization.setId(organization.getId());
            tableOrganization.setName(organization.getTitle());
            tableOrganization.setAddress(organization.getAddress());
            tableOrganization.setLink(organization.getLink());
            tableOrganization.setPhone(organization.getPhone());
            tableOrganization.setCityId(organization.getCityId());
            tableOrganization.setRegionId(organization.getRegionId());
            tableOrganization.setCurrenciesListId(organization.getId());
            convertListCurrenciesForOrganization(organization.getId(), organizationCurrenciesList);
            tableOrganizationList.add(tableOrganization);
        }
    }

    private static void convertListCurrenciesForOrganization(String organizationId,
                                                             Map<String, Organization.Currency> currencies) {
        for (String key : currencies.keySet()) {
            TableCurrenciesList currenciesList = new TableCurrenciesList();
            currenciesList.setId(organizationId + key);
            currenciesList.setOrganizationId(organizationId);
            currenciesList.setCurrencyId(key);
            currenciesList.setAsk(currencies.get(key).getAsk());
            currenciesList.setBid(currencies.get(key).getBid());
            tableCurrenciesLists.add(currenciesList);
        }
    }

    public static List<TableCurrencyMap> getTableCurrencyList() {
        return tableCurrencyList;
    }

    public static List<TableCityMap> getTableCityMapList() {
        return tableCityMapList;
    }

    public static List<TableRegionMap> getTableRegionMapList() {
        return tableRegionMapList;
    }

    public static List<TableOrganization> getTableOrganizationList() {
        return tableOrganizationList;
    }

    public static List<TableCurrenciesList> getTableCurrenciesLists() {
        return tableCurrenciesLists;
    }

    public static TableDate getTableDate() {
        return tableDate;
    }
}
