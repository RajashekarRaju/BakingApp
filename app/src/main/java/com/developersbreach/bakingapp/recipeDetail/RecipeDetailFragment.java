package com.developersbreach.bakingapp.recipeDetail;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.net.URL;
import java.util.Objects;

public class RecipeDetailFragment extends Fragment {

    public static final String ARG_PARCEL_RECIPE = "ParcelRecipe";
    private RecipeDetailFragmentViewModel mViewModel;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private ImageView mRecipeImageView;
    private ViewPager2 recipeDetailViewPager;

    private TabLayout mTabLayout;
    private String mRecipeName;
    private boolean mTwoPane;
    private Recipe mRecipeTwoPane;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_PARCEL_RECIPE)) {
            mRecipeTwoPane = getArguments().getParcelable(ARG_PARCEL_RECIPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
        }

        setFragmentViews(view);
        setNavButton();
        return view;
    }


    private void setFragmentViews(View binding) {
        mRecipeImageView = binding.findViewById(R.id.detail_image);
        mToolbar = binding.findViewById(R.id.detail_toolbar);
        mCollapsingToolbarLayout = binding.findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout = binding.findViewById(R.id.recipe_detail_appbarLayout);
        mTabLayout = binding.findViewById(R.id.recipe_detail_tabLayout);
        recipeDetailViewPager = binding.findViewById(R.id.recipe_detail_view_pager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!mTwoPane) {

            Application application = Objects.requireNonNull(getActivity()).getApplication();
            Recipe recipe = RecipeDetailFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getRecipeDetailArgs();
            RecipeDetailFragmentViewModelFactory factory = new RecipeDetailFragmentViewModelFactory(application, recipe);
            mViewModel = new ViewModelProvider(this, factory).get(RecipeDetailFragmentViewModel.class);

            mViewModel.getSelectedRecipe().observe(getViewLifecycleOwner(), recipe1 -> {
                mRecipeName = recipe1.getRecipeName();

                int deviceState = getResources().getConfiguration().orientation;
                if (deviceState == Configuration.ORIENTATION_LANDSCAPE) {
                    mToolbar.setTitle(mRecipeName);
                } else if (deviceState == Configuration.ORIENTATION_PORTRAIT) {
                    mToolbar.setTitle("");
                }

                Glide.with(Objects.requireNonNull(getContext()))
                        .load(recipe1.getRecipeImage())
                        .centerCrop()
                        .into(mRecipeImageView);

                createViewPagerWithTabs(recipeDetailViewPager, recipe1.getRecipeId() - 1, mRecipeName);
                setBadges(mTabLayout, recipe1.getRecipeId() - 1);
            });

        } else {

            mToolbar.setTitle(mRecipeTwoPane.getRecipeName());

            Glide.with(Objects.requireNonNull(getContext()))
                    .load(mRecipeTwoPane.getRecipeImage())
                    .centerCrop()
                    .into(mRecipeImageView);

            createViewPagerWithTabs(recipeDetailViewPager, mRecipeTwoPane.getRecipeId() - 1, mRecipeTwoPane.getRecipeName());
            setTabLayoutBadges(mTabLayout, mRecipeTwoPane.getRecipeId() - 1);
        }

        setDetailAppBarLayout();
    }

    private void setTabLayoutBadges(final TabLayout tabLayout, final int recipeId) {

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String uriBuilder = UriBuilder.uriBuilder();
                    URL requestUrl = QueryUtils.createUrl(uriBuilder);
                    String responseString = QueryUtils.getResponseFromHttpUrl(requestUrl);
                    ItemLength result = JsonUtils.findTotalNumber(responseString, recipeId);
                    runOnMainThread(result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void runOnMainThread(final ItemLength itemLength) {
                AppExecutors.getInstance().mainThread().execute(
                        () -> applyBadgeDrawables(itemLength, tabLayout));
            }
        });
    }

    private void setBadges(final TabLayout tabLayout, int recipeId) {
        mViewModel.getTotalIngredients(recipeId).observe(getViewLifecycleOwner(), itemLength ->
                applyBadgeDrawables(itemLength, tabLayout));
    }

    private void applyBadgeDrawables(ItemLength itemLength, TabLayout tabLayout) {

        int ingredientsSize = itemLength.getIngredientsSize();
        int stepSize = itemLength.getStepsSize();

        BadgeDrawable ingredientsBadge = Objects.requireNonNull(tabLayout.getTabAt(0)).getOrCreateBadge();
        ingredientsBadge.setVisible(true);
        ingredientsBadge.setNumber(ingredientsSize);

        BadgeDrawable walkThroughBadge = Objects.requireNonNull(tabLayout.getTabAt(1)).getOrCreateBadge();
        walkThroughBadge.setVisible(true);
        walkThroughBadge.setNumber(stepSize);
    }

    private void createViewPagerWithTabs(ViewPager2 recipeDetailViewPager, int recipeId, String recipeName) {

        ChildFragmentPagerAdapter adapter = new ChildFragmentPagerAdapter(Objects
                .requireNonNull(getActivity()), recipeId, recipeName);
        recipeDetailViewPager.setAdapter(adapter);
        TabLayoutMediator mediator = new TabLayoutMediator(mTabLayout, recipeDetailViewPager, new TabConfiguration());
        mediator.attach();
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
                    mCollapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    private void setNavButton() {
        mToolbar.setNavigationOnClickListener(
                view -> Navigation.findNavController(view).navigateUp());
    }

    private class TabConfiguration implements TabLayoutMediator.TabConfigurationStrategy {

        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            String[] tabNames = new String[]{"Ingredients", "WalkThrough"};
            int[] tabIcons = new int[]{R.drawable.ic_ingredients, R.drawable.ic_walkthrough};
            tab.setText(tabNames[position]).setIcon(tabIcons[position]);
        }
    }
}
