package com.developersbreach.bakingapp.view.recipeDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;

public class ChildFragmentPagerAdapter extends FragmentStateAdapter {

    private int mRecipeId;
    private String mRecipeName;

    ChildFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, int mRecipeId, String recipeName) {
        super(fragmentActivity);
        this.mRecipeId = mRecipeId;
        this.mRecipeName = recipeName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return IngredientsFragment.newInstance(mRecipeId);
            case 1:
                return StepsFragment.newInstance(mRecipeId, mRecipeName);
        }
        return new IngredientsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}