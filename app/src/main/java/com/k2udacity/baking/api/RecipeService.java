package com.k2udacity.baking.api;

import com.k2udacity.baking.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface RecipeService {
    @GET("baking.json")
    Call<List<Recipe>> getAllRecipes();
}
