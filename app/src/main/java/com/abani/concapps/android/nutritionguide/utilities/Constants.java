package com.abani.concapps.android.nutritionguide.utilities;

import com.abani.concapps.android.nutritionguide.data.models.Nutrient;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    //TODO: add required IP address by replacing localhost
    public static final String NUTRIENT_BASE_URL = "http://localhost:8080/NutrientsFoodApi/";
    public static final String FOOD_DATA = "food_data";
    public static final String NUTRIENT_CLICKED = "nutrient_id";
    public static Map<Integer, Nutrient> currentNutrientsMap = new HashMap<>();

}
