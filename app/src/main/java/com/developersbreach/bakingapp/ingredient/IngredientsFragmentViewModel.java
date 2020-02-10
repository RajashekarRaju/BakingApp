package com.developersbreach.bakingapp.ingredient;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.ingredient.IngredientsAdapter.IngredientsViewHolder;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;
import java.util.List;

public class IngredientsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<Ingredients>> mMutableIngredientsList;
    private int mMutableRecipeId;

    IngredientsFragmentViewModel(Application application, int recipeId) {
        super(application);
        this.mMutableRecipeId = recipeId;
    }

    MutableLiveData<List<Ingredients>> getMutableIngredientsList() {
        mMutableIngredientsList = new MutableLiveData<>();
        getIngredientsData(mMutableRecipeId);
        return mMutableIngredientsList;
    }

    private void getIngredientsData(final int recipeId) {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String uriBuilder = UriBuilder.uriBuilder();
                    URL requestUrl = QueryUtils.createUrl(uriBuilder);
                    String responseString = QueryUtils.getResponseFromHttpUrl(requestUrl);
                    List<Ingredients> ingredientsList = JsonUtils.fetchIngredients(responseString, recipeId);
                    mMutableIngredientsList.postValue(ingredientsList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void loadIngredientsData(Context context, Ingredients ingredients, IngredientsViewHolder holder) {
        String quantity = ingredients.getIngredientsQuantity();
        String measure = ingredients.getIngredientsMeasure();
        String name = ingredients.getIngredientsName();

        switch (quantity) {
            case "0.5":
                holder.mIngredientQuantityItemTextView.setText("1/2");
                break;
            case "1.5":
                holder.mIngredientQuantityItemTextView.setText("1 1/2");
                break;
            default:
                holder.mIngredientQuantityItemTextView.setText(quantity);
                break;
        }

        switch (measure) {
            case "G":
                holder.mIngredientMeasureItemTextView.setText(R.string.measure_grams);
                break;
            case "CUP":
                holder.mIngredientMeasureItemTextView.setText(R.string.measure_cup);
                break;
            case "TBLSP":
                holder.mIngredientMeasureItemTextView.setText(R.string.measure_table_spoon);
                break;
            case "K":
                holder.mIngredientMeasureItemTextView.setText(R.string.measure_kilogram);
                break;
            case "OZ":
                holder.mIngredientMeasureItemTextView.setText(R.string.measure_ounce);
                break;
            case "TSP":
                holder.mIngredientMeasureItemTextView.setText(R.string.measure_tea_spoon);
                break;
            case "UNIT":
                holder.mIngredientMeasureItemTextView.setText(R.string.measure_no_units);
                break;
            default:
                holder.mIngredientMeasureItemTextView.setText(measure);
                break;
        }

        String formatName = name.substring(0, 1).toUpperCase() + name.substring(1);

        holder.mIngredientNameItemTextView.setText(formatName);
        holder.mIngredientIndicatorItemView.setBackgroundResource(R.drawable.ingredient_indicator);
    }
}
