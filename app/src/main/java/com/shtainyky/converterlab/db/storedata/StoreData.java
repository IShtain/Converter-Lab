package com.shtainyky.converterlab.db.storedata;

import android.database.sqlite.SQLiteException;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import com.shtainyky.converterlab.db.converter.ConvertData;
import com.shtainyky.converterlab.db.storeModel.OrganizationDatabase;
import com.shtainyky.converterlab.db.storeModel.TableCityMap;
import com.shtainyky.converterlab.db.storeModel.TableCityMap_Table;
import com.shtainyky.converterlab.db.storeModel.TableCurrenciesList;
import com.shtainyky.converterlab.db.storeModel.TableCurrenciesList_Table;
import com.shtainyky.converterlab.db.storeModel.TableCurrencyMap;
import com.shtainyky.converterlab.db.storeModel.TableCurrencyMap_Table;
import com.shtainyky.converterlab.db.storeModel.TableDate;
import com.shtainyky.converterlab.db.storeModel.TableDate_Table;
import com.shtainyky.converterlab.db.storeModel.TableOrganization;
import com.shtainyky.converterlab.db.storeModel.TableOrganization_Table;
import com.shtainyky.converterlab.db.storeModel.TableRegionMap;
import com.shtainyky.converterlab.db.storeModel.TableRegionMap_Table;
import com.shtainyky.converterlab.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.models.modelRetrofit.organization.Organization;
import com.shtainyky.converterlab.models.modelRetrofit.region.RegionMap;
import com.shtainyky.converterlab.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.util.Constants;
import com.shtainyky.converterlab.util.logger.LogManager;
import com.shtainyky.converterlab.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class StoreData {
    private static Logger mLogger = LogManager.getLogger();
    private static String TAG = "StoreData";

    private static StoreData sStoreData;
    private OnAllDBTransactionFinishedListener mListener;
    private AtomicInteger counter;
    private static final int max_count_transaction = 5;

    private StoreData() {
    }

    public static StoreData getInstance() {
        if (sStoreData == null) {
            sStoreData = new StoreData();
        } else {
            return sStoreData;
        }
        return sStoreData;
    }

    public interface OnAllDBTransactionFinishedListener {
        void onSuccess();

        void onError();

    }

    public void saveData(RootModel rootModel, OnAllDBTransactionFinishedListener allDBTransactionFinishedListener) {
        counter = new AtomicInteger();
        mLogger.d(TAG, "max_count_transaction i = " + max_count_transaction);
        mListener = allDBTransactionFinishedListener;
        insertDate(rootModel.getDate());
        insertCurrencyMap(rootModel.getCurrencies());
        insertCityMap(rootModel.getCities());
        insertRegionMap(rootModel.getRegions());
        insertOrganization(rootModel.getOrganizations());
    }

    public void insertDate(String date) {
        TableDate tableDate = ConvertData.convertDate(date);
        tableDate.save();
    }

    public String getDate() {
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
        } catch (SQLiteException e) {
            return Constants.DATABASE_NOT_CREATED;
        }

    }

    private void insertCurrencyMap(List<CurrencyMap> currencyMaps) {
        List<TableCurrencyMap> tableCurrencyList = ConvertData.convertCurrencies(currencyMaps);
        saveAllCurrenciesMap(tableCurrencyList);
        mLogger.d(TAG, "insertCurrencyMap");
    }

    private void saveAllCurrenciesMap(List<TableCurrencyMap> tableCurrencyList) {
        FlowManager.getDatabase(OrganizationDatabase.class)
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
                        mListener.onError();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        if (counter.incrementAndGet() == max_count_transaction)
                            mListener.onSuccess();
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllCurrenciesMap");
    }

    private void insertCityMap(List<CityMap> cityMaps) {
        List<TableCityMap> tableCityMapList = ConvertData.convertCities(cityMaps);
        saveAllCitiesMap(tableCityMapList);
        mLogger.d(TAG, "insertCityMap");
    }

    private void saveAllCitiesMap(List<TableCityMap> tableCityList) {
        FlowManager.getDatabase(OrganizationDatabase.class)
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
                        mListener.onError();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        if (counter.incrementAndGet() == max_count_transaction)
                            mListener.onSuccess();
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllCitiesMap");
    }

    private void insertRegionMap(List<RegionMap> regionMaps) {
        List<TableRegionMap> tableRegionMapList = ConvertData.convertRegions(regionMaps);
        saveAllRegions(tableRegionMapList);
        mLogger.d(TAG, "insertRegionMap");
    }

    private void saveAllRegions(List<TableRegionMap> tableRegionMapList) {
        FlowManager.getDatabase(OrganizationDatabase.class)
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
                        mListener.onError();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        if (counter.incrementAndGet() == max_count_transaction)
                            mListener.onSuccess();
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllRegions");
    }

    private void insertOrganization(List<Organization> organizations) {
        List<TableOrganization> tableOrganizationList = ConvertData.convertOrganizations(organizations);
        saveAllOrganizations(tableOrganizationList);

        List<TableCurrenciesList> newTableCurrenciesLists = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            newTableCurrenciesLists.addAll(getCurrenciesForOrganization
                    (organizations.get(i).getId(), organizations.get(i).getCurrencies()));
        }

        saveAllCurrenciesList(newTableCurrenciesLists);
        mLogger.d(TAG, "insertOrganization");
    }

    private void saveAllOrganizations(List<TableOrganization> organizations) {
        FlowManager.getDatabase(OrganizationDatabase.class)
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
                        mListener.onError();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        if (counter.incrementAndGet() == max_count_transaction)
                            mListener.onSuccess();
                    }
                }).build().execute();
        mLogger.d(TAG, "saveAllOrganizations");
    }

    private List<TableCurrenciesList> getCurrenciesForOrganization(String orgID, Map<String, Organization.Currency> currencies) {
        List<TableCurrenciesList> newTableCurrenciesLists = ConvertData.convertListCurrenciesForOrganization(orgID, currencies);
        List<TableCurrenciesList> oldTableCurrenciesLists = SQLite.select()
                .from(TableCurrenciesList.class)
                .where(TableCurrenciesList_Table.organizationId.is(orgID))
                .queryList();

        mLogger.d(TAG, "oldTableCurrenciesLists.size() = " + oldTableCurrenciesLists.size());
        mLogger.d(TAG, "newTableCurrenciesLists.size() = " + newTableCurrenciesLists.size());
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
        return newTableCurrenciesLists;
    }

    private boolean haveSameId(TableCurrenciesList firstCurrencyList, TableCurrenciesList secondCurrencyList) {
        return firstCurrencyList.getId().equals(secondCurrencyList.getId());
    }

    private void saveAllCurrenciesList(List<TableCurrenciesList> tableCurrenciesLists) {
        FlowManager.getDatabase(OrganizationDatabase.class)
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
                        if (counter.incrementAndGet() == max_count_transaction) {
                            mListener.onSuccess();
                        }
                    }
                }).build().execute();
    }

    //************************************************************************************

    public List<OrganizationUI> getListOrganizationsUI() {
        List<OrganizationUI> organizationUIList = new ArrayList<>();
        List<TableOrganization> organizationList = SQLite.select()
                .from(TableOrganization.class)
                .orderBy(TableOrganization_Table.name, true)
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

    public OrganizationUI getOrganizationForID(String id) {
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

    private List<OrganizationUI.CurrencyUI> getCurrenciesForID(String id) {
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

    private String getCurrencyNameForID(String currencyId) {
        List<TableCurrencyMap> currencyMaps = SQLite.select()
                .from(TableCurrencyMap.class)
                .where(TableCurrencyMap_Table.id.is(currencyId))
                .queryList();
        return currencyMaps.get(0).getName();
    }

    private String getRegionNameForID(String regionId) {
        List<TableRegionMap> regionMaps = SQLite.select()
                .from(TableRegionMap.class)
                .where(TableRegionMap_Table.id.is(regionId))
                .queryList();
        return regionMaps.get(0).getName();

    }

    private String getCityNameForID(String cityId) {
        List<TableCityMap> cityMaps = SQLite.select()
                .from(TableCityMap.class)
                .where(TableCityMap_Table.id.is(cityId))
                .queryList();
        return cityMaps.get(0).getName();
    }


}

