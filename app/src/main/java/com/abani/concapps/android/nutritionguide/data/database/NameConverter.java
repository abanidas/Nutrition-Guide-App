package com.abani.concapps.android.nutritionguide.data.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NameConverter {

    @TypeConverter
    public static String fromFoodListToString(List<String> foods){
        if (foods == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String jsonString = gson.toJson(foods, type);
        return jsonString;
    }

    @TypeConverter
    public static List<String> fromStringToList(String jsonString) {
        if (jsonString == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> foods = gson.fromJson(jsonString, type);
        return foods;
    }
}
