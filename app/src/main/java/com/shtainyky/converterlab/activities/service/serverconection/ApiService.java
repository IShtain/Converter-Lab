package com.shtainyky.converterlab.activities.service.serverconection;

import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;

import retrofit2.Call;
import retrofit2.http.GET;

interface ApiService {
    @GET("ru/public/currency-cash.json")
    Call<RootModel> getModelsList();

}
