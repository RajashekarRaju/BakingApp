package com.developersbreach.bakingapp.recipeList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.ResponseBuilder;

import java.util.List;


public class RecipeListFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Recipe>> mMutableRecipeList;

    MutableLiveData<List<Recipe>> getMutableRecipeList() {
        if (mMutableRecipeList == null) {
            mMutableRecipeList = new MutableLiveData<>();
            fetchRecipeJsonData();
        }
        return mMutableRecipeList;
    }

    private void fetchRecipeJsonData() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String responseString = ResponseBuilder.startResponse();
                List<Recipe> recipeList = JsonUtils.fetchRecipeJsonData(responseString);
                mMutableRecipeList.postValue(recipeList);
            }
        });
    }


}
