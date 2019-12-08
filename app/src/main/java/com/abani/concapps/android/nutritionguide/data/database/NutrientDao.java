package com.abani.concapps.android.nutritionguide.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.data.models.VersionTable;

import java.util.List;

@Dao
public interface NutrientDao {

    @Query("SELECT * FROM nutrient ORDER BY id")
    LiveData<List<Nutrient>> loadAllNutrients();

    @Insert
    void insertNutrient(Nutrient nutrient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNutrients(Nutrient... nutrients);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNutrient(Nutrient nutrient);

    @Delete
    void deleteNutrient(Nutrient nutrient);

    @Query("DELETE FROM nutrient")
    void deleteAllNutrient();

    @Query("SELECT * FROM version_table")
    List<VersionTable> loadLocalVersion();

    @Query("DELETE FROM version_table")
    void deleteOldVersion();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVersionTable(VersionTable versionTable);

    @Query("SELECT * FROM preferred_foods ORDER BY id")
    LiveData<List<Food>> loadAllPreferredFoods();

    @Query("SELECT * FROM preferred_foods ORDER BY id")
    List<Food> loadAllPreferredFoodsForWidget();

    /*@Query("SELECT * FROM preferred_foods WHERE id = :id")
    Food findPreferredFoodById(Integer id);*/

    @Query("SELECT id FROM preferred_foods WHERE id = :foodId")
    LiveData<Integer> getPreferredFoodId(Integer foodId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreferredFood(Food food);

    @Delete
    void deletePreferredFood(Food food);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPreferredFood(Food... foods);
}
