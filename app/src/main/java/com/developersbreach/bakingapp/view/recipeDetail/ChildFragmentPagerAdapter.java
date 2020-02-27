package com.developersbreach.bakingapp.view.recipeDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;


/**
 * Adapter class which shows fragments inside ViewPager2 with Tabs.
 * For this behaviour extension of class to {@link FragmentStateAdapter} is necessary.
 */
public class ChildFragmentPagerAdapter extends FragmentStateAdapter {

    /**
     * RecipeId is passed into {@link IngredientsFragment#newInstance(int)} and also class
     * {@link StepsFragment#newInstance(int, String)} whenever it creates a new instance.
     */
    private final int mRecipeId;

    /**
     * RecipeName is passed into {@link StepsFragment#newInstance(int, String)} whenever it creates
     * a new instance.
     */
    private final String mRecipeName;

    /**
     * @param fragmentActivity gets support-based fragment as base activity.
     * @param recipeId         create a recipeId for each instance on fragment called.
     * @param recipeName       create a recipeName for each instance on fragment called.
     */
    ChildFragmentPagerAdapter(FragmentActivity fragmentActivity, int recipeId, String recipeName) {
        super(fragmentActivity);
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
    }

    /**
     * Provide a new Fragment associated with the specified position.
     * <p>
     * The adapter will be responsible for the Fragment lifecycle:
     * <ul>
     *     <li>The Fragment will be used to display an item.</li>
     *     <li>The Fragment will be destroyed when it gets too far from the viewport, and its state
     *     will be saved. When the item is close to the viewport again, a new Fragment will be
     *     requested, and a previously saved state will be used to initialize it.
     * </ul>
     *
     * @param position create or add fragment based on the position.
     * @see ViewPager2#setOffscreenPageLimit
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return IngredientsFragment.newInstance(mRecipeId);
            case 1:
                return StepsFragment.newInstance(mRecipeId, mRecipeName);
        }
        return IngredientsFragment.newInstance(mRecipeId);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}