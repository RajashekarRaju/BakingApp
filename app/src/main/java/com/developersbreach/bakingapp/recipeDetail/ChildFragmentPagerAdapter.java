package com.developersbreach.bakingapp.recipeDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.developersbreach.bakingapp.ingredient.IngredientsFragment;
import com.developersbreach.bakingapp.step.StepsFragment;

public class ChildFragmentPagerAdapter extends FragmentStateAdapter {

    private int recipeId;
    private String recipeName;

    ChildFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, int recipe, String name) {
        super(fragmentActivity);
        this.recipeId = recipe;
        this.recipeName = name;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return IngredientsFragment.newInstance(recipeId);
            case 1:
                return StepsFragment.newInstance(recipeId, recipeName);
        }
        return new IngredientsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}