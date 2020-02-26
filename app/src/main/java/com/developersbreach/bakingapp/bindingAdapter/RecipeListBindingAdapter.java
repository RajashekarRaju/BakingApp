package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;
import com.developersbreach.bakingapp.viewModel.RecipeListFragmentViewModel;

/**
 * BindingAdapter for fragment class {@link RecipeListFragment} with model {@link Recipe} and
 * ViewModel class {@link RecipeListFragmentViewModel}
 */
public class RecipeListBindingAdapter {

    /**
     * When recipeImageView is used on ImageView, the method bindRecipeImageView is called and glide
     * loads the image using URL.
     *
     * @param imageView using Glide library we set an image which needs a URL.
     * @param recipeUrl contains URL to load an image
     */
    @BindingAdapter("recipeImageView")
    public static void bindRecipeImageView(ImageView imageView, String recipeUrl) {
        Glide.with(imageView.getContext())
                .load(recipeUrl)
                .centerCrop()
                .into(imageView);
    }

    /**
     * When recipeName is used on TextView, the method bindRecipeName is called.
     *
     * @param textView   a view which we use to set a String value to it.
     * @param recipeName contains String value to be set to TextView.
     */
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
