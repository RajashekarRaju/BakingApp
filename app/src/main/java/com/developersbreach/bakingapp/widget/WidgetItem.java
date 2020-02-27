package com.developersbreach.bakingapp.widget;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.bindingAdapter.RecipeListBindingAdapter;
import com.developersbreach.bakingapp.network.JsonUtils;

/**
 * Data class for getting WidgetItems, which returns objects of recipe name property.
 * We pass each property into ArrayList of WidgetItem from class {@link JsonUtils#fetchWidgetJsonData(String)}
 * and these properties will be accessed by binding objects as static fields.
 * <p>
 * The list of properties are set to RecyclerView in class {@link RecipeListBindingAdapter}
 */
public class WidgetItem {

    // Widget property of type String with name of recipe.
    private final String mRecipeName;

    // Class constructor for making data class into reusable objects of items.
    public WidgetItem(String recipeName) {
        this.mRecipeName = recipeName;
    }

    /**
     * @return recipe name string value for binding view in {@link R.layout#item_recipe_widget}
     */
    public String getRecipeName() {
        return mRecipeName;
    }
}
