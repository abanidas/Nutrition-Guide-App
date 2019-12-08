package com.abani.concapps.android.nutritionguide.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.abani.concapps.android.nutritionguide.data.NutritionGuideRepository;

public class PreferredFoodListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final NutritionGuideRepository mRepository;

    public PreferredFoodListViewModelFactory(NutritionGuideRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PreferredFoodListViewModel(mRepository);
    }
}
