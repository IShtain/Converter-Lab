package com.shtainyky.converterlab.activities.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityDeserializer;
import com.shtainyky.converterlab.activities.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionDeserializer;
import com.shtainyky.converterlab.activities.models.modelRetrofit.region.RegionMap;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    private static final String BASE_URL = "http://resources.finance.ua/";
    private static HttpManager manager;

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

//        GsonBuilder gsonBuilder = initCityGsonBuilder();
//        GsonBuilder gsonBuilder = initCurrencyGsonBuilder();
        GsonBuilder gsonBuilder = getRegionGsonBuilder();
        GsonConverterFactory factory = getGsonConverterFactory(gsonBuilder);
        Retrofit retrofit = getRetrofit(okHttpClient, factory);

        final ApiService apiService = retrofit.create(ApiService.class);

        getResponse(apiService);
    }


    public OkHttpClient getOkHttpClient() {
    //    final HttpLoggingInterceptor loggingBODY = new HttpLoggingInterceptor();
//        loggingBODY.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        final HttpLoggingInterceptor loggingHEADERS = new HttpLoggingInterceptor();
//        loggingHEADERS.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
               // .addInterceptor(loggingHEADERS)
               // .addInterceptor(loggingBODY)
                .build();

        return okHttpClient;
    }

    public GsonBuilder getRegionGsonBuilder() {
        final Type type = new TypeToken<List<RegionMap>>() {
        }.getType();

        final GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(type, new RegionDeserializer());

        return gsonBuilder;
    }

    public GsonBuilder getCityGsonBuilder() {
        final Type type = new TypeToken<List<CityMap>>() {
        }.getType();

        final GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(type, new CityDeserializer());

        return gsonBuilder;
    }

    public GsonBuilder getCurrencyGsonBuilder() {
        final Type type = new TypeToken<List<CityMap>>() {
        }.getType();

        final GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(type, new CityDeserializer());

        return gsonBuilder;
    }

    public GsonConverterFactory getGsonConverterFactory(GsonBuilder gsonBuilder) {
        final Gson gson = gsonBuilder.create();

        final GsonConverterFactory factory = GsonConverterFactory.create(gson);

        return factory;
    }

    public Retrofit getRetrofit(OkHttpClient okHttpClient, GsonConverterFactory factory) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(factory)
                .build();

        return retrofit;
    }

    public void getResponse(ApiService apiService) {

        final Call<RootModel> modelCall = apiService.getModelsList();

        modelCall.enqueue(new Callback<RootModel>() {
                              @Override
                              public void onResponse(Call<RootModel> call, Response<RootModel> response) {


                              }

                              @Override
                              public void onFailure(Call<RootModel> call, Throwable t) {

                              }
                          }
        );
    }

}
