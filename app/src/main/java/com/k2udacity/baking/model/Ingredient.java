package com.k2udacity.baking.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Ingredient implements Parcelable {
    private String measure;

    private String ingredient;

    private String quantity;

    public Ingredient(String measure, String ingredient, String quantity) {
        this.measure = measure;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient(Parcel in) {
        measure = in.readString();
        ingredient = in.readString();
        quantity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(measure);
        dest.writeString(ingredient);
        dest.writeString(quantity);
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;

        Ingredient that = (Ingredient) o;

        if (getMeasure() != null ? !getMeasure().equals(that.getMeasure()) : that.getMeasure() != null)
            return false;
        if (getIngredient() != null ? !getIngredient().equals(that.getIngredient()) : that.getIngredient() != null)
            return false;
        return getQuantity() != null ? getQuantity().equals(that.getQuantity()) : that.getQuantity() == null;
    }

    @Override
    public int hashCode() {
        int result = getMeasure() != null ? getMeasure().hashCode() : 0;
        result = 31 * result + (getIngredient() != null ? getIngredient().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        return result;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}