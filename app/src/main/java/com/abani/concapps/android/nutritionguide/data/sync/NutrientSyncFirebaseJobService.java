package com.abani.concapps.android.nutritionguide.data.sync;

import android.util.Log;

import com.abani.concapps.android.nutritionguide.AppExecutors;
import com.abani.concapps.android.nutritionguide.data.database.NutrientDao;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.data.models.VersionTable;
import com.abani.concapps.android.nutritionguide.data.network.ApiInterface;
import com.abani.concapps.android.nutritionguide.utilities.InjectorUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutrientSyncFirebaseJobService extends JobService {

    private static final String LOG_TAG = NutrientSyncFirebaseJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters job) {

        //NutrientNetworkDataSource networkDataSource = InjectorUtils.getNetworkDataSource(this.getApplicationContext());

        final AppExecutors executors = AppExecutors.getInstance();
        final NutrientDao nutrientDao = InjectorUtils.getLocalNutrientDao(this.getApplicationContext());
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Running netrwork check with cache");
                final ApiInterface apiInterface = InjectorUtils.getNetworkApiInterface();

                getAndCheckVersionOfLocalAndServer(apiInterface, nutrientDao, executors, job);

            }
        });
        return true;
    }

    //compare version from local and server. And load new content
    private void getAndCheckVersionOfLocalAndServer(final ApiInterface apiInterface, final NutrientDao nutrientDao, final AppExecutors executors, final JobParameters job) {
        Call<VersionTable> versionResponse = apiInterface.getCurrentVersion();
        versionResponse.enqueue(new Callback<VersionTable>() {
            @Override
            public void onResponse(Call<VersionTable> call, final Response<VersionTable> response) {


                executors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<VersionTable> localVersionTable = nutrientDao.loadLocalVersion();

                        if ((!localVersionTable.isEmpty() && localVersionTable.get(0).getVersion() < response.body().getVersion())
                                || localVersionTable.isEmpty()) {

                            Log.d(LOG_TAG, "Updating local data");

                            executors.diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    nutrientDao.insertVersionTable(response.body());

                                    nutrientDao.deleteAllNutrient();
                                }
                            });

                            insertUpdatedNutrients(apiInterface, nutrientDao, executors, job);

                        } else {

                            Log.d(LOG_TAG, "No update required");
                            jobFinished(job, false);
                        }
                    }
                });




            }

            @Override
            public void onFailure(Call<VersionTable> call, Throwable t) {
                Log.e(LOG_TAG, "Error: "+t.getMessage());
            }
        });
    }

    private void insertUpdatedNutrients(ApiInterface apiInterface, final NutrientDao nutrientDao, final AppExecutors executors, final JobParameters job) {

        Call<List<Nutrient>> nutrientsResponse = apiInterface.getAllNutrients();
        nutrientsResponse.enqueue(new Callback<List<Nutrient>>() {
            @Override
            public void onResponse(Call<List<Nutrient>> call, Response<List<Nutrient>> response) {
                final List<Nutrient> nutrients = response.body();
                if (nutrients != null && !nutrients.isEmpty()) {
                    executors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            nutrientDao.insertAllNutrients(nutrients.toArray(new Nutrient[nutrients.size()]));
                        }
                    });

                }
                Log.d(LOG_TAG, "Local data updated successfully");
                jobFinished(job, false);
            }

            @Override
            public void onFailure(Call<List<Nutrient>> call, Throwable t) {
                Log.e(LOG_TAG, "Error: "+t.getMessage());
            }
        });
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
