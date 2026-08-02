package com.example.ecosort;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface WasteService
{
@GET("5c561a5aebccbd55f0d8")
    Call<APIResponse> getWastes();

static WasteService getInstance()
{
WasteService wasteService = null;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.npoint.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    return retrofit.create(WasteService.class);
}
}
