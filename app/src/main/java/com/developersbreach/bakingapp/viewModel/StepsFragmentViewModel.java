package com.developersbreach.bakingapp.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;

import java.io.IOException;
import java.util.List;

public class StepsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<Steps>> _mMutableStepsList;
    private final String _mMutableRecipeName;
    private final int mMutableRecipeId;

    public MutableLiveData<List<Steps>> stepsList() {
        getMutableStepsList();
        return _mMutableStepsList;
    }

    public String recipeName() {
        return _mMutableRecipeName;
    }

    public StepsFragmentViewModel(Application application, int recipeId, String recipeName) {
        super(application);
        this.mMutableRecipeId = recipeId;
        this._mMutableRecipeName = recipeName;
    }

    private void getMutableStepsList() {
        _mMutableStepsList = new MutableLiveData<>();
        getStepsData(mMutableRecipeId);
    }

    private void getStepsData(final int recipeId) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String responseString = ResponseBuilder.startResponse();
                List<Steps> stepsList = JsonUtils.fetchSteps(responseString, recipeId);
                _mMutableStepsList.postValue(stepsList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
