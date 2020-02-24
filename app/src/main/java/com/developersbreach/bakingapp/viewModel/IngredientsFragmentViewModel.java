package com.developersbreach.bakingapp.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.ResponseBuilder;

import java.io.IOException;
import java.util.List;

public class IngredientsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<Ingredients>> _mMutableIngredientsList;
    private int mMutableRecipeId;

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

    private void getIngredientsData(final int recipeId) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String responseString = ResponseBuilder.startResponse();
                List<Ingredients> ingredientsList = JsonUtils.fetchIngredients(responseString, recipeId);
                _mMutableIngredientsList.postValue(ingredientsList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
