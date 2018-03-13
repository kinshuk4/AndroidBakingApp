package com.k2udacity.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Recipe implements Parcelable {
    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private String servings;
    private String image;

    public Recipe(List<Ingredient> ingredients, String id, String servings, String name, String image, List<Step> steps) {
        this.ingredients = ingredients;
        this.id = id;
        this.servings = servings;
        this.name = name;
        this.image = image;
        this.steps = steps;
    }

    public static final List<Recipe> RECIPE_ITEMS = new ArrayList<Recipe>();
    public static final Map<String, Recipe> RECIPE_MAP = new HashMap<String, Recipe>();

    public static void addItem(Recipe item) {
        RECIPE_ITEMS.add(item);
        RECIPE_MAP.put(item.id, item);
    }

    public Recipe(String id) {
        this.id = id;
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Step> getSteps() {
        return steps;
    }


    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "[ingredients = " + ingredients + ", id = " + id + ", servings = " + servings + ", name = " + name + ", image = " + image + ", steps = " + steps + "]";
    }

    public Recipe(Parcel in) {
        id = in.readString();
        servings = in.readString();
        name = in.readString();
        image = in.readString();
        ingredients = new ArrayList<Ingredient>();
        steps = new ArrayList<Step>();
        in.readTypedList(ingredients, Ingredient.CREATOR);
        in.readTypedList(steps, Step.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(servings);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeTypedList(ingredients);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}