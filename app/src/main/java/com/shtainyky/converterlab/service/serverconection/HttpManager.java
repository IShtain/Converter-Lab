package com.shtainyky.converterlab.service.serverconection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shtainyky.converterlab.util.logger.LogManager;
import com.shtainyky.converterlab.util.logger.Logger;
import com.shtainyky.converterlab.models.modelRetrofit.RootModel;
import com.shtainyky.converterlab.models.modelRetrofit.city.CityDeserializer;
import com.shtainyky.converterlab.models.modelRetrofit.city.CityMap;
import com.shtainyky.converterlab.models.modelRetrofit.currency.CurrencyDeserializer;
import com.shtainyky.converterlab.models.modelRetrofit.currency.CurrencyMap;
import com.shtainyky.converterlab.models.modelRetrofit.region.RegionDeserializer;
import com.shtainyky.converterlab.models.modelRetrofit.region.RegionMap;

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
    private static HttpManager sManager;
    private Logger mLogger = LogManager.getLogger();
    private ApiService mApiService;

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (sManager == null) {
            sManager = new HttpManager();
        } else {
            return sManager;
        }
        return sManager;
    }


    public void init() {
        OkHttpClient okHttpClient = getOkHttpClient();
        GsonBuilder gsonBuilder = getGsonBuilder();
        GsonConverterFactory factory = getGsonConverterFactory(gsonBuilder);
        Retrofit retrofit = getRetrofit(okHttpClient, factory);
        mApiService = retrofit.create(ApiService.class);
    }


    private OkHttpClient getOkHttpClient() {
        final HttpLoggingInterceptor loggingBODY = new HttpLoggingInterceptor();
        loggingBODY.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingBODY)
                .build();
    }

    private GsonBuilder getGsonBuilder() {
        final Type typeRegion = new TypeToken<List<RegionMap>>() {
        }.getType();
        final Type typeCity = new TypeToken<List<CityMap>>() {
        }.getType();
        final Type typeCurrency = new TypeToken<List<CurrencyMap>>() {
        }.getType();

        return new GsonBuilder()
                .registerTypeAdapter(typeRegion, new RegionDeserializer())
                .registerTypeAdapter(typeCity, new CityDeserializer())
                .registerTypeAdapter(typeCurrency, new CurrencyDeserializer());
    }


    private GsonConverterFactory getGsonConverterFactory(GsonBuilder gsonBuilder) {
        final Gson gson = gsonBuilder.create();

        return GsonConverterFactory.create(gson);
    }

    private Retrofit getRetrofit(OkHttpClient okHttpClient, GsonConverterFactory factory) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(factory)
                .build();
    }

    public void getResponse(final OnResponseListener listener) {

        final Call<RootModel> modelCall = mApiService.getModelsList();

        modelCall.enqueue(new Callback<RootModel>() {
                              @Override
                              public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                                  mLogger.d(TAG, response.code() + "");
                                  listener.onSuccess(response.body());

                              }

                              @Override
                              public void onFailure(Call<RootModel> call, Throwable t) {
                                  listener.onError(t.getMessage());
                              }
                          }
        );
    }

    public interface OnResponseListener {
        void onSuccess(RootModel rootModel);

        void onError(String message);
    }



}
