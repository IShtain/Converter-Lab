package com.shtainyky.converterlab.activities.db.storedata;

import android.database.sqlite.SQLiteException;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.shtainyky.converterlab.activities.db.converter.ConvertData;
import com.shtainyky.converterlab.activities.db.storeModel.ConverterDatabase;
import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCityMap_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrenciesList_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableCurrencyMap_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableDate;
import com.shtainyky.converterlab.activities.db.storeModel.TableDate_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableOrganization;
import com.shtainyky.converterlab.activities.db.storeModel.TableOrganization_Table;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap;
import com.shtainyky.converterlab.activities.db.storeModel.TableRegionMap_Table;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoreData {

    private static Logger mLogger = LogManager.getLogger();
    private static String TAG = "StoreData";

    public static void saveData() {
        insertDate();
        insertCurrencyMap();
        insertCityMap();
        insertRegionMap();
        insertOrganization();
    }

    public static void insertDate() {
        TableDate tableDate = ConvertData.getTableDate();
        tableDate.save();
    }

    public static String getDate() {
        List<TableDate> dates;
        try {
            dates = SQLite.select()
                    .from(TableDate.class)
                    .where(TableDate_Table.id.is("date"))
                    .queryList();
            if (dates.size() > 0)
                return dates.get(0).getDate();
            else
                return Constants.DATABASE_NOT_CREATED;
        }
        catch (SQLiteException e) {
            return Constants.DATABASE_NOT_CREATED;
        }

    }

    private static void insertCurrencyMap() {
        List<TableCurrencyMap> tableCurrencyList = ConvertData.getTableCurrencyList();
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

    private static void insertCityMap() {
        List<TableCityMap> tableCityMapList = ConvertData.getTableCityMapList();
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

    private static void insertRegionMap() {
        List<TableRegionMap> tableRegionMapList = ConvertData.getTableRegionMapList();
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

    private static void insertOrganization() {
        List<TableOrganization> tableOrganizationList = ConvertData.getTableOrganizationList();
        saveAllOrganizations(tableOrganizationList);
        insertCurrenciesForOrganization();
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

    private static void insertCurrenciesForOrganization() {
        List<TableCurrenciesList> newTableCurrenciesLists = ConvertData.getTableCurrenciesLists();
        List<TableCurrenciesList> oldTableCurrenciesLists = SQLite.select()
                .from(TableCurrenciesList.class)
                .where()
                .queryList();

        mLogger.d(TAG, "oldTableCurrenciesLists.size() = " + oldTableCurrenciesLists.size());
        if (oldTableCurrenciesLists.size() > 0) {
            for (int i = 0; i < oldTableCurrenciesLists.size(); i++) {
                for (int j = 0; j < newTableCurrenciesLists.size(); j++) {
                    if (haveSameId(oldTableCurrenciesLists.get(i), newTableCurrenciesLists.get(j))) {
                        double oldAsk = oldTableCurrenciesLists.get(i).getAsk();
                        double newAsk = newTableCurrenciesLists.get(j).getAsk();
                        double diffAsk = oldAsk - newAsk;
                        newTableCurrenciesLists.get(j).setDiffAsk(diffAsk);

                        double oldBid = oldTableCurrenciesLists.get(i).getBid();
                        double newBid = newTableCurrenciesLists.get(j).getBid();
                        double diffBid = oldBid - newBid;
                        newTableCurrenciesLists.get(j).setDiffBid(diffBid);
                        break;
                    }
                }
            }
        }
        saveAllCurrenciesList(newTableCurrenciesLists);
    }

    private static boolean haveSameId(TableCurrenciesList firstCurrencyList, TableCurrenciesList secondCurrencyList) {
        return firstCurrencyList.getId().equals(secondCurrencyList.getId());
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

    //************************************************************************************

    public static List<OrganizationUI> getListOrganizationsUI() {
        List<OrganizationUI> organizationUIList = new ArrayList<>();
        List<TableOrganization> organizationList = SQLite.select()
                .from(TableOrganization.class)
                .queryList();
        for (int i = 0; i < organizationList.size(); i++) {
            TableOrganization organization = organizationList.get(i);
            OrganizationUI organizationUI = new OrganizationUI();
            organizationUI.setId(organization.getId());
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

    public static OrganizationUI getOrganizationForID(String id) {
        List<TableOrganization> organizationList = SQLite.select()
                .from(TableOrganization.class)
                .where(TableOrganization_Table.id.in(id))
                .queryList();
        if (organizationList.size() <= 0) return null;
        TableOrganization organization = organizationList.get(0);
        OrganizationUI organizationUI = new OrganizationUI();
        organizationUI.setId(organization.getId());
        organizationUI.setName(organization.getName());
        organizationUI.setPhone(organization.getPhone());
        organizationUI.setAddress(organization.getAddress());
        organizationUI.setLink(organization.getLink());
        organizationUI.setCityName(getCityNameForID(organization.getCityId()));
        organizationUI.setRegionName(getRegionNameForID(organization.getRegionId()));
        organizationUI.setCurrencies(getCurrenciesForID(organization.getId()));
        return organizationUI;
    }

    private static List<OrganizationUI.CurrencyUI> getCurrenciesForID(String id) {
        List<OrganizationUI.CurrencyUI> currencyUIs = new ArrayList<>();

        List<TableCurrenciesList> tableCurrenciesLists = SQLite.select()
                .from(TableCurrenciesList.class)
                .where(TableCurrenciesList_Table.organizationId.is(id))
                .queryList();

        for (int i = 0; i < tableCurrenciesLists.size(); i++) {
            TableCurrenciesList currenciesList = tableCurrenciesLists.get(i);
            OrganizationUI.CurrencyUI currencyUI = new OrganizationUI().new CurrencyUI();
            currencyUI.setCurrencyId(currenciesList.getCurrencyId());
            currencyUI.setName(getCurrencyNameForID(currenciesList.getCurrencyId()));
            currencyUI.setAsk(currenciesList.getAsk());
            currencyUI.setDiffAsk(currenciesList.getDiffAsk());
            currencyUI.setBid(currenciesList.getBid());
            currencyUI.setDiffBid(currenciesList.getDiffBid());
            currencyUIs.add(currencyUI);
        }
        return currencyUIs;
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

