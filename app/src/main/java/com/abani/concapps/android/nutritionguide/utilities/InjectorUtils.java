package com.abani.concapps.android.nutritionguide.utilities;

import android.content.Context;

import com.abani.concapps.android.nutritionguide.AppExecutors;
import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.NutritionGuideRepository;
import com.abani.concapps.android.nutritionguide.data.database.AppDatabase;
import com.abani.concapps.android.nutritionguide.data.database.NutrientDao;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.data.network.ApiClient;
import com.abani.concapps.android.nutritionguide.data.network.ApiInterface;
import com.abani.concapps.android.nutritionguide.data.network.NutrientNetworkDataSource;
import com.abani.concapps.android.nutritionguide.viewmodels.PreferredFoodListViewModelFactory;

import java.util.Random;

public class InjectorUtils {

    public static NutritionGuideRepository getRepository(Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        NutrientNetworkDataSource networkDataSource = NutrientNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return NutritionGuideRepository.getInstance(appDatabase.nutrientDao(), networkDataSource, executors);
    }

    public static NutrientNetworkDataSource getNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return NutrientNetworkDataSource.getInstance(context, executors);
    }

    public static ApiInterface getNetworkApiInterface() {
        return ApiClient.getRetrofitClient().create(ApiInterface.class);
    }

    public static NutrientDao getLocalNutrientDao(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return appDatabase.nutrientDao();
    }

    public static PreferredFoodListViewModelFactory getPreferredFoodViewModelFactory(Context context) {
        NutritionGuideRepository repository = getRepository(context);
        return new PreferredFoodListViewModelFactory(repository);
    }

    public static int getRandomDrawableResource() {

        Random random = new Random();
        int randomInt = random.nextInt(6);
        switch (randomInt){
            case 1: return R.drawable.ic_gradient_1;
            case 2: return R.drawable.ic_gradient_2;
            case 3: return R.drawable.ic_gradient_3;
            case 4: return R.drawable.ic_gradient_4;
            case 5: return R.drawable.ic_gradient_5;
        }
        return R.drawable.ic_gradient_6;
    }

    public static int getImageResourceByNutrient(Nutrient nutrient) {

        switch (nutrient.getName().toUpperCase()){
            case "VITAMIN A": return R.drawable.ic_carrot;
            case "VITAMIN C": return R.drawable.ic_broccoli;
            case "IRON": return R.drawable.ic_beans;
            case "PROTEIN": return R.drawable.ic_fish_icon;
            case "VITAMIN E": return R.drawable.ic_sunflower;
            case "VITAMIN D": return R.drawable.ic_fish_icon;
            case "CALCIUM": return R.drawable.ic_spinach;
            case "VITAMIN K": return R.drawable.ic_carrot;
            case "VITAMIN B": return R.drawable.ic_carrot;
            default: return R.drawable.ic_carrot;
        }
    }
}
