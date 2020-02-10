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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.ingredient.IngredientsFragment;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.step.StepsFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class RecipeDetailFragment extends Fragment {

    private RecipeDetailFragmentViewModel mViewModel;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private ImageView mRecipeImageView;
    private String mRecipeName;
    private ViewPager recipeDetailViewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        setFragmentViews(view);

        return view;
    }

    private void setFragmentViews(View view) {
        mRecipeImageView = view.findViewById(R.id.detail_image);
        mToolbar = view.findViewById(R.id.detail_toolbar);
        mCollapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout = view.findViewById(R.id.recipe_detail_appbarLayout);
        TabLayout recipeDetailTabLayout = view.findViewById(R.id.recipe_detail_tabLayout);
        recipeDetailViewPager = view.findViewById(R.id.recipe_detail_view_pager);
        setNavButton();
        setDetailAppBarLayout();
        recipeDetailTabLayout.setupWithViewPager(recipeDetailViewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

                createViewPager(recipeDetailViewPager, recipe.getRecipeId() - 1);
            }
        });
    }

    private void createViewPager(ViewPager recipeDetailViewPager, int recipeId) {
        ChildFragmentPagerAdapter adapter = new ChildFragmentPagerAdapter(getChildFragmentManager());
        adapter.createNewChildFragment(IngredientsFragment.newInstance(recipeId), "Ingredients");
        adapter.createNewChildFragment(StepsFragment.newInstance(recipeId, mRecipeName), "Steps");
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
                    mCollapsingToolbarLayout.setTitle(mRecipeName);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle("");
                    isShow = false;
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
