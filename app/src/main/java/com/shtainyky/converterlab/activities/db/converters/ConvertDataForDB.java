package com.shtainyky.converterlab.activities.db.converters;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableOrganization;
import com.shtainyky.converterlab.activities.db.storeModel.TableOrganization_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap_Table;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.organization.Organization;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;

import java.util.List;
import java.util.Map;

public class ConvertDataForDB {

    public static void insertCurrencyMap(List<CurrencyMap> currencyMapList) {
        for (int i = 0; i < currencyMapList.size(); i++) {
            SQLite.insert(TableCurrencyMap.class)
                    .columns(TableCurrencyMap_Table.id, TableCurrencyMap_Table.name)
                    .values(currencyMapList.get(i).getId(), currencyMapList.get(i).getCurrencyTitle())
                    .execute();
        }
    }

    public static void insertCityMap(List<CityMap> cityMapList) {
        for (int i = 0; i < cityMapList.size(); i++) {
            SQLite.insert(TableCityMap.class)
                    .columns(TableCityMap_Table.id, TableCityMap_Table.name)
                    .values(cityMapList.get(i).getId(), cityMapList.get(i).getCityName())
                    .execute();
        }
    }

    public static void insertRegionMap(List<RegionMap> regionMapList) {
        for (int i = 0; i < regionMapList.size(); i++) {
            SQLite.insert(TableRegionMap.class)
                    .columns(TableRegionMap_Table.id, TableRegionMap_Table.name)
                    .values(regionMapList.get(i).getId(), regionMapList.get(i).getRegionName())
                    .execute();

        }
    }

    public static void insertOrganization(List<Organization> organizations) {
        for (int i = 0; i < organizations.size(); i++) {
            Organization organization = organizations.get(i);
            Map<String, Organization.Currency> organizationCurrenciesList = organization.getCurrencies();

            SQLite.insert(TableOrganization.class)
                    .columnValues(TableOrganization_Table.id.eq(organization.getId()),
                            TableOrganization_Table.name.eq(organization.getTitle()),
                            TableOrganization_Table.address.eq(organization.getAddress()),
                            TableOrganization_Table.link.eq(organization.getLink()),
                            TableOrganization_Table.phone.eq(organization.getPhone()),
                            TableOrganization_Table.cityId.eq(organization.getCityId()),
                            TableOrganization_Table.regionId.eq(organization.getRegionId()),
                            TableOrganization_Table.currenciesListId.eq(organization.getId()))
                    .execute();
            insertCurrenciesForOrganization(organization.getId(), organizationCurrenciesList);
        }
    }

    private static void insertCurrenciesForOrganization(String organizationId,
                                                        Map<String, Organization.Currency> currencies) {
//        int count = SQLite.select(TableCurrenciesList_Table.id)
//                .from(TableCurrenciesList.class)
//                .where(TableCurrenciesList_Table.id.is(1))
//                .count();


        for (String key : currencies.keySet()) {
            TableCurrenciesList currenciesList = new TableCurrenciesList();
            currenciesList.setId(organizationId + key);
            currenciesList.setOrganizationId(organizationId);
            currenciesList.setCurrencyId(key);
            currenciesList.setAsk(currencies.get(key).getAsk());
            currenciesList.setBid(currencies.get(key).getBid());
            currenciesList.save();
        }

    }
}

