package com.abani.concapps.android.nutritionguide.data.network;

import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.data.models.VersionTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("api/nutrients")
    Call<List<Nutrient>> getAllNutrients();//load nutrients from server

    @GET("api/version")
    Call<VersionTable> getCurrentVersion();//get version for syncing
}
