package com.shtainyky.converterlab.service.serverconection;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GeoApiService {
    @GET("maps/api/geocode/json?")
    Call<JsonObject> getRequest(@Query("address") String strAddress);

}
