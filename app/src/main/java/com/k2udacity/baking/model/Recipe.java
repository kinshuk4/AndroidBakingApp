package com.k2udacity.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Recipe implements Parcelable {
    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private String servings;
    private String image;

    public Recipe(String id, String name, List<Ingredient> ingredients, List<Step> steps, String servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    private Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readString();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;

        Recipe recipe = (Recipe) o;

        if (!getId().equals(recipe.getId())) return false;
        if (!getName().equals(recipe.getName())) return false;
        if (getIngredients() != null ? !getIngredients().equals(recipe.getIngredients()) : recipe.getIngredients() != null)
            return false;
        if (getSteps() != null ? !getSteps().equals(recipe.getSteps()) : recipe.getSteps() != null)
            return false;
        if (getServings() != null ? !getServings().equals(recipe.getServings()) : recipe.getServings() != null)
            return false;
        return getImage() != null ? getImage().equals(recipe.getImage()) : recipe.getImage() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getIngredients() != null ? getIngredients().hashCode() : 0);
        result = 31 * result + (getSteps() != null ? getSteps().hashCode() : 0);
        result = 31 * result + (getServings() != null ? getServings().hashCode() : 0);
        result = 31 * result + (getImage() != null ? getImage().hashCode() : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeString(servings);
        parcel.writeString(image);
    }
}