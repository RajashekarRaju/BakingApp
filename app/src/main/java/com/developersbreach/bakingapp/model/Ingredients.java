package com.developersbreach.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {

    private final String mIngredientsQuantity;
    private final String mIngredientsMeasure;
    private final String mIngredientsName;

    public Ingredients(String quantity, String measure, String name) {
        this.mIngredientsQuantity = quantity;
        this.mIngredientsMeasure = measure;
        this.mIngredientsName = name;
    }

    public String getIngredientsQuantity() {
        return mIngredientsQuantity;
    }

    public String getIngredientsMeasure() {
        return mIngredientsMeasure;
    }

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
