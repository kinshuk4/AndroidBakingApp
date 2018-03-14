package com.k2udacity.baking.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.k2udacity.baking.api.caller.RecipeFetcher;
import com.k2udacity.baking.model.Recipe;
import java.util.List;


public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private List<Recipe> recipes;

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (recipes != null) {
            deliverResult(recipes);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Recipe> loadInBackground() {
        return RecipeFetcher.getInstance().getAllRecipes();
    }

    @Override
    public void deliverResult(List<Recipe> recipes) {
        this.recipes = recipes;
        super.deliverResult(recipes);
    }
}
