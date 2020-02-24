package com.developersbreach.bakingapp.view.recipeList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentRecipeListBinding;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.viewModel.RecipeListFragmentViewModel;

import java.util.Objects;

public class RecipeListFragment extends Fragment {

    private boolean mTwoPane;
    private RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecipeRecyclerView;
    private ConstraintLayout mParentFrameLayout;
    public static String COMPLETED_ANIMATION_PREF_NAME = "ANIMATION_LINEAR_OUT";
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentRecipeListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);

        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
        }

        Toolbar recipeToolBar = binding.recipeToolbar;
        mParentFrameLayout = binding.recipeListContainer;
        mRecipeRecyclerView = binding.recipeRecyclerView;
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(recipeToolBar);

        binding.setLifecycleOwner(this);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeListFragmentViewModel viewModel = new ViewModelProvider(this).get(RecipeListFragmentViewModel.class);
        viewModel.recipeList().observe(getViewLifecycleOwner(), recipeList -> {
            mRecipeAdapter = new RecipeAdapter(recipeList, new RecipeClickListener());
            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_podcasts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(
                Objects.requireNonNull(this.getActivity()), R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private class RecipeClickListener implements RecipeAdapter.RecipeAdapterListener {
        @Override
        public void onRecipeSelected(Recipe recipe, View view) {
            // If crashes replace twoPane from adapter
            if (!mTwoPane) {
                NavDirections direction = RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment(recipe);
                Navigation.findNavController(view).navigate(direction);
            } else {
                Bundle arguments = new Bundle();
                arguments.putParcelable(RecipeDetailFragment.ARG_PARCEL_RECIPE_DUAL_PANE, recipe);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        animateForFirstTimeOnly();
    }

    // Until value in sharedPreference won't effect, we continue showing animation to user.
    private void animateForFirstTimeOnly() {
        // Getting our shared preference in primate mode.
        mSharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        // Check for change in default value, if false returned then no effect happens.
        if (!mSharedPreferences.getBoolean(COMPLETED_ANIMATION_PREF_NAME, false)) {
            startLinearAnimation();
        }
    }

    private void startLinearAnimation() {
        mParentFrameLayout.setTranslationY(800);
        mParentFrameLayout.animate().translationY(0f).setDuration(1000L).start();
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(COMPLETED_ANIMATION_PREF_NAME, true);
        mEditor.apply();
    }
}
