package com.developersbreach.bakingapp.model;

public class ItemLength {

    private int mIngredientsSize;
    private int mStepsSize;

    public ItemLength(int ingredientsSize, int stepsSize) {
        this.mIngredientsSize = ingredientsSize;
        this.mStepsSize = stepsSize;
    }

    public int getIngredientsSize() {
        return mIngredientsSize;
    }

    public int getStepsSize() {
        return mStepsSize;
    }
}
