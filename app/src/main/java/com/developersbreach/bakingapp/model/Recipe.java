package com.developersbreach.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.bindingAdapter.RecipeListBindingAdapter;
import com.developersbreach.bakingapp.network.JsonUtils;

/**
 * Data class for getting Recipes, which returns objects of recipe properties.
 * We pass each property into ArrayList of Recipes from class {@link JsonUtils} and these
 * properties will be accessed by binding objects as static fields.
 * <p>
 * The list of properties are set to RecyclerView in class {@link RecipeListBindingAdapter}
 */
public class Recipe implements Parcelable {

    // Recipe property of type int with unique recipe id.
    private final int mRecipeId;
    // Recipe property of type String with name of recipe.
    private final String mRecipeName;
    // Recipe property of type String which has URL to get thumbnail from VideoURL.
    private final String mRecipeImage;

    // Class constructor for making data class into reusable objects of recipes.
    public Recipe(int recipeId, String recipeName, String recipeImage) {
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
        this.mRecipeImage = recipeImage;
    }

    /**
     * @return recipeId int value for passing as an argument to load specific ingredients and recipes.
     */
    public int getRecipeId() {
        return mRecipeId;
    }

    /**
     * @return recipe name string value for binding view in {@link R.layout#item_recipe}
     */
    public String getRecipeName() {
        return mRecipeName;
    }

    /**
     * @return URL string value for binding view suing Glide library in {@link R.layout#item_ingredient}
     */
    public String getRecipeImage() {
        return mRecipeImage;
    }

    private Recipe(Parcel in) {
        mRecipeId = in.readInt();
        mRecipeName = in.readString();
        mRecipeImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRecipeId);
        dest.writeString(mRecipeName);
        dest.writeString(mRecipeImage);
    }
}
