package com.developersbreach.bakingapp.recipeDetail;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.ResponseBuilder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.Objects;

public class RecipeDetailFragment extends Fragment {

    private FragmentRecipeDetailBinding mBinding;
    private RecipeDetailFragmentViewModel mViewModel;

    public static final String ARG_PARCEL_RECIPE_DUAL_PANE = "ParcelRecipe";
    private boolean mDualPane;
    private Recipe mRecipeDualPane;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private ViewPager2 mDetailViewPager;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_PARCEL_RECIPE_DUAL_PANE)) {
            mRecipeDualPane = getArguments().getParcelable(ARG_PARCEL_RECIPE_DUAL_PANE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        if (getResources().getBoolean(R.bool.isTablet)) {
            mDualPane = true;
        }
        bindFragmentViews(mBinding);
        mBinding.setLifecycleOwner(this);
        setNavButton();
        return mBinding.getRoot();
    }

    private void bindFragmentViews(FragmentRecipeDetailBinding binding) {
        mToolbar = binding.detailToolbar;
        mCollapsingToolbarLayout = binding.collapsingToolbarLayout;
        mAppBarLayout = binding.recipeDetailAppbarLayout;
        mTabLayout = binding.recipeDetailTabLayout;
        mDetailViewPager = binding.recipeDetailViewPager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!mDualPane) {

            Bundle args = Objects.requireNonNull(getArguments());
            Activity activity = Objects.requireNonNull(getActivity());
            Application application = activity.getApplication();
            Recipe recipeArgs = RecipeDetailFragmentArgs.fromBundle(args).getRecipeDetailArgs();
            RecipeDetailFragmentViewModelFactory factory = new RecipeDetailFragmentViewModelFactory(application, recipeArgs);
            mViewModel = new ViewModelProvider(this, factory).get(RecipeDetailFragmentViewModel.class);
            mViewModel.selectedRecipe().observe(getViewLifecycleOwner(), recipe ->
                    setSinglePaneData(recipe, mToolbar, mBinding, mDetailViewPager, mTabLayout));

        } else {
            setDualPaneData(mRecipeDualPane, mToolbar, mDetailViewPager, mTabLayout);
        }
    }

    private void setSinglePaneData(Recipe recipe, Toolbar toolbar, FragmentRecipeDetailBinding binding,
                                   ViewPager2 viewPager, TabLayout tabLayout) {

        String recipeName = recipe.getRecipeName();
        int recipeId = recipe.getRecipeId() - 1;

        binding.setRecipeDetail(recipe);
        binding.executePendingBindings();

        setToolbarConfigure(toolbar, recipeName, getResources().getConfiguration().orientation);
        setAppBarLayout(recipeName, mCollapsingToolbarLayout, mAppBarLayout);
        createViewPagerWithTabs(viewPager, recipeId, recipeName);
        setBadgeDrawablesSinglePane(tabLayout, recipeId);
    }

    private void setDualPaneData(Recipe recipe, Toolbar toolbar, ViewPager2 viewPager, TabLayout tabLayout) {

        String recipeName = recipe.getRecipeName();
        int recipeId = recipe.getRecipeId() - 1;

        toolbar.setTitle(recipeName);
        setAppBarLayout(recipeName, mCollapsingToolbarLayout, mAppBarLayout);
        createViewPagerWithTabs(viewPager, recipeId, recipeName);
        setBadgeDrawablesDualPane(tabLayout, recipeId);
    }

    private void setToolbarConfigure(Toolbar toolbar, String recipeName, int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toolbar.setTitle(recipeName);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            toolbar.setTitle(getResources().getString(R.string.empty_toolbar_title));
        }
    }

    private void setAppBarLayout(String recipeName, CollapsingToolbarLayout collapsingToolbarLayout,
                                 AppBarLayout appBarLayout) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mDualPane) {
                        collapsingToolbarLayout.setTitle(recipeName);
                        isShow = true;
                    } else {
                        collapsingToolbarLayout.setTitle(recipeName);
                        isShow = true;
                    }
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.empty_toolbar_title));
                    isShow = false;
                }
            }
        });
    }

    private void createViewPagerWithTabs(ViewPager2 viewPager, int recipeId, String recipeName) {

        ChildFragmentPagerAdapter adapter = new ChildFragmentPagerAdapter(Objects
                .requireNonNull(getActivity()), recipeId, recipeName);
        viewPager.setAdapter(adapter);
        TabLayoutMediator mediator = new TabLayoutMediator(mTabLayout, viewPager, new TabConfiguration());
        mediator.attach();
    }

    private void setBadgeDrawablesSinglePane(final TabLayout tabLayout, int recipeId) {
        mViewModel.totalIngredientsAndStepsNumber(recipeId).observe(getViewLifecycleOwner(), itemLength ->
                applyBadgeDrawables(itemLength, tabLayout));
    }

    private void setBadgeDrawablesDualPane(final TabLayout tabLayout, final int recipeId) {

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseString = ResponseBuilder.startResponse();
                    ItemLength result = JsonUtils.findTotalNumber(responseString, recipeId);
                    runOnMainThread(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void runOnMainThread(final ItemLength itemLength) {
                AppExecutors.getInstance().mainThread().execute(
                        () -> applyBadgeDrawables(itemLength, tabLayout));
            }
        });
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

    private void setNavButton() {
        mToolbar.setNavigationOnClickListener(
                view -> Navigation.findNavController(view).navigateUp());
    }

    private class TabConfiguration implements TabLayoutMediator.TabConfigurationStrategy {

        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            String[] tabNames = new String[]{
                    getResources().getString(R.string.tab_title_ingredients),
                    getResources().getString(R.string.tab_name_walk_thorugh)
            };

            int[] tabIcons = new int[]{
                    R.drawable.ic_ingredients,
                    R.drawable.ic_walkthrough
            };

            tab.setText(tabNames[position]).setIcon(tabIcons[position]);
        }
    }
}
