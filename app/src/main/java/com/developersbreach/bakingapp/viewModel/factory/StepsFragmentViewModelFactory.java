package com.developersbreach.bakingapp.viewModel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.developersbreach.bakingapp.viewModel.StepsFragmentViewModel;

public class StepsFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;
    private final int mRecipeId;
    private final String mRecipeName;

    public StepsFragmentViewModelFactory(@NonNull Application application, int recipeId, String recipeName) {
        super(application);
        this.mApplication = application;
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StepsFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new StepsFragmentViewModel(mApplication, mRecipeId, mRecipeName);
        }
        throw new IllegalArgumentException("Cannot create Instance for this class StepsFragmentViewModel");
    }
}
