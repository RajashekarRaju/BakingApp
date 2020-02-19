package com.developersbreach.bakingapp.recipeList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;
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
                try {
                    String uriBuilder = UriBuilder.uriBuilder();
                    URL requestUrl = QueryUtils.createUrl(uriBuilder);
                    String responseString = QueryUtils.getResponseFromHttpUrl(requestUrl);
                    List<Recipe> recipeList = JsonUtils.fetchRecipeJsonData(responseString);
                    mMutableRecipeList.postValue(recipeList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
