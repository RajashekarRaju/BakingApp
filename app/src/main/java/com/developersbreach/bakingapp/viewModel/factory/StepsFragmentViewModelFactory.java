package com.developersbreach.bakingapp.viewModel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;
import com.developersbreach.bakingapp.viewModel.IngredientsFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.StepsFragmentViewModel;

/**
 * A AndroidViewModelFactory for creating a instance of {@link StepsFragmentViewModel}
 * AndroidViewModel for fragment class {@link StepsFragment} with a constructor.
 */
public class StepsFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    // Needs to to be passed as parameter for AndroidViewModel class.
    private final Application mApplication;
    // Pass id for selected recipe detail.
    private final int mRecipeId;
    // Pass name for selected recipe detail.
    private final String mRecipeName;

    /**
     * Creates a {@link ViewModelProvider.AndroidViewModelFactory}
     *
     * @param application parameter to pass in {@link AndroidViewModel}
     * @param recipeId    a user selected Recipe object to pass in {@link AndroidViewModel}
     * @param recipeName  a user selected Recipe object to pass in {@link AndroidViewModel}
     */
    public StepsFragmentViewModelFactory(@NonNull Application application, int recipeId, String recipeName) {
        super(application);
        this.mApplication = application;
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
    }

    /**
     * @param modelClass to check if our {@link StepsFragmentViewModel} model class is assignable.
     * @param <T>        type of generic class
     * @return returns the ViewModel class with passing parameters if instance is created.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StepsFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new StepsFragmentViewModel(mApplication, mRecipeId, mRecipeName);
        }
        throw new IllegalArgumentException("Cannot create Instance for StepsFragmentViewModel class");
    }
}
