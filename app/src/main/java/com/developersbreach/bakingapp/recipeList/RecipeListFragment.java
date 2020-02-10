package com.developersbreach.bakingapp.recipeList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Recipe;

import java.util.List;

public class RecipeListFragment extends Fragment {

    private RecipeListFragmentViewModel mViewModel;
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecipeRecyclerView = view.findViewById(R.id.recipe_recycler_view);
        Toolbar recipeToolBar = view.findViewById(R.id.recipe_toolbar);
        recipeToolBar.setTitle("Recipe's");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecipeListFragmentViewModel.class);
        mViewModel.getMutableRecipeList().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipeList) {
                mRecipeAdapter = new RecipeAdapter(getContext(), recipeList, new RecipeListener(), mViewModel);
                mRecipeRecyclerView.setAdapter(mRecipeAdapter);
            }
        });
    }

    private class RecipeListener implements RecipeAdapter.RecipeAdapterListener {

        @Override
        public void onRecipeSelected(Recipe recipe, View view) {
            NavDirections direction = RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment(recipe);
            Navigation.findNavController(view).navigate(direction);
        }
    }
}
