package com.developersbreach.bakingapp;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingAdapters {


    @BindingAdapter("recipeImageView")
    public static void bindRecipeImageView(ImageView imageView, String recipeUrl) {
        Glide.with(imageView.getContext())
                .load(recipeUrl)
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter("recipeName")
    public static void bindRecipeName(TextView textView, String recipeName) {
        if ("".equals(recipeName)) {
            // If value not available we set something appropriate to it.
            textView.setText(textView.getContext().getString(R.string.name_not_available));
        } else {
            // If value is valid, show it to user
            textView.setText(recipeName);
        }
    }
}
