package com.developersbreach.bakingapp.recipeList;

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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentRecipeListBinding;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.recipeDetail.RecipeDetailFragment;

import java.util.List;
import java.util.Objects;

public class RecipeListFragment extends Fragment {

    private boolean mTwoPane;
    private RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecipeRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentRecipeListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);

        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
        }

        Toolbar recipeToolBar = binding.recipeToolbar;
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
        viewModel.getMutableRecipeList().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipeList) {
                mRecipeAdapter = new RecipeAdapter(recipeList, new RecipeClickListener());
                mRecipeRecyclerView.setAdapter(mRecipeAdapter);
            }
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
}
