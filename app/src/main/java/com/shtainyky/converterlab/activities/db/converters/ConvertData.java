package com.shtainyky.converterlab.activities.db.converters;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.shtainyky.converterlab.activities.db.storeModel.ConverterDatabase;
import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableOrganization;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap_Table;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.organization.Organization;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConvertData {

    private static Logger mLogger = LogManager.getLogger();
    private static String TAG = "ConvertData";

    public static void insertCurrencyMap(List<CurrencyMap> currencyMapList) {
        List<TableCurrencyMap> tableCurrencyList = new ArrayList<>();
        for (int i = 0; i < currencyMapList.size(); i++) {
            TableCurrencyMap tableCurrencyMap = new TableCurrencyMap();
            tableCurrencyMap.setId(currencyMapList.get(i).getId());
            tableCurrencyMap.setName(currencyMapList.get(i).getCurrencyTitle());
            tableCurrencyList.add(tableCurrencyMap);
        }
        saveAllCurrenciesMap(tableCurrencyList);
        mLogger.d(TAG, "insertCurrencyMap");
    }

    private static void saveAllCurrenciesMap(List<TableCurrencyMap> tableCurrencyList) {
        FlowManager.getDatabase(ConverterDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<TableCurrencyMap>() {
                            @Override
                            public void processModel(TableCurrencyMap currency) {
                                currency.save();
                            }
                        }).addAll(tableCurrencyList).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllCurrenciesMap");
    }

    public static void insertCityMap(List<CityMap> cityMapList) {
        List<TableCityMap> tableCityMapList = new ArrayList<>();
        for (int i = 0; i < cityMapList.size(); i++) {
            TableCityMap tableCityMap = new TableCityMap();
            tableCityMap.setId(cityMapList.get(i).getId());
            tableCityMap.setName(cityMapList.get(i).getCityName());
            tableCityMapList.add(tableCityMap);
        }
        saveAllCitiesMap(tableCityMapList);
        mLogger.d(TAG, "insertCityMap");
    }

    private static void saveAllCitiesMap(List<TableCityMap> tableCityList) {
        FlowManager.getDatabase(ConverterDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<TableCityMap>() {
                            @Override
                            public void processModel(TableCityMap city) {
                                city.save();
                            }
                        }).addAll(tableCityList).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllCitiesMap");
    }

    public static void insertRegionMap(List<RegionMap> regionMapList) {
        List<TableRegionMap> tableRegionMapList = new ArrayList<>();
        for (int i = 0; i < regionMapList.size(); i++) {
            TableRegionMap tableRegionMap = new TableRegionMap();
            tableRegionMap.setId(regionMapList.get(i).getId());
            tableRegionMap.setName(regionMapList.get(i).getRegionName());
            tableRegionMapList.add(tableRegionMap);
        }
        saveAllRegions(tableRegionMapList);
        mLogger.d(TAG, "insertRegionMap");
    }

    private static void saveAllRegions(List<TableRegionMap> tableRegionMapList) {
        FlowManager.getDatabase(ConverterDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<TableRegionMap>() {
                            @Override
                            public void processModel(TableRegionMap region) {
                                region.save();
                            }
                        }).addAll(tableRegionMapList).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllRegions");
    }

    public static void insertOrganization(List<Organization> organizations) {
        List<TableOrganization> tableOrganizationList = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            Organization organization = organizations.get(i);
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
            insertCurrenciesForOrganization(organization.getId(), organizationCurrenciesList);
            tableOrganizationList.add(tableOrganization);
        }
        saveAllOrganizations(tableOrganizationList);
        mLogger.d(TAG, "insertOrganization");
    }

    private static void saveAllOrganizations(List<TableOrganization> organizations) {
        FlowManager.getDatabase(ConverterDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<TableOrganization>() {
                            @Override
                            public void processModel(TableOrganization organization) {
                                organization.save();
                            }
                        }).addAll(organizations).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllOrganizations");
    }

    private static void insertCurrenciesForOrganization(String organizationId,
                                                        Map<String, Organization.Currency> currencies) {
//        int count = SQLite.select(TableCurrenciesList_Table.id)
//                .from(TableCurrenciesList.class)
//                .where(TableCurrenciesList_Table.id.is(1))
//                .count();

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
        saveAllCurrenciesList(tableCurrenciesLists);
    }

    private static void saveAllCurrenciesList(List<TableCurrenciesList> tableCurrenciesLists) {
        FlowManager.getDatabase(ConverterDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<TableCurrenciesList>() {
                            @Override
                            public void processModel(TableCurrenciesList currenciesList) {
                                currenciesList.save();
                            }
                        }).addAll(tableCurrenciesLists).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                    }
                }).build().execute();
    }

    public static List<OrganizationUI> getListOrganizationsUI() {
        List<OrganizationUI> organizationUIList = new ArrayList<>();
        List<TableOrganization> organizationList = SQLite.select()
                .from(TableOrganization.class)
                .queryList();
        for (int i = 0; i < organizationList.size(); i++) {
            TableOrganization organization = organizationList.get(i);
            OrganizationUI organizationUI = new OrganizationUI();
            organizationUI.setName(organization.getName());
            organizationUI.setPhone(organization.getPhone());
            organizationUI.setAddress(organization.getAddress());
            organizationUI.setLink(organization.getLink());
            organizationUI.setCityName(getCityNameForID(organization.getCityId()));
            organizationUI.setRegionName(getRegionNameForID(organization.getRegionId()));
            organizationUI.setCurrencies(getCurrenciesForID(organization.getId()));
            organizationUIList.add(organizationUI);
        }
        return organizationUIList;
    }

    private static Map<String, OrganizationUI.CurrencyUI> getCurrenciesForID(String id) {
        List<TableCurrenciesList> tableCurrenciesLists = SQLite.select()
                .from(TableCurrenciesList.class)
                .where(TableCurrenciesList_Table.organizationId.is(id))
                .queryList();
        Map<String, OrganizationUI.CurrencyUI> stringCurrencyUIMap = new HashMap<>();
        for (int i = 0; i < tableCurrenciesLists.size(); i++) {
            TableCurrenciesList currenciesList = tableCurrenciesLists.get(i);
            OrganizationUI.CurrencyUI currencyUI = new OrganizationUI(). new CurrencyUI();
            currencyUI.setAsk(currenciesList.getAsk());
            currencyUI.setBid(currenciesList.getBid());
            stringCurrencyUIMap.put(getCurrencyNameForID(currenciesList.getCurrencyId()), currencyUI);
        }

        return stringCurrencyUIMap;
    }

    private static String getCurrencyNameForID(String currencyId) {
        List<TableCurrencyMap> currencyMaps = SQLite.select()
                .from(TableCurrencyMap.class)
                .where(TableCurrencyMap_Table.id.is(currencyId))
                .queryList();
        return currencyMaps.get(0).getName();
    }

    private static String getRegionNameForID(String regionId) {
        List<TableRegionMap> regionMaps = SQLite.select()
                .from(TableRegionMap.class)
                .where(TableRegionMap_Table.id.is(regionId))
                .queryList();
        return regionMaps.get(0).getName();

    }

    private static String getCityNameForID(String cityId) {

        List<TableCityMap> cityMaps = SQLite.select()
                .from(TableCityMap.class)
                .where(TableCityMap_Table.id.is(cityId))
                .queryList();
        return cityMaps.get(0).getName();
    }


}

