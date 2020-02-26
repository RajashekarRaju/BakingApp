package com.developersbreach.bakingapp.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;

import java.io.IOException;
import java.util.List;

public class IngredientsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<Ingredients>> _mMutableIngredientsList;
    private final int mMutableRecipeId;

    public MutableLiveData<List<Ingredients>> ingredientsList() {
        return _mMutableIngredientsList;
    }

    public IngredientsFragmentViewModel(Application application, int recipeId) {
        super(application);
        this.mMutableRecipeId = recipeId;
        getMutableIngredientsList();
    }

    private void getMutableIngredientsList() {
        _mMutableIngredientsList = new MutableLiveData<>();
        getIngredientsData(mMutableRecipeId);
    }

    public void getIngredientsData(final int recipeId) {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            try {
                String responseString = ResponseBuilder.startResponse();
                List<Ingredients> ingredientsList = JsonUtils.fetchIngredientsJsonData(responseString, recipeId);
                _mMutableIngredientsList.postValue(ingredientsList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
