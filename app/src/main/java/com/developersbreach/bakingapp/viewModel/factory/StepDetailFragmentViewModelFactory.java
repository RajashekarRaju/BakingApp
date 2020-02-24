package com.developersbreach.bakingapp.viewModel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.viewModel.StepDetailFragmentViewModel;

public class StepDetailFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private Application mApplication;
    private Steps mStep;
    private String mRecipeName;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link StepDetailFragmentViewModel}
     */
    public StepDetailFragmentViewModelFactory(@NonNull Application application, Steps step, String recipeName) {
        super(application);
        this.mApplication = application;
        this.mStep = step;
        this.mRecipeName = recipeName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StepDetailFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new StepDetailFragmentViewModel(mApplication, mStep, mRecipeName);
        }
        throw new IllegalArgumentException("Cannot create Instance for this class StepDetailFragmentViewModel");
    }
}
