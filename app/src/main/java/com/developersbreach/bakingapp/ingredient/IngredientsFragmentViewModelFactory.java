package com.developersbreach.bakingapp.ingredient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class IngredientsFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;
    private final int mRecipeId;

    public IngredientsFragmentViewModelFactory(@NonNull Application application, int recipeId) {
        super(application);
        this.mApplication = application;
        this.mRecipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(IngredientsFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new IngredientsFragmentViewModel(mApplication, mRecipeId);
        }
        throw new IllegalArgumentException("Cannot create Instance for this class");
    }
}
