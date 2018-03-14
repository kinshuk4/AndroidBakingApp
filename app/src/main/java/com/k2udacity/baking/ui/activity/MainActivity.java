package com.k2udacity.baking.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.k2udacity.baking.R;
import com.k2udacity.baking.adapter.RecipeAdapter;
import com.k2udacity.baking.loader.RecipeLoader;
import com.k2udacity.baking.model.Recipe;
import com.k2udacity.baking.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeOnClickHandler {
    private static String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_ID = 1;


    @BindView(R.id.recyclerview_recipes)
    RecyclerView recyclerViewRecipes;

    @BindView(R.id.textview_no_internet)
    TextView textViewNoInternet;

    @BindView(R.id.button_refresh)
    Button refreshButton;

    @BindView(R.id.progressbar_recipe)
    ProgressBar progressBarRecipeLoader;

    private RecipeAdapter recipeAdapter;

    private LoaderManager.LoaderCallbacks<List<Recipe>> recipeLoaderCallbacks = getLoaderCallbacks();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initLoader();
            }
        });


        recipeAdapter = new RecipeAdapter(this, this);
        recyclerViewRecipes.setAdapter(recipeAdapter);

        int spanCount = 1;
        if (getResources().getBoolean(R.bool.isTablet)) {
            spanCount = 3;
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerViewRecipes.setLayoutManager(layoutManager);

        initLoader();
    }

    private void initLoader(){
        if (NetworkUtils.isInternetAvailable(this)) {
            gotBackConnectionView();
            getLoaderManager().initLoader(LOADER_ID, null, recipeLoaderCallbacks);
        } else {
            setNoConnectionView();
            textViewNoInternet.setText(getString(R.string.no_internet_message));
        }
    }
    private void gotBackConnectionView(){
        progressBarRecipeLoader.setVisibility(View.VISIBLE);
        recyclerViewRecipes.setVisibility(View.VISIBLE);
        textViewNoInternet.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);
    }
    private void setNoConnectionView() {
        progressBarRecipeLoader.setVisibility(View.GONE);
        recyclerViewRecipes.setVisibility(View.GONE);
        textViewNoInternet.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Recipe recipe) {
        Log.d(LOG_TAG, "Inside onClick()");
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(getString(R.string.recipe_intent_key), recipe);
        startActivity(intent);
    }

    @NonNull
    private LoaderManager.LoaderCallbacks<List<Recipe>> getLoaderCallbacks() {
        return new LoaderManager.LoaderCallbacks<List<Recipe>>() {

            @Override
            public RecipeLoader onCreateLoader(int i, Bundle bundle) {
                progressBarRecipeLoader.setVisibility(View.VISIBLE);
                return new RecipeLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
                progressBarRecipeLoader.setVisibility(View.GONE);
                recipeAdapter.clear();
                recipeAdapter.setRecipes(recipes);
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {
                recipeAdapter.clear();
                recipeAdapter.notifyDataSetChanged();
            }
        };
    }
}
