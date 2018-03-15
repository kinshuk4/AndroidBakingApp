package com.k2udacity.baking.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Ingredient;
import com.k2udacity.baking.model.Recipe;

import java.util.List;

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Ingredient> ingredients;

    ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.recipe_preferences),
                0
        );

        String jsonRecipe = sharedPreferences.getString(
                context.getString(R.string.recipe_json_key),
                null);

        sharedPreferences.edit().clear().apply();
        if (TextUtils.isEmpty(jsonRecipe)) {
            ingredients = null;
            return;
        }

        Recipe recipe = Recipe.fromJson(jsonRecipe);
        ingredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredients == null || ingredients.size() == 0) {
            return null;
        }

        Ingredient ingredient = ingredients.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget_ingredient_item);
        String quantityAndMeasurement = String.format("%-8s", ingredient.getQuantity()) + ingredient.getMeasure().toLowerCase();
        views.setTextViewText(R.id.textview_widget_ingredient_quantity_and_measurement, quantityAndMeasurement);
        views.setTextViewText(R.id.textview_widget_ingredient_name, ingredient.getIngredient());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
