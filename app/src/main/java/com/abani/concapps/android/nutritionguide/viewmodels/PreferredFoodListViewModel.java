package com.abani.concapps.android.nutritionguide.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.abani.concapps.android.nutritionguide.data.NutritionGuideRepository;
import com.abani.concapps.android.nutritionguide.data.models.Food;

import java.util.List;

public class PreferredFoodListViewModel extends ViewModel {

    private final NutritionGuideRepository mRepository;
    private final LiveData<List<Food>> mPreferredFoods;

    public PreferredFoodListViewModel(NutritionGuideRepository repository) {
        mRepository = repository;
        mPreferredFoods = mRepository.getCurrentPreferredFoods();
    }

    public LiveData<List<Food>> getPreferredFoods() {
        return mPreferredFoods;
    }
}
