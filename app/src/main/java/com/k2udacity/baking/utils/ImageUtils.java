package com.k2udacity.baking.utils;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.k2udacity.baking.R;
import com.squareup.picasso.Picasso;

public class ImageUtils {

    public static void setImage(Context context, String imageUrl, ImageView imageView, int defaultDrawableId){
        if (!TextUtils.isEmpty(imageUrl)) {
            Uri uri = Uri.parse(imageUrl);
            Picasso.with(context).load(uri).into(imageView);
        } else {
            Picasso.with(context).load(defaultDrawableId).into(imageView);
        }
    }
}
