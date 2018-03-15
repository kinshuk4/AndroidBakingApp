package com.k2udacity.baking.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.k2udacity.baking.R;
import com.k2udacity.baking.adapter.StepAdapter;
import com.k2udacity.baking.model.Recipe;
import com.k2udacity.baking.model.Step;
import com.k2udacity.baking.ui.fragment.RecipeDetailsFragment;
import com.k2udacity.baking.ui.fragment.StepDetailsFragment;
import com.k2udacity.baking.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsFragment.RecipeDetailsOnClickListener,
        StepDetailsFragment.StepDetailsOnClickListener {

    private static String LOG_TAG = RecipeDetailsActivity.class.getSimpleName();
    private Recipe recipe;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        Log.d(LOG_TAG, "Inside onCreate()");

        Intent intent = getIntent();
        if (intent != null) {
            recipe = intent.getParcelableExtra(getString(R.string.recipe_intent_key));
        }

        if (recipe != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(recipe.getName());
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.recipe_intent_key), recipe);

            boolean isFragmentAdded = FragmentUtils.addFragment(this, R.id.container_recipes, RecipeDetailsFragment.newInstance(bundle));
            if (!isFragmentAdded) {
                return;
            }

            if (getResources().getBoolean(R.bool.isTablet)) {
                List<Step> steps = recipe.getSteps();
                if (steps != null) {
                    bundle = getStepsBundle();
                    FragmentUtils.addFragment(this, R.id.container_steps, StepDetailsFragment.newInstance(bundle));
                }
            }
        }
    }

    @Override
    public void onStepSelected(int position) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            List<Step> steps = recipe.getSteps();
            if (steps != null) {
                this.position = position;

                Bundle bundle = getStepsBundle();
                FragmentUtils.replaceFragment(this, R.id.container_steps, StepDetailsFragment.newInstance(bundle));

                FragmentManager fragmentManager = getSupportFragmentManager();
                RecipeDetailsFragment recipeDetailsFragment = (RecipeDetailsFragment) fragmentManager
                        .findFragmentByTag(RecipeDetailsFragment.class.getSimpleName());
                if (recipeDetailsFragment != null) {
                    StepAdapter stepAdapter = recipeDetailsFragment.getStepAdapter();
                    stepAdapter.setSelectedRowIndex(position);
                    stepAdapter.notifyDataSetChanged();
                }
            }
        } else {
            startStepDetailsActivity();
        }
    }

    @NonNull
    private Bundle getStepsBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.steps_intent_key), (ArrayList<Step>) recipe.getSteps());
        bundle.putInt(getString(R.string.step_position_key), position);
        return bundle;
    }

    private void startStepDetailsActivity() {
        List<Step> steps = recipe.getSteps();

        Intent intent = new Intent(this, StepDetailsActivity.class);
        intent.putParcelableArrayListExtra(getString(R.string.steps_intent_key), (ArrayList<Step>) steps);
        intent.putExtra(getString(R.string.step_position_key), position);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            intent.putExtra(getString(R.string.recipe_name_key), actionBar.getTitle());
        }
        startActivity(intent);
    }

}