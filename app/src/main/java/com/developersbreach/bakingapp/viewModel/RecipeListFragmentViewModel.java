package com.developersbreach.bakingapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;

import java.io.IOException;
import java.util.List;


public class RecipeListFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Recipe>> _mMutableRecipeList;

    public MutableLiveData<List<Recipe>> recipeList() {
        getMutableRecipeList();
        return _mMutableRecipeList;
    }

    private void getMutableRecipeList() {
        if (_mMutableRecipeList == null) {
            _mMutableRecipeList = new MutableLiveData<>();
            fetchRecipeJsonData();
        }
    }

    public void fetchRecipeJsonData() {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            try {
                String responseString = ResponseBuilder.startResponse();
                List<Recipe> recipeList = JsonUtils.fetchRecipeJsonData(responseString);
                _mMutableRecipeList.postValue(recipeList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
