package com.developersbreach.bakingapp.viewModel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.view.stepDetail.StepDetailFragment;
import com.developersbreach.bakingapp.viewModel.RecipeDetailFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.StepDetailFragmentViewModel;

/**
 * A AndroidViewModelFactory for creating a instance of {@link StepDetailFragmentViewModel}
 * AndroidViewModel for fragment class {@link StepDetailFragment} with a constructor.
 */
public class StepDetailFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    // Needs to to be passed as parameter for AndroidViewModel class.
    private final Application mApplication;
    // Parcel model class Recipe as argument.
    private final Steps mStep;
    // Pass argument recipe name to show in detail step fragment.
    private final String mRecipeName;

    /**
     * Creates a {@link ViewModelProvider.AndroidViewModelFactory}
     *
     * @param application parameter to pass in {@link AndroidViewModel}
     * @param step      a user selected Step object to pass in {@link AndroidViewModel}
     * @param recipeName pass parameter name of the recipe
     */
    public StepDetailFragmentViewModelFactory(@NonNull Application application, Steps step, String recipeName) {
        super(application);
        this.mApplication = application;
        this.mStep = step;
        this.mRecipeName = recipeName;
    }

    /**
     * @param modelClass to check if our {@link StepDetailFragmentViewModel} model class is assignable.
     * @param <T>        type of generic class
     * @return returns the ViewModel class with passing parameters if instance is created.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StepDetailFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new StepDetailFragmentViewModel(mApplication, mStep, mRecipeName);
        }
        throw new IllegalArgumentException("Cannot create Instance for StepDetailFragmentViewModel class");
    }
}
