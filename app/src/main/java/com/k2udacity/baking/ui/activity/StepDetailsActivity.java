package com.k2udacity.baking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;


import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Step;
import com.k2udacity.baking.ui.fragment.StepDetailsFragment;
import com.k2udacity.baking.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity implements
        StepDetailsFragment.StepDetailsOnClickListener {

    private List<Step> steps;
    private String recipeName;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_step);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(getString(R.string.steps_intent_key))) {
                steps = intent.getParcelableArrayListExtra(getString(R.string.steps_intent_key));
            }

            if (savedInstanceState != null) {
                position = savedInstanceState.getInt(getString(R.string.step_position_key));
            } else if (intent.hasExtra(getString(R.string.step_position_key))) {
                position = intent.getIntExtra(getString(R.string.step_position_key), position);
            }

            recipeName = intent.getStringExtra(getString(R.string.recipe_name_key));
        }

        if (steps != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(recipeName);
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(getString(R.string.steps_intent_key), (ArrayList<Step>) steps);
            bundle.putInt(getString(R.string.step_position_key), position);
            FragmentUtils.addFragment(this, R.id.container_steps, StepDetailsFragment.newInstance(bundle));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.step_position_key), position);
    }

    @Override
    public void onStepSelected(int position) {
        if (steps != null) {
            this.position = position;

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null && !TextUtils.isEmpty(recipeName)) {
                actionBar.setTitle(recipeName);
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(getString(R.string.steps_intent_key), (ArrayList<Step>) steps);
            bundle.putInt(getString(R.string.step_position_key), this.position);
            FragmentUtils.replaceFragment(this, R.id.container_steps, StepDetailsFragment.newInstance(bundle));
        }
    }

}
