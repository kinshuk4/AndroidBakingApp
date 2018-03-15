package com.k2udacity.baking.ui.activity;

import android.content.Context;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.k2udacity.baking.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.Intents.intended;
import static com.k2udacity.baking.utils.TestConstant.ACTION_BAR;
import static com.k2udacity.baking.utils.TestConstant.BROWNIES;
import static com.k2udacity.baking.utils.TestConstant.NUTELLA_PIE;
import static com.k2udacity.baking.utils.TestConstant.BROWNIES_STEP_ONE_SHORT_DESCRIPTION;
import static com.k2udacity.baking.utils.TestConstant.BROWNIES_STEP_ONE_DESCRIPTION;
import static com.k2udacity.baking.utils.TestConstant.BROWNIES_STEP_TWO_DESCRIPTION;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {
    private Context context;
    private boolean tablet;

    @Before
    public void beforeTest() {
        context = mainActivityTestRule.getActivity();
        tablet = context.getResources().getBoolean(R.bool.isTablet);
        Intents.init();
    }

    @After
    public void afterTest() {
        Intents.release();
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> recipeDetailsActivityTestRule =
            new ActivityTestRule<>(RecipeDetailsActivity.class);

//    @Rule
//    public IntentsTestRule<RecipeDetailsActivity> intentsTestRule = new IntentsTestRule<>(
//            RecipeDetailsActivity.class);

    @Test
    public void clickedRecyclerViewItemHasIntentWithRecipeKey() {
        clickOnRecipeList(1);
        intended(hasExtraWithKey("intent_recipe"));
    }

    @Test
    public void clickOnStepDisplaysCorrectStepShortDescription() {
        clickOnRecipeList(1);
        clickOnStepList(0);

        onView(allOf(isDescendantOfA(withResourceName(ACTION_BAR)), withText(BROWNIES)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.textview_step_description)).check(matches(withText(BROWNIES_STEP_ONE_SHORT_DESCRIPTION)));
    }


    @Test
    public void clickOnNextButtonInTabletShouldDisplayNextStepView() {
        if (tablet) {
            clickOnRecipeList(0);
            clickOnStepList(0);
            onView(withId(R.id.button_next)).perform(click());

            onView(allOf(isDescendantOfA(withResourceName(ACTION_BAR)), withText(NUTELLA_PIE)))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.textview_step_description)).check(matches(withText(BROWNIES_STEP_TWO_DESCRIPTION)));
        }
    }

    @Test
    public void clickOnPreviousButtonInTabletViewShouldDisplayPreviousStepView() {
        if (tablet) {
            clickOnRecipeList(0);
            clickOnStepList(1);
            onView(withId(R.id.button_prev)).perform(click());

            onView(allOf(isDescendantOfA(withResourceName(ACTION_BAR)), withText(NUTELLA_PIE)))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.textview_step_description)).check(matches(withText(BROWNIES_STEP_ONE_DESCRIPTION)));
        }
    }

    @Test
    public void clickOnPlayerPauseButtonInTabletViewDisplaysPlayButton() {
        if (tablet) {
            onView(withId(R.id.recyclerview_recipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            clickOnStepList(0);
            onView(withId(R.id.exo_pause))
                    .perform(click());
            onView(withId(R.id.exo_play))
                    .check(matches(isDisplayed()));
        }
    }

    private void clickOnStepList(int index) {
        onView(withId(R.id.recyclerview_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));
    }

    private void clickOnRecipeList(int index) {
        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));
    }


}
