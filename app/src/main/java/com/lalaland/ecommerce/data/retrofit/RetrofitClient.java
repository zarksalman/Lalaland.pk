package com.lalaland.ecommerce.data.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.lalaland.ecommerce.helpers.AppConstants.BASE_URL;

public class RetrofitClient {

    private static RetrofitClient retrofitClient;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {

        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public LalalandServiceApi createClient() {
        return retrofit.create(LalalandServiceApi.class);
    }
}
