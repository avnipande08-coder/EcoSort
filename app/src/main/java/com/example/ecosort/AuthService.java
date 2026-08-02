package com.example.ecosort;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    static AuthService getInstance() {

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl("https://dummyjson.com/")
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        )
                        .build();

        return retrofit.create(AuthService.class);
    }
}