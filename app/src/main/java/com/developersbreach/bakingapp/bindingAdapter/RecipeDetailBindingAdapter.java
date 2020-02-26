package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.viewModel.RecipeDetailFragmentViewModel;

/**
 * BindingAdapter for fragment class {@link RecipeDetailFragment} and ViewModel class
 * {@link RecipeDetailFragmentViewModel}
 */
public class RecipeDetailBindingAdapter {

    /**
     * When recipeDetailImageView is used on ImageView, the method recipeDetailImageView is called
     * and glide loads the image using URL.
     *
     * @param imageView using Glide library we set an image which needs a URL.
     * @param recipeUrl contains URL to load an image
     */
    @BindingAdapter("recipeDetailImageView")
    public static void bindRecipeDetailImageView(ImageView imageView, String recipeUrl) {
        Glide.with(imageView.getContext())
                .load(recipeUrl)
                .centerCrop()
                .into(imageView);
    }
}
