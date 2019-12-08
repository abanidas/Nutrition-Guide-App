package com.abani.concapps.android.nutritionguide.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.abani.concapps.android.nutritionguide.AppExecutors;
import com.abani.concapps.android.nutritionguide.data.database.NutrientDao;
import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.data.network.NutrientNetworkDataSource;

import java.util.List;

public class NutritionGuideRepository {

    private static final String LOG_TAG = NutritionGuideRepository.class.getSimpleName();

    private static final Object LOCK = new Object();

    private final NutrientDao mNutrientDao;
    private final NutrientNetworkDataSource mNetworkDataSource;
    private final AppExecutors mExecutors;

    private static NutritionGuideRepository sInstance;

    private boolean mInitialized = false;

    private NutritionGuideRepository(NutrientDao nutrientDao,
                                     NutrientNetworkDataSource networkDataSource,
                                     AppExecutors executors){
        mNutrientDao = nutrientDao;
        mNetworkDataSource = networkDataSource;
        mExecutors = executors;

        LiveData<List<Nutrient>> networkData = mNetworkDataSource.getCurrentNutrients();

        networkData.observeForever(new Observer<List<Nutrient>>() {
            @Override
            public void onChanged(@Nullable final List<Nutrient> nutrients) {

                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        deleteOldData();

                        mNutrientDao.insertAllNutrients(nutrients.toArray(new Nutrient[nutrients.size()]));
                    }
                });
            }
        });
    }

    public synchronized static NutritionGuideRepository getInstance(
            NutrientDao nutrientDao, NutrientNetworkDataSource networkDataSource,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new NutritionGuideRepository(nutrientDao, networkDataSource,
                        executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    private synchronized void initializeData() {
        if (mInitialized) return;
        mInitialized = true;

        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isNetworkFetchNeeded()){
                    mNetworkDataSource.startFetchingNutrients();
                }
            }
        });
    }

    private boolean isNetworkFetchNeeded() {
        return (mNutrientDao.loadAllNutrients().getValue() == null);
    }

    public LiveData<List<Nutrient>> getCurrentNutrients() {
        initializeData();
        return mNutrientDao.loadAllNutrients();
    }

    private void deleteOldData() {
        mNutrientDao.deleteAllNutrient();
    }

    public LiveData<List<Food>> getCurrentPreferredFoods() {

        return mNutrientDao.loadAllPreferredFoods();
    }
}
