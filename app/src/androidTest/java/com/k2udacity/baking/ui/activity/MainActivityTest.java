package com.k2udacity.baking.ui.activity;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.k2udacity.baking.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.k2udacity.baking.utils.TestConstant.ACTION_BAR;
import static com.k2udacity.baking.utils.TestConstant.BROWNIES;
import static com.k2udacity.baking.utils.TestConstant.NUTELLA_PIE;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);



    @Before
    public void getContext() {
        context = activityRule.getActivity();
    }

    @Test
    public void checkRecipesRecyclerViewIsDisplayed(){
        onView(withId(R.id.recyclerview_recipes)).check(matches(isDisplayed()));
        ViewInteraction textView = onView(allOf(withId(R.id.textview_recipe_name),
                withText(NUTELLA_PIE)));
        textView.check(matches(withText(NUTELLA_PIE)));
//        onData(withId(R.id.textview_recipe_name)).inAdapterView(withId(R.id.recyclerview_recipes)).check(matches(isDisplayed()));
    }


    @Test
    public void clickedRecipeViewItemHasRightRecipeOnActionBar() {
        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(allOf(isDescendantOfA(withResourceName(ACTION_BAR)), withText(BROWNIES)))
                .check(matches(isDisplayed()));
    }
}
