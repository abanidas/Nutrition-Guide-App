package com.abani.concapps.android.nutritionguide.utilities;

import com.abani.concapps.android.nutritionguide.data.models.Nutrient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatterUtils {

    public static String formatNutrientQuantities(Map<String, String> quantityOfNutrients) {

        String resultString = "N/A";
        if (quantityOfNutrients != null) {
            resultString = "";
            for (Map.Entry<String, String> entry : quantityOfNutrients.entrySet()) {
                int id = Integer.parseInt(entry.getKey());
                resultString = resultString + Constants.currentNutrientsMap.get(new Integer(id)).getName() + " : " + entry.getValue() + "\n";
            }
        }
        return resultString;
    }

    public static void formatNutrientWithIdToMap(List<Nutrient> nutrients){
        Map<Integer, Nutrient> nutrientMap = new HashMap<>();

        for (Nutrient nutrient: nutrients){
            nutrientMap.put(nutrient.getId(), nutrient);
        }
        Constants.currentNutrientsMap.clear();
        Constants.currentNutrientsMap.putAll(nutrientMap);
    }
}
