package com.k2udacity.baking.api.caller;

import com.k2udacity.baking.model.Recipe;

import java.util.List;

public interface IRecipeRepository {
    void setRecipes(List<Recipe> recipes);
}
