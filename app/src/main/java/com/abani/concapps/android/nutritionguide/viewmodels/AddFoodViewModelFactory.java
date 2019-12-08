package com.abani.concapps.android.nutritionguide.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.abani.concapps.android.nutritionguide.data.database.AppDatabase;

public class AddFoodViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final Integer foodId;

    public AddFoodViewModelFactory(AppDatabase mDb, Integer foodId) {
        this.mDb = mDb;
        this.foodId = foodId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddFoodViewModel(mDb, foodId);
    }
}
