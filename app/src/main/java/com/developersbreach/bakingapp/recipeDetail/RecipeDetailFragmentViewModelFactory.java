package com.developersbreach.bakingapp.recipeDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.developersbreach.bakingapp.model.Recipe;

public class RecipeDetailFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;
    private final Recipe mRecipe;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public RecipeDetailFragmentViewModelFactory(@NonNull Application application, Recipe recipe) {
        super(application);
        this.mApplication = application;
        this. mRecipe = recipe;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeDetailFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeDetailFragmentViewModel(mApplication, mRecipe);
        }
        throw new IllegalArgumentException("Cannot create Instance for this class");
    }
}
