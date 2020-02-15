package com.developersbreach.bakingapp.recipeDetail;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.ingredient.IngredientsFragment;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.step.StepsFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class RecipeDetailFragment extends Fragment {

    public static final String ARG_PARCEL_RECIPE = "ParcelRecipe";
    private RecipeDetailFragmentViewModel mViewModel;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private ImageView mRecipeImageView;
    private ViewPager recipeDetailViewPager;

    private TabLayout mTabLayout;

    private String mRecipeName;
    private boolean mTwoPane;
    private Recipe mRecipeTwoPane;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_PARCEL_RECIPE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipeTwoPane = getArguments().getParcelable(ARG_PARCEL_RECIPE);
            if (mRecipeTwoPane != null) {
                Log.e("Tablet", mRecipeTwoPane.getRecipeName());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        if (view.findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        setFragmentViews(view);
        return view;
    }

    private void setFragmentViews(View view) {
        mRecipeImageView = view.findViewById(R.id.detail_image);
        mToolbar = view.findViewById(R.id.detail_toolbar);
        mCollapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout = view.findViewById(R.id.recipe_detail_appbarLayout);
        mTabLayout = view.findViewById(R.id.recipe_detail_tabLayout);
        recipeDetailViewPager = view.findViewById(R.id.recipe_detail_view_pager);
        setNavButton();
        setDetailAppBarLayout();
        mTabLayout.setupWithViewPager(recipeDetailViewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!mTwoPane) {

            Application application = Objects.requireNonNull(getActivity()).getApplication();
            Recipe recipe = RecipeDetailFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getRecipeDetailArgs();
            RecipeDetailFragmentViewModelFactory factory = new RecipeDetailFragmentViewModelFactory(application, recipe);
            mViewModel = new ViewModelProvider(this, factory).get(RecipeDetailFragmentViewModel.class);

            mViewModel.getSelectedRecipe().observe(getViewLifecycleOwner(), new Observer<Recipe>() {
                @Override
                public void onChanged(Recipe recipe) {
                    mRecipeName = recipe.getRecipeName();

                    int deviceState = getResources().getConfiguration().orientation;
                    if (deviceState == Configuration.ORIENTATION_LANDSCAPE) {
                        mToolbar.setTitle(mRecipeName);
                    } else if (deviceState == Configuration.ORIENTATION_PORTRAIT) {
                        mToolbar.setTitle("");
                    }

                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(recipe.getRecipeImage())
                            .centerCrop()
                            .into(mRecipeImageView);

                    createViewPagerSinglePane(recipeDetailViewPager, recipe.getRecipeId() - 1);

                    Objects.requireNonNull(mTabLayout.getTabAt(0)).setIcon(R.drawable.ic_ingredients);
                    Objects.requireNonNull(mTabLayout.getTabAt(1)).setIcon(R.drawable.ic_walkthrough);

                    setBadges(mTabLayout, recipe.getRecipeId() - 1);
                }
            });

        } else if (mTwoPane){

            mToolbar.setTitle(mRecipeTwoPane.getRecipeName());

            Glide.with(Objects.requireNonNull(getContext()))
                    .load(mRecipeTwoPane.getRecipeImage())
                    .centerCrop()
                    .into(mRecipeImageView);

            createViewPagerDualPane(recipeDetailViewPager, mRecipeTwoPane.getRecipeId() - 1);

            Objects.requireNonNull(mTabLayout.getTabAt(0)).setIcon(R.drawable.ic_ingredients);
            Objects.requireNonNull(mTabLayout.getTabAt(1)).setIcon(R.drawable.ic_walkthrough);

            setBadges(mTabLayout, mRecipeTwoPane.getRecipeId()- 1);
        }
    }

    private void setBadges(final TabLayout tabLayout, int recipeId) {

        mViewModel.getTotalIngredients(recipeId).observe(getViewLifecycleOwner(), new Observer<ItemLength>() {
            @Override
            public void onChanged(ItemLength itemLength) {

                BadgeDrawable ingredientsBadge = tabLayout.getTabAt(0).getOrCreateBadge();
                ingredientsBadge.setVisible(true);
                ingredientsBadge.setNumber(itemLength.getIngredientsSize());

                BadgeDrawable walkThroughBadge = tabLayout.getTabAt(1).getOrCreateBadge();
                walkThroughBadge.setVisible(true);
                walkThroughBadge.setNumber(itemLength.getStepsSize());
            }
        });
    }

    private void createViewPagerDualPane(ViewPager recipeDetailViewPager, int recipeId) {
        ChildFragmentPagerAdapter adapter = new ChildFragmentPagerAdapter(getChildFragmentManager());
        adapter.createNewChildFragment(IngredientsFragment.newInstance(recipeId), "Ingredients");
        adapter.createNewChildFragment(StepsFragment.newInstance(recipeId, mRecipeTwoPane.getRecipeName()), "WalkThrough");
        recipeDetailViewPager.setAdapter(adapter);
    }

    private void createViewPagerSinglePane(ViewPager recipeDetailViewPager, int recipeId) {
        ChildFragmentPagerAdapter adapter = new ChildFragmentPagerAdapter(getChildFragmentManager());
        adapter.createNewChildFragment(IngredientsFragment.newInstance(recipeId), "Ingredients");
        adapter.createNewChildFragment(StepsFragment.newInstance(recipeId, mRecipeName), "WalkThrough");
        recipeDetailViewPager.setAdapter(adapter);
    }

    private void setDetailAppBarLayout() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mTwoPane) {
                        mCollapsingToolbarLayout.setTitle(mRecipeTwoPane.getRecipeName());
                        isShow = true;
                    } else {
                        mCollapsingToolbarLayout.setTitle(mRecipeName);
                        isShow = true;
                    }
                } else if (isShow) {
                    if (mTwoPane) {
                        mCollapsingToolbarLayout.setTitle("");
                        isShow = false;
                    } else {
                        mCollapsingToolbarLayout.setTitle("");
                        isShow = false;
                    }
                }
            }
        });
    }

    private void setNavButton() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }
}
