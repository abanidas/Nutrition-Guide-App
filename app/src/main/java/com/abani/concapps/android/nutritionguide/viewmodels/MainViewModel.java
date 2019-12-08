package com.abani.concapps.android.nutritionguide.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.abani.concapps.android.nutritionguide.data.NutritionGuideRepository;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.utilities.InjectorUtils;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final NutritionGuideRepository mRepository;
    private LiveData<List<Nutrient>> nutrients;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = InjectorUtils.getRepository(this.getApplication());
        nutrients = mRepository.getCurrentNutrients();
    }

    public LiveData<List<Nutrient>> getNutrients() {
        return nutrients;
    }
}
