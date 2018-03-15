package com.k2udacity.baking.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.k2udacity.baking.R;
import com.k2udacity.baking.adapter.IngredientAdapter;
import com.k2udacity.baking.adapter.StepAdapter;
import com.k2udacity.baking.model.Ingredient;
import com.k2udacity.baking.model.Recipe;
import com.k2udacity.baking.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.k2udacity.baking.utils.BundleUtils.extractStepsAndIngredients;

public class RecipeDetailsFragment extends Fragment implements StepAdapter.StepOnClickHandler {

    @BindView(R.id.recyclerview_ingredients)
    RecyclerView recyclerViewIngredients;

    @BindView(R.id.recyclerview_steps)
    RecyclerView recyclerViewSteps;

    @BindView(R.id.nestedscrollview_recipe_details)
    NestedScrollView nestedScrollViewRecipeDetails;

    private RecipeDetailsOnClickListener listener;
    private StepAdapter stepAdapter;

    public RecipeDetailsFragment() {

    }

    public static RecipeDetailsFragment newInstance(Bundle bundle) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setArguments(bundle);
        return recipeDetailsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_recipe, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        Pair<List<Ingredient>, List<Step>> ingredientsAndStepsPair = extractStepsAndIngredients(bundle, getString(R.string.recipe_intent_key));

        List<Ingredient> ingredients = ingredientsAndStepsPair.first;
        List<Step> steps = ingredientsAndStepsPair.second;

        //setup the adapters
        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewIngredients.setAdapter(ingredientAdapter);

        stepAdapter = new StepAdapter(getContext(), steps, this);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSteps.setAdapter(stepAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailsOnClickListener) {
            listener = (RecipeDetailsOnClickListener) context;
        } else {
            throw new RuntimeException(getString(R.string.error_unable_to_attach_recipe_details_onclick_listener) + context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(getString(R.string.scroll_position_key),
                new int[]{nestedScrollViewRecipeDetails.getScrollX(), nestedScrollViewRecipeDetails.getScrollY()});
        outState.putInt(getString(R.string.selected_row_index_key), stepAdapter.getSelectedRowIndex());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray(getString(R.string.scroll_position_key));
            if (position != null) {
                nestedScrollViewRecipeDetails.post(new Runnable() {
                    public void run() {
                        nestedScrollViewRecipeDetails.scrollTo(position[0], position[1]);
                    }
                });
            }
            int selectedRowIndex = savedInstanceState.getInt(getString(R.string.selected_row_index_key));
            stepAdapter.setSelectedRowIndex(selectedRowIndex);
            stepAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(int position) {
        listener.onStepSelected(position);
    }

    public StepAdapter getStepAdapter() {
        return stepAdapter;
    }

    //Implemented by the Activity - RecipeDetailsActivity
    public interface RecipeDetailsOnClickListener {
        void onStepSelected(int position);
    }
}
