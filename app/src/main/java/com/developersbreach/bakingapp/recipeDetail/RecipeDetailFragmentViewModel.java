package com.developersbreach.bakingapp.recipeDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Recipe;


public class RecipeDetailFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Recipe> mMutableRecipe;

    RecipeDetailFragmentViewModel(@NonNull Application application, Recipe recipe) {
        super(application);
        mMutableRecipe = new MutableLiveData<>();
        mMutableRecipe.postValue(recipe);
    }

    MutableLiveData<Recipe> getSelectedRecipe() {
        return mMutableRecipe;
    }
}
