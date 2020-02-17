package com.developersbreach.bakingapp.ingredient;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentIngredientsBinding;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.utils.DividerItemDecorator;

import java.util.List;
import java.util.Objects;

public class IngredientsFragment extends Fragment {

    private static final String EXTRA_RECIPE_ID = "RECIPE_ID";
    private RecyclerView mIngredientsRecyclerView;
    private IngredientsAdapter mIngredientsAdapter;
    private int mRecipeId;


    public static IngredientsFragment newInstance(int recipeId) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID, recipeId);
        IngredientsFragment fragment = new IngredientsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentIngredientsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);
        mIngredientsRecyclerView = binding.ingredientsRecyclerView;
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(EXTRA_RECIPE_ID);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Application application = Objects.requireNonNull(getActivity()).getApplication();
        IngredientsFragmentViewModelFactory factory = new IngredientsFragmentViewModelFactory(application, mRecipeId);
        final IngredientsFragmentViewModel viewModel = new ViewModelProvider(this, factory).get(IngredientsFragmentViewModel.class);

        viewModel.getMutableIngredientsList().observe(getViewLifecycleOwner(), new Observer<List<Ingredients>>() {
            @Override
            public void onChanged(List<Ingredients> ingredientsList) {
                mIngredientsAdapter = new IngredientsAdapter(getContext(), ingredientsList, viewModel);
                mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
                mIngredientsRecyclerView.addItemDecoration(new DividerItemDecorator(getContext()));
            }
        });
    }

}
