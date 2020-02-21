package com.developersbreach.bakingapp.step;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.ResponseBuilder;

import java.util.List;

public class StepsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<Steps>> mMutableStepsList;
    private int mMutableRecipeId;
    private String mMutableRecipeName;

    StepsFragmentViewModel(Application application, int recipeId, String recipeName) {
        super(application);
        this.mMutableRecipeId = recipeId;
        this.mMutableRecipeName = recipeName;
    }

    MutableLiveData<List<Steps>> getMutableStepsList() {
        mMutableStepsList = new MutableLiveData<>();
        getStepsData(mMutableRecipeId);
        return mMutableStepsList;
    }

    String getMutableRecipeName() {
        return mMutableRecipeName;
    }

    private void getStepsData(final int recipeId) {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String responseString = ResponseBuilder.startResponse();
                List<Steps> stepsList = JsonUtils.fetchSteps(responseString, recipeId);
                mMutableStepsList.postValue(stepsList);
            }
        });
    }
}
