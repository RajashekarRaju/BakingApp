package com.developersbreach.bakingapp.viewModel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;

import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.viewModel.IngredientsFragmentViewModel;

/**
 * A AndroidViewModelFactory for creating a instance of {@link IngredientsFragmentViewModel}
 * AndroidViewModel for fragment class {@link IngredientsFragment} with a constructor.
 */
public class IngredientsFragmentViewModelFactory extends AndroidViewModelFactory {

    // Needs to to be passed as parameter for AndroidViewModel class.
    private final Application mApplication;
    // Pass id for selected recipe detail.
    private final int mRecipeId;

    /**
     * Creates a {@link AndroidViewModelFactory}
     *
     * @param application parameter to pass in {@link AndroidViewModel}
     * @param recipeId    a user selected Recipe object to pass in {@link AndroidViewModel}
     */
    public IngredientsFragmentViewModelFactory(@NonNull Application application, int recipeId) {
        super(application);
        this.mApplication = application;
        this.mRecipeId = recipeId;
    }

    /**
     * @param modelClass to check if our {@link IngredientsFragmentViewModel} model class is assignable.
     * @param <T>        type of generic class
     * @return returns the ViewModel class with passing parameters if instance is created.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(IngredientsFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new IngredientsFragmentViewModel(mApplication, mRecipeId);
        }
        throw new IllegalArgumentException("Cannot create Instance for IngredientsFragmentViewModel class ");
    }
}
