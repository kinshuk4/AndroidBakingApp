package com.k2udacity.baking.api.caller;

import android.util.Log;

import com.k2udacity.baking.api.RecipeService;
import com.k2udacity.baking.model.Recipe;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.k2udacity.baking.utils.Constants.RECIPE_ROOT_URL;

public class RecipeFetcher {
    private static String LOG_TAG = RecipeFetcher.class.getSimpleName();
    private RecipeService service;
    private static RecipeFetcher instance;

    private RecipeFetcher() {
        init();
    }

    static {
        try {
            instance = new RecipeFetcher();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    public static RecipeFetcher getInstance() {
        return instance;
    }


    private void init() {
        Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(RECIPE_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = RETROFIT.create(RecipeService.class);
    }

    public void getAllRecipesAsync(final IRecipeRepository recipeList) {
        Call<List<Recipe>> call = service.getAllRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (!response.isSuccessful()) {
                    Log.d(LOG_TAG, "Failed to fetch the response");
                }

                List<Recipe> outList = response.body();
                recipeList.setRecipes(outList);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    public List<Recipe> getAllRecipes() {
        try {
            return service.getAllRecipes().execute().body();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }
}
