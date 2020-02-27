package com.developersbreach.bakingapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;
import com.developersbreach.bakingapp.viewModel.factory.StepsFragmentViewModelFactory;

import java.io.IOException;
import java.util.List;

/**
 * We are extending with AndroidViewModel instead of ViewModel, because AndroidViewModel is aware of
 * using application context helps to get or access resources we need.
 * <p>
 * This class is responsible for preparing and managing the data for fragment class {@link StepsFragment}
 * This ViewModel expose the data StepsList via {@link LiveData} and fragment observes the changes.
 * <p>
 * This ViewModel instance is created by factory {@link StepsFragmentViewModelFactory}
 * <p>
 * This helps us save fragment data and keeps UI simple.
 * This class performs background operations to fetch json data from internet.
 */
public class StepsFragmentViewModel extends AndroidViewModel {

    /**
     * This field is encapsulated, we used {@link MutableLiveData} because when the data is being
     * changed we will be updating {@link Steps} with new values. And any externally exposed LiveData
     * can observe this changes.
     */
    private MutableLiveData<List<Steps>> _mMutableStepsList;

    /**
     * Get selected recipes steps by recipe id.
     */
    private final int mRecipeId;

    /**
     * Get recipe name for selected recipe to steps by recipe id.
     */
    private final String mRecipeName;

    /**
     * @return Exposed {@link LiveData} object of List of {@link Steps} externally allowing fragment
     * to observe changes. Data is observed once changes will be done internally.
     */
    public MutableLiveData<List<Steps>> stepsList() {
        getMutableStepsList();
        return _mMutableStepsList;
    }

    /**
     * @return name of the recipe from selected recipes with recipe id.
     */
    public String recipeName() {
        return mRecipeName;
    }

    /**
     * @param application provides application context for ViewModel.
     * @param recipeId    get steps by user selected data with recipe id.
     * @param recipeName  get name of the recipe selected by user with recipe id.
     */
    public StepsFragmentViewModel(Application application, int recipeId, String recipeName) {
        super(application);
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
    }

    /**
     * Create a new {@link MutableLiveData} after checking if is is empty, otherwise no need make
     * changes by adding new values. If is empty start getting new values from JSON in background
     * thread.
     */
    private void getMutableStepsList() {
        if (_mMutableStepsList == null) {
            _mMutableStepsList = new MutableLiveData<>();
            // Call this method to get new step values with recipe ID.
            getStepsData(mRecipeId);
        }
    }

    /**
     * Start this operation in new background thread.
     * First get single instance of {@link AppExecutors} and start the background thread to execute.
     *
     * @see JsonUtils#fetchStepsJsonData(String, int)
     */
    public void getStepsData(final int recipeId) {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            // Try to start a response to get response string from ResponseBuilder.
            try {
                String responseString = ResponseBuilder.startResponse();
                // Get JSON data which returns list of steps objects with responseString.
                List<Steps> stepsList = JsonUtils.fetchStepsJsonData(responseString, recipeId);
                // Add list to internally exposed data of steps list by calling postValue.
                _mMutableStepsList.postValue(stepsList);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("StepsFragmentViewModel", "Problem fetching Steps", e);
            }
        });
    }
}
