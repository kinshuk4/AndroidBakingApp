package com.k2udacity.baking.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.k2udacity.baking.R;
import com.k2udacity.baking.ui.fragment.RecipeDetailsFragment;


public final class FragmentUtils {

    private FragmentUtils() {

    }

    public static boolean addFragment(FragmentActivity fragmentActivity, int resourceId, Fragment fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()) != null) {
            return false;
        }
        fragmentManager.beginTransaction()
                .add(
                        resourceId,
                        fragment,
                        fragment.getClass().getSimpleName()
                )
                .commit();
        return true;
    }

    public static boolean replaceFragment(FragmentActivity fragmentActivity, int resourceId, Fragment fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(
                        resourceId,
                        fragment,
                        fragment.getClass().getSimpleName()
                )
                .commit();
        return true;
    }
}
