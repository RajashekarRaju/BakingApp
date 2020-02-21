package com.developersbreach.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Recipe implements Parcelable {

    private final int mRecipeId;
    // Name of the sandwich
    private final String mRecipeName;
    // Image of the sandwich
    private final String mRecipeImage;


    public Recipe(int recipeId, String recipeName, String recipeImage) {
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
        this.mRecipeImage = recipeImage;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

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
