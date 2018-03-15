package com.k2udacity.baking.contract;

import android.net.Uri;

public class RecipeContract {

    private static final String AUTHORITY = "com.k2udacity.baking";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
}
