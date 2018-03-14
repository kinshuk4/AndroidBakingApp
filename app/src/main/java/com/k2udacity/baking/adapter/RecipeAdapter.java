package com.k2udacity.baking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Recipe;
import com.k2udacity.baking.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipes;
    private RecipeOnClickHandler clickHandler;

    public RecipeAdapter(Context context, RecipeOnClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_recipe_item,
                parent,
                false
        );
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        if (recipes == null) {
            return;
        }
        Recipe recipe = recipes.get(position);
        holder.textViewRecipeName.setText(recipe.getName());
        holder.textViewServes.setText(recipe.getServings());

        ImageUtils.setImage(context, recipe.getImage(), holder.imageViewRecipeItem,  R.drawable.default_recipe);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void clear() {
        if (recipes == null) {
            return;
        }
        recipes.clear();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_recipe_name)
        TextView textViewRecipeName;

        @BindView(R.id.textview_serves)
        TextView textViewServes;

        @BindView(R.id.imageview_recipe)
        ImageView imageViewRecipeItem;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            clickHandler.onClick(recipes.get(index));
        }
    }

    public interface RecipeOnClickHandler {
        void onClick(Recipe recipe);
    }
}
