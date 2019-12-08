package com.abani.concapps.android.nutritionguide.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.data.models.VersionTable;

@Database(entities = {Nutrient.class, VersionTable.class, Food.class}, version = 1, exportSchema = false)
@TypeConverters({FoodListConverter.class, NameConverter.class, QuantitiesMapConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "nutrition_guide";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating the database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract NutrientDao nutrientDao();
}
