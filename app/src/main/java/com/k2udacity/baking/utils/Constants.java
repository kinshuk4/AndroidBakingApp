package com.k2udacity.baking.utils;

import android.net.Uri;

public class Constants {
    public static final String AUTHORITY = "com.k2udacity.baking";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static String RECIPE_ROOT_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
}
