package com.shtainyky.converterlab.activities.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityDeserializer;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyDeserializer;
import com.shtainyky.converterlab.activities.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.org_type.OrgTypeDeserializer;
import com.shtainyky.converterlab.activities.models.modelRetrofit.org_type.OrgTypeMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionDeserializer;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static final String TAG = "HttpManager";
    private static final String BASE_URL = "http://resources.finance.ua/";
    private static HttpManager manager;
    private Logger mLogger = LogManager.getLogger();

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (manager == null) {
            manager = new HttpManager();
        } else {
            return manager;
        }
        return manager;
    }

    public void test() {

        OkHttpClient okHttpClient = getOkHttpClient();
        GsonBuilder gsonBuilder = getGsonBuilder();
        GsonConverterFactory factory = getGsonConverterFactory(gsonBuilder);
        Retrofit retrofit = getRetrofit(okHttpClient, factory);

        final ApiService apiService = retrofit.create(ApiService.class);

        getResponse(apiService);
    }


    public OkHttpClient getOkHttpClient() {
        final HttpLoggingInterceptor loggingBODY = new HttpLoggingInterceptor();
        loggingBODY.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                 .addInterceptor(loggingBODY)
                .build();

        return okHttpClient;
    }

    public GsonBuilder getGsonBuilder() {
        final Type typeRegion = new TypeToken<List<RegionMap>>() {
        }.getType();
        final Type typeCity = new TypeToken<List<CityMap>>() {
        }.getType();
        final Type typeCurrency = new TypeToken<List<CurrencyMap>>() {
        }.getType();
        final Type typeOrgType = new TypeToken<List<OrgTypeMap>>() {
        }.getType();

        return new GsonBuilder()
                .registerTypeAdapter(typeRegion, new RegionDeserializer())
                .registerTypeAdapter(typeCity, new CityDeserializer())
                .registerTypeAdapter(typeCurrency, new CurrencyDeserializer())
                .registerTypeAdapter(typeOrgType, new OrgTypeDeserializer());
    }


    public GsonConverterFactory getGsonConverterFactory(GsonBuilder gsonBuilder) {
        final Gson gson = gsonBuilder.create();

        final GsonConverterFactory factory = GsonConverterFactory.create(gson);

        return factory;
    }

    public Retrofit getRetrofit(OkHttpClient okHttpClient, GsonConverterFactory factory) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(factory)
                .build();
    }

    public void getResponse(ApiService apiService) {

        final Call<RootModel> modelCall = apiService.getModelsList();

        modelCall.enqueue(new Callback<RootModel>() {
                              @Override
                              public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                                  mLogger.d(TAG, response.code() + "");

                              }

                              @Override
                              public void onFailure(Call<RootModel> call, Throwable t) {

                              }
                          }
        );
    }

}
