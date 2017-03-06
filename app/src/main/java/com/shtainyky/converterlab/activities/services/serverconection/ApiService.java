package com.shtainyky.converterlab.activities.services.serverconection;

import com.shtainyky.converterlab.activities.models.modelRetrofit.RootModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("ru/public/currency-cash.json")
    Call<RootModel> getModelsList();

}
