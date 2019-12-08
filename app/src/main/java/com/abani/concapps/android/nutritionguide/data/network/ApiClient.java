package com.abani.concapps.android.nutritionguide.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.abani.concapps.android.nutritionguide.utilities.Constants.NUTRIENT_BASE_URL;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NUTRIENT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
