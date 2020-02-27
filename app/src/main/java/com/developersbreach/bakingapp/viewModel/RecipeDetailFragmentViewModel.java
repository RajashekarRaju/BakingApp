package com.developersbreach.bakingapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;
import com.developersbreach.bakingapp.viewModel.factory.RecipeDetailFragmentViewModelFactory;

import java.io.IOException;

/**
 * We are extending with AndroidViewModel instead of ViewModel, because AndroidViewModel is aware of
 * using application context helps to get or access resources we need.
 * <p>
 * This class is responsible for preparing and managing the data for fragment class
 * {@link RecipeDetailFragment}
 * This ViewModel expose the data RecipeDetails via {@link LiveData} and fragment observes the changes.
 * <p>
 * This ViewModel instance is created by factory {@link RecipeDetailFragmentViewModelFactory}
 * <p>
 * This helps us save fragment data and keeps UI simple.
 * This class performs background operations to fetch json data from internet.
 */
public class RecipeDetailFragmentViewModel extends AndroidViewModel {

    /**
     * This field is encapsulated, we used {@link MutableLiveData} because when the data is being
     * changed we will be updating StepsDetail with new values. And any externally exposed LiveData
     * can observe this changes.
     */
    private MutableLiveData<Recipe> _mMutableRecipe;

    /**
     * @see ItemLength, this data class with two integers return count of {@link Ingredients} and
     * {@link Steps} by getting there length of ArrayList.
     * <p>
     * This field is encapsulated, we used {@link MutableLiveData} because when the data is being
     * changed we will be updating {@link ItemLength} with new values. And any externally exposed
     * LiveData can observe this changes.
     */
    private MutableLiveData<ItemLength> _mMutableTotalNumber;

    /**
     * @return Exposed {@link LiveData} object of RecipeDetails of {@link Recipe} externally allowing
     * fragment to observe changes. Data is observed once changes will be done internally.
     */
    public LiveData<Recipe> selectedRecipe() {
        return _mMutableRecipe;
    }

    /**
     * @return Exposed {@link LiveData} object of List of {@link ItemLength} externally allowing
     * fragment to observe changes. Data is observed once changes will be done internally.
     * <p>
     * Getting recipe id of user selected and get data from JSON for total count of ingredients
     * and steps by their ArrayList.
     */
    public LiveData<ItemLength> totalIngredientsAndStepsNumber(int recipeId) {
        getTotalIngredients(recipeId);
        return _mMutableTotalNumber;
    }

    /**
     * @param application provides application context for ViewModel.
     * @param recipe      parcel Recipe object with data for user selected recipe from
     *                    {@link RecipeListFragment}
     */
    public RecipeDetailFragmentViewModel(@NonNull Application application, Recipe recipe) {
        super(application);
        getMutableRecipeDetailsData(recipe);
    }

    /**
     * Create a new {@link MutableLiveData} after checking if is is empty, otherwise no need make
     * changes by adding new values.
     *
     * @param recipe has data for user selected Recipe with id.
     */
    private void getMutableRecipeDetailsData(Recipe recipe) {
        if (_mMutableRecipe == null) {
            _mMutableRecipe = new MutableLiveData<>();
            // Add list to internally exposed data of RecipeDetails by calling postValue.
            _mMutableRecipe.postValue(recipe);
        }
    }

    /**
     * @param recipeId get count for Ingredients and Steps list by passing user selected recipe ID
     */
    private void getTotalIngredients(final int recipeId) {
        if (_mMutableTotalNumber == null) {
            _mMutableTotalNumber = new MutableLiveData<>();
            // Call this method to get new values with id.
            getTotalNumber(recipeId);
        }
    }

    /**
     * Start this operation in new background thread.
     * First get single instance of {@link AppExecutors} and start the background thread to execute.
     *
     * @param recipeId pass id from user selected position.
     * @see JsonUtils#fetchTotalNumberJsonData(String, int)
     */
    private void getTotalNumber(final int recipeId) {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            // Try to start a response to get response string from ResponseBuilder.
            try {
                String responseString = ResponseBuilder.startResponse();
                // Get JSON data which returns count of ArrayList of Ingredients and Steps objects
                // with responseString.
                ItemLength itemLength = JsonUtils.fetchTotalNumberJsonData(responseString, recipeId);
                // Add values to internally exposed data of ItemLength by calling postValue.
                _mMutableTotalNumber.postValue(itemLength);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("RecipeDetailViewModel", "Problem fetching RecipeDetails", e);
            }
        });
    }
}
