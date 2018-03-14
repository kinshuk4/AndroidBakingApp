package com.k2udacity.baking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Ingredient;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private List<Ingredient> ingredients;

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_ingredient_item,
                parent,
                false
        );
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        if (ingredients == null) {
            return;
        }
        Ingredient ingredient = ingredients.get(position);
        String quantityAndMeasurement = String.format("%-8s", ingredient.getQuantity()) + ingredient.getMeasure().toLowerCase();
        holder.textViewIngredientQuantityAndMeasurement.setText(quantityAndMeasurement);

        String name = StringUtils.capitalize(ingredient.getIngredient());
        holder.textViewIngredientName.setText(name);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_ingredient)
        TextView textViewIngredientName;

        @BindView(R.id.textview_quantity)
        TextView textViewIngredientQuantityAndMeasurement;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
