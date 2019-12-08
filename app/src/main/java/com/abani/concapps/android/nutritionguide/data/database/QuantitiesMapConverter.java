package com.abani.concapps.android.nutritionguide.data.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class QuantitiesMapConverter {

    @TypeConverter
    public static String fromQuantityMapToString(Map<String, String> quantities){
        if (quantities == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        String jsonString = gson.toJson(quantities, type);
        return jsonString;
    }

    @TypeConverter
    public static Map<String, String> fromStringToQuantityMap(String jsonString) {
        if (jsonString == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> quantities = gson.fromJson(jsonString, type);
        return quantities;
    }
}
