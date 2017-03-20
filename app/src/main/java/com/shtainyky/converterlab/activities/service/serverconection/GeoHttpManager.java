package com.shtainyky.converterlab.activities.service.serverconection;


import com.google.gson.JsonObject;
import com.shtainyky.converterlab.activities.util.logger.LogManager;
import com.shtainyky.converterlab.activities.util.logger.Logger;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class GeoHttpManager {

    private static GeoHttpManager sManager;
    private  GeoApiService mApiService;
    private Logger mLogger = LogManager.getLogger();
    private static final String TAG = "GeoHttpManager";
    private static final String BASE_URL = "https://maps.googleapis.com/";

    private GeoHttpManager() {
    }

    public static GeoHttpManager getInstance() {
        if (sManager == null) {
            sManager = new GeoHttpManager();
        } else {
            return sManager;
        }
        return sManager;
    }

    public void initGeoService() {
        OkHttpClient okHttpClient = getOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = retrofit.create(GeoApiService.class);
    }

    private OkHttpClient getOkHttpClient() {
        final HttpLoggingInterceptor loggingBODY = new HttpLoggingInterceptor();
        loggingBODY.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingBODY)
                .build();
    }

    public void getResponse(String address, final OnGeoResponseListener listener) {

        final Call<JsonObject> modelCall = mApiService.getRequest(address);

        modelCall.enqueue(new Callback<JsonObject>() {
                              @Override
                              public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                  mLogger.d(TAG, response.code() + "");
                                  listener.onSuccess(response.body().toString());
                              }

                              @Override
                              public void onFailure(Call<JsonObject> call, Throwable t) {
                                  listener.onError(t.getMessage());
                              }
                          }
        );
    }

    public interface OnGeoResponseListener {
        void onSuccess(String srtResponse);

        void onError(String message);
    }


}

