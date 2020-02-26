package com.developersbreach.bakingapp.viewModel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.viewModel.IngredientsFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.RecipeDetailFragmentViewModel;

/**
 * A AndroidViewModelFactory for creating a instance of {@link RecipeDetailFragmentViewModel}
 * AndroidViewModel for fragment class {@link RecipeDetailFragment} with a constructor.
 */
public class RecipeDetailFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    // Needs to to be passed as parameter for AndroidViewModel class.
    private final Application mApplication;
    // Parcel model class Recipe as argument.
    private final Recipe mRecipe;

    /**
     * Creates a {@link ViewModelProvider.AndroidViewModelFactory}
     *
     * @param application parameter to pass in {@link AndroidViewModel}
     * @param recipe      a user selected Recipe object to pass in {@link AndroidViewModel}
     */
    public RecipeDetailFragmentViewModelFactory(@NonNull Application application, Recipe recipe) {
        super(application);
        this.mApplication = application;
        this.mRecipe = recipe;
    }

    /**
     * @param modelClass to check if our {@link RecipeDetailFragmentViewModel} model class is assignable.
     * @param <T>        type of generic class
     * @return returns the ViewModel class with passing parameters if instance is created.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeDetailFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeDetailFragmentViewModel(mApplication, mRecipe);
        }
        throw new IllegalArgumentException("Cannot create Instance for RecipeDetailFragmentViewModel class");
    }
}
