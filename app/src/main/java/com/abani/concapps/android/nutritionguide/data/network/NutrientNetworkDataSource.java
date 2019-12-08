package com.abani.concapps.android.nutritionguide.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.abani.concapps.android.nutritionguide.AppExecutors;
import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutrientNetworkDataSource {

    private static final String LOG_TAG = NutrientNetworkDataSource.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static NutrientNetworkDataSource sInstance;

    private final Context mContext;
    private final AppExecutors mExecutors;
    private final MutableLiveData<List<Nutrient>> mDownloadedNutrients;

    private NutrientNetworkDataSource(Context context, AppExecutors appExecutors) {
        mContext = context;
        mExecutors = appExecutors;
        mDownloadedNutrients = new MutableLiveData<>();
    }

    public static NutrientNetworkDataSource getInstance(Context context, AppExecutors executors){
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = new NutrientNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Network data source created");
            }
        }
        return sInstance;
    }

    public LiveData<List<Nutrient>> getCurrentNutrients() {
        return mDownloadedNutrients;
    }

    public void startFetchingNutrients(){
        ApiInterface apiInterface = ApiClient.getRetrofitClient().create(ApiInterface.class);
        fetchNutrients(apiInterface);
    }

    //fetch from server or using json raw file if fails
    private void fetchNutrients(final ApiInterface apiInterface) {

        Log.d(LOG_TAG, "Fetching nutrients started");
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                Call<List<Nutrient>> response = apiInterface.getAllNutrients();

                response.enqueue(new Callback<List<Nutrient>>() {
                    @Override
                    public void onResponse(Call<List<Nutrient>> call, Response<List<Nutrient>> response) {

                        if (response != null && response.body().size() != 0){
                            Log.d(LOG_TAG, "Successfully retrieved nutrients");
                            mDownloadedNutrients.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Nutrient>> call, Throwable t) {
                        Log.e(LOG_TAG, ""+t.getMessage());
                        Log.d(LOG_TAG, "Started fetching from raw json file");
                        String nutrientJson = getInputStreamAsString(mContext.getResources().openRawResource(R.raw.nutrients));
                        if (nutrientJson != null){
                            List<Nutrient> nutrientList = getListNutrientsFromJsonString(nutrientJson);
                            if (nutrientList != null){
                                mDownloadedNutrients.postValue(nutrientList);
                            } else {
                                Log.e(LOG_TAG, "Error parsing json to list");
                            }
                        } else {
                            Log.e(LOG_TAG, "Error loading raw json data");
                        }
                    }
                });
            }
        });
    }

    private List<Nutrient> getListNutrientsFromJsonString(String nutrientJson) {
        if (nutrientJson == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Nutrient>>(){}.getType();
        List<Nutrient> nutrients = gson.fromJson(nutrientJson, type);
        return nutrients;
    }

    private String getInputStreamAsString(InputStream inputStream) {

        try{
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            inputStream.close();
            return new String(bytes, "UTF-8");
        } catch (IOException ie){
            Log.e(LOG_TAG, ie.getMessage());
        }
        return null;
    }
}
