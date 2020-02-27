package com.developersbreach.bakingapp.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;

import java.io.IOException;
import java.util.List;


/**
 * ViewModel responsible for preparing and managing the data for fragment class {@link RecipeListFragment}
 * This ViewModel expose the data RecipeList via {@link LiveData} and fragment observes the changes.
 * <p>
 * This helps us save fragment data and keeps UI simple.
 * This class performs background operations to fetch json data from internet.
 */
public class RecipeListFragmentViewModel extends ViewModel {

    /**
     * This field is encapsulated, we used MutableLiveData because when the data is being changed
     * we will be updating Recipe with new values. And any externally exposed LiveData can observe.
     */
    private MutableLiveData<List<Recipe>> _mMutableRecipeList;

    /**
     * @return Exposed {@link LiveData} object of List of {@link Recipe} externally allowing fragment
     * to observe changes. Data is observed once changes will be done internally.
     */
    public LiveData<List<Recipe>> recipeList() {
        getMutableRecipeList();
        return _mMutableRecipeList;
    }

    /**
     * Create a new {@link MutableLiveData} after checking if is is empty, otherwise no need make
     * changes by adding new values. If is empty start getting new values from JSON in background
     * thread.
     */
    private void getMutableRecipeList() {
        if (_mMutableRecipeList == null) {
            _mMutableRecipeList = new MutableLiveData<>();
            // Call this method to get new values.
            getMutableRecipeData();
        }
    }

    /**
     * Start this operation in new background thread.
     * First get single instance of {@link AppExecutors} and start the background thread to execute.
     *
     * @see JsonUtils#fetchRecipeJsonData(String)
     */
    public void getMutableRecipeData() {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            // Try to start a response to get response string from ResponseBuilder.
            try {
                String responseString = ResponseBuilder.startResponse();
                // Get JSON data which returns list of recipe objects with responseString.
                List<Recipe> recipeList = JsonUtils.fetchRecipeJsonData(responseString);
                // Add list to internally exposed data of recipe list by calling postValue.
                _mMutableRecipeList.postValue(recipeList);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("RecipeListViewModel", "Problem fetching Recipes", e);
            }
        });
    }
}
