package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class RecipeDetailBindingAdapter {

    @BindingAdapter("recipeDetailImageView")
    public static void bindRecipeDetailImageView(ImageView imageView, String recipeUrl) {
        Glide.with(imageView.getContext())
                .load(recipeUrl)
                .centerCrop()
                .into(imageView);
    }
}
