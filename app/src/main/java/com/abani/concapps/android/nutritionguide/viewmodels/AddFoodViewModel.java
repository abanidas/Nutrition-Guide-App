package com.abani.concapps.android.nutritionguide.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.abani.concapps.android.nutritionguide.data.database.AppDatabase;

public class AddFoodViewModel extends ViewModel {

    private LiveData<Integer> foodId;

    public AddFoodViewModel(AppDatabase database, Integer foodId){
        this.foodId = database.nutrientDao().getPreferredFoodId(foodId);
    }

    public LiveData<Integer> getFoodId() {
        return foodId;
    }
}
