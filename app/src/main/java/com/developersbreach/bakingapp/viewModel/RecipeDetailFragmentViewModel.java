package com.developersbreach.bakingapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;

import java.io.IOException;


public class RecipeDetailFragmentViewModel extends AndroidViewModel {

    private final MutableLiveData<Recipe> _mMutableRecipe;
    private MutableLiveData<ItemLength> _mMutableTotalNumber;

    public MutableLiveData<Recipe> selectedRecipe() {
        return _mMutableRecipe;
    }

    public MutableLiveData<ItemLength> totalIngredientsAndStepsNumber(int recipeId) {
        getTotalIngredients(recipeId);
        return _mMutableTotalNumber;
    }

    public RecipeDetailFragmentViewModel(@NonNull Application application, Recipe recipe) {
        super(application);
        _mMutableRecipe = new MutableLiveData<>();
        _mMutableRecipe.postValue(recipe);
    }

    private void getTotalIngredients(final int recipeId) {
        _mMutableTotalNumber = new MutableLiveData<>();
        getTotalNumber(recipeId);
    }

    private void getTotalNumber(final int recipeId) {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            try {
                String responseString = ResponseBuilder.startResponse();
                ItemLength result = JsonUtils.fetchTotalNumberJsonData(responseString, recipeId);
                _mMutableTotalNumber.postValue(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
