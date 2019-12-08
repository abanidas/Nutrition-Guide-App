package com.abani.concapps.android.nutritionguide.data.database;

import android.arch.persistence.room.TypeConverter;

import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class FoodListConverter {

    @TypeConverter
    public static String fromFoodListToString(List<Food> foods){
        if (foods == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>() {}.getType();
        String jsonString = gson.toJson(foods, type);
        return jsonString;
    }

    @TypeConverter
    public static List<Food> fromStringToList(String jsonString) {
        if (jsonString == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>(){}.getType();
        List<Food> foods = gson.fromJson(jsonString, type);
        return foods;
    }
}
