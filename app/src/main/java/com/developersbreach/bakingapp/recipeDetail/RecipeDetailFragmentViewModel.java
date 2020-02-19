package com.developersbreach.bakingapp.recipeDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;


public class RecipeDetailFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Recipe> mMutableRecipe;
    private MutableLiveData<ItemLength> mMutableTotalNumber;

    RecipeDetailFragmentViewModel(@NonNull Application application, Recipe recipe) {
        super(application);
        mMutableRecipe = new MutableLiveData<>();
        mMutableRecipe.postValue(recipe);
    }

    MutableLiveData<Recipe> getSelectedRecipe() {
        return mMutableRecipe;
    }

    MutableLiveData<ItemLength> getTotalIngredients(final int recipeId) {
        mMutableTotalNumber = new MutableLiveData<>();
        getTotalNumber(recipeId);
        return mMutableTotalNumber;
    }

    private void getTotalNumber(final int recipeId) {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String uriBuilder = UriBuilder.uriBuilder();
                    URL requestUrl = QueryUtils.createUrl(uriBuilder);
                    String responseString = QueryUtils.getResponseFromHttpUrl(requestUrl);
                    ItemLength result = JsonUtils.findTotalNumber(responseString, recipeId);
                    mMutableTotalNumber.postValue(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
