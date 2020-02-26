package com.developersbreach.bakingapp.model;

import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;

/**
 * Data class for getting length of number of ingredients and steps available in each recipe selected
 * by the user, which returns count of array list in JsonArray.
 * We pass each property into ArrayList of Steps from class {@link JsonUtils#fetchTotalNumberJsonData(String, int)}
 * <p>
 * These values will be accessed by {@link RecipeDetailFragment} in TabLayout as BadgeDrawables.
 */
public class ItemLength {

    // Property of type int has length of JsonArray of Ingredients.
    private final int mIngredientsSize;
    // Property of type int has length of JsonArray of Steps.
    private final int mStepsSize;

    // Class constructor for making data class into reusable objects of item lengths.
    public ItemLength(int ingredientsSize, int stepsSize) {
        this.mIngredientsSize = ingredientsSize;
        this.mStepsSize = stepsSize;
    }

    /**
     * @return total number of items in Ingredients.
     */
    public int getIngredientsSize() {
        return mIngredientsSize;
    }

    /**
     * @return total number of items in Steps.
     */
    public int getStepsSize() {
        return mStepsSize;
    }
}
