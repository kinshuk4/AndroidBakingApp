package com.k2udacity.baking.model;

import android.os.Parcel;
import android.support.test.espresso.core.deps.guava.collect.ImmutableList;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.k2udacity.baking.utils.ParcelUtils.obtainParcelForParcelable;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class RecipeTest {
    @Test
    public void testRecipeIsParcelable() throws Exception {
        Step step = new Step("id", "short desc", "desc", "vid_url", "thumb_url");
        Ingredient ingredient = new Ingredient("measure", "ingredient", "quantity");

        Recipe recipe = new Recipe("id", "name",ImmutableList.of(ingredient), ImmutableList.of(step), "servings", "image");
        Parcel parcel = obtainParcelForParcelable(recipe);

        Recipe createdFromParcel = Recipe.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getId(), is("id"));
        boolean data = listEquals(createdFromParcel.getSteps(), ImmutableList.of(step));
        assertTrue(listEquals(createdFromParcel.getSteps(), ImmutableList.of(step)));
        assertTrue(listEquals(createdFromParcel.getIngredients(), ImmutableList.of(ingredient)));
    }

    private static boolean listEquals(List<?> listA, ImmutableList<?> listB){
        return listA.containsAll(listB) && listB.containsAll(listA);

    }
}