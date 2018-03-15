package com.k2udacity.baking.utils;


import android.os.Bundle;
import android.util.Pair;

import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Ingredient;
import com.k2udacity.baking.model.Recipe;
import com.k2udacity.baking.model.Step;

import java.util.List;

public class BundleUtils {
    private BundleUtils() {

    }

    public static Pair<List<Ingredient>, List<Step>> extractStepsAndIngredients(Bundle bundle, String key) {
        List<Ingredient> ingredients = null;
        List<Step> steps = null;
        if (bundle != null) {
            Recipe recipe = bundle.getParcelable(key);
            if (recipe != null) {
                ingredients = recipe.getIngredients();
                steps = recipe.getSteps();
            }
        }

        return Pair.<List<Ingredient>, List<Step>>create(ingredients, steps);
    }
}
