package com.developersbreach.bakingapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;
import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.viewModel.factory.IngredientsFragmentViewModelFactory;

import java.io.IOException;
import java.util.List;

/**
 * We are extending with AndroidViewModel instead of ViewModel, because AndroidViewModel is aware of
 * using application context helps to get or access resources we need.
 * <p>
 * This class is responsible for preparing and managing the data for fragment class {@link IngredientsFragment}
 * This ViewModel expose the data IngredientsList via {@link LiveData} and fragment observes the changes.
 * <p>
 * This ViewModel instance is created by factory {@link IngredientsFragmentViewModelFactory}
 * <p>
 * This helps us save fragment data and keeps UI simple.
 * This class performs background operations to fetch json data from internet.
 */
public class IngredientsFragmentViewModel extends AndroidViewModel {

    /**
     * This field is encapsulated, we used {@link MutableLiveData} because when the data is being changed
     * we will be updating Ingredients with new values. And any externally exposed LiveData can observe
     * this changes.
     */
    private MutableLiveData<List<Ingredients>> _mMutableIngredientsList;

    /**
     * Get selected recipes ingredients by recipe id.
     */
    private final int mRecipeId;

    /**
     * @return Exposed {@link LiveData} object of List of {@link Ingredients} externally allowing fragment
     * to observe changes. Data is observed once changes will be done internally.
     */
    public LiveData<List<Ingredients>> ingredientsList() {
        return _mMutableIngredientsList;
    }

    /**
     * @param application provides application context for ViewModel.
     * @param recipeId    get ingredients by user selected data with id.
     */
    public IngredientsFragmentViewModel(Application application, int recipeId) {
        super(application);
        this.mRecipeId = recipeId;
        getMutableIngredientsList();
    }

    /**
     * Create a new {@link MutableLiveData} after checking if is is empty, otherwise no need make
     * changes by adding new values. If is empty start getting new values from JSON in background
     * thread.
     */
    private void getMutableIngredientsList() {
        if (_mMutableIngredientsList == null) {
            _mMutableIngredientsList = new MutableLiveData<>();
            // Call this method to get new values with recipe ID.
            getIngredientsData(mRecipeId);
        }
    }

    /**
     * Start this operation in new background thread.
     * First get single instance of {@link AppExecutors} and start the background thread to execute.
     *
     * @see JsonUtils#fetchIngredientsJsonData(String, int)
     */
    public void getIngredientsData(final int recipeId) {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            // Try to start a response to get response string from ResponseBuilder.
            try {
                String responseString = ResponseBuilder.startResponse();
                // Get JSON data which returns list of ingredient objects with responseString.
                List<Ingredients> ingredientsList = JsonUtils.fetchIngredientsJsonData(responseString, recipeId);
                // Add list to internally exposed data of ingredient list by calling postValue.
                _mMutableIngredientsList.postValue(ingredientsList);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("IngredientsViewModel", "Problem fetching Ingredients", e);
            }
        });
    }
}
