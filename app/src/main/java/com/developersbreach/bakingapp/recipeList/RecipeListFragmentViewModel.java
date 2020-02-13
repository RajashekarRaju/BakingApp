package com.developersbreach.bakingapp.recipeList;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.recipeList.RecipeAdapter.RecipeViewHolder;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;
import java.util.List;


public class RecipeListFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Recipe>> mMutableRecipeList;

    MutableLiveData<List<Recipe>> getMutableRecipeList() {
        if (mMutableRecipeList == null) {
            mMutableRecipeList = new MutableLiveData<>();
            fetchJsonData();
        }
        return mMutableRecipeList;
    }

    private void fetchJsonData() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String uriBuilder = UriBuilder.uriBuilder();
                    URL requestUrl = QueryUtils.createUrl(uriBuilder);
                    String responseString = QueryUtils.getResponseFromHttpUrl(requestUrl);
                    List<Recipe> recipeList = JsonUtils.fetchRecipeJsonData(responseString);
                    mMutableRecipeList.postValue(recipeList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void loadRecipeProperties(final Context context, Recipe recipe, final RecipeViewHolder holder) {
        // First check whether sandwich has a value before showing it to user.
        if (recipe.getRecipeName().equals("") || recipe.getRecipeName() == null) {
            // If value not available we set something appropriate to it.
            holder.mRecipeNameTextItemView.setText(context.getString(R.string.name_not_available));
        } else {
            // If value is valid, show it to user
            holder.mRecipeNameTextItemView.setText(recipe.getRecipeName());
        }
    }

    void loadRecipeThumbnails(Context context, Recipe recipe, RecipeViewHolder holder) {

        Glide.with(context)
                .load(recipe.getRecipeImage())
                .centerCrop()
                .into(holder.mRecipeImageItemView);
    }
}
