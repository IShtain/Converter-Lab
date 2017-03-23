package com.shtainyky.converterlab.service.serverconection;

import com.shtainyky.converterlab.models.modelRetrofit.RootModel;

import retrofit2.Call;
import retrofit2.http.GET;

interface ApiService {
    @GET("ru/public/currency-cash.json")
    Call<RootModel> getModelsList();

}
