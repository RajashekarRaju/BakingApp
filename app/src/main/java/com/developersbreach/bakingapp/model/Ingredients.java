package com.developersbreach.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.developersbreach.bakingapp.bindingAdapter.IngredientsListBindingAdapter;
import com.developersbreach.bakingapp.network.JsonUtils;

import static com.developersbreach.bakingapp.R.*;

/**
 * Data class for getting Ingredients, which returns objects of ingredient properties.
 * We pass each property into ArrayList of Ingredients from {@link JsonUtils#fetchIngredientsJsonData(String, int)}
 * and these properties will be accessed by binding objects as static fields.
 * <p>
 * The list of properties are set to RecyclerView in class {@link IngredientsListBindingAdapter}
 */
public class Ingredients implements Parcelable {

    // Ingredient property of type string has value quantity.
    private final String mIngredientsQuantity;
    // Ingredient property of type string has value measure.
    private final String mIngredientsMeasure;
    // Ingredient property of type string has value name of ingredient.
    private final String mIngredientsName;

    // Class constructor for making data class into reusable objects of ingredients.
    public Ingredients(String quantity, String measure, String name) {
        this.mIngredientsQuantity = quantity;
        this.mIngredientsMeasure = measure;
        this.mIngredientsName = name;
    }

    /**
     * @return quantity string value for binding view in {@link layout#item_ingredient}
     */
    public String getIngredientsQuantity() {
        return mIngredientsQuantity;
    }

    /**
     * @return measure string value for binding view in {@link layout#item_ingredient}
     */
    public String getIngredientsMeasure() {
        return mIngredientsMeasure;
    }

    /**
     * @return ingredient string value for binding view in {@link layout#item_ingredient}
     */
    public String getIngredientsName() {
        return mIngredientsName;
    }

    private Ingredients(Parcel in) {
        mIngredientsQuantity = in.readString();
        mIngredientsMeasure = in.readString();
        mIngredientsName = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
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
        dest.writeString(mIngredientsQuantity);
        dest.writeString(mIngredientsMeasure);
        dest.writeString(mIngredientsName);
    }
}
