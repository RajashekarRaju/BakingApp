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
import androidx.lifecycle.ViewModelProvider;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentIngredientsBinding;

import java.util.Objects;

public class IngredientsFragment extends Fragment {

    private static final String EXTRA_RECIPE_ID = "RECIPE_ID";
    private int mRecipeId;

    public static IngredientsFragment newInstance(int recipeId) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID, recipeId);
        IngredientsFragment fragment = new IngredientsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(EXTRA_RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentIngredientsBinding binding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_ingredients, container, false);

        Application application = Objects.requireNonNull(getActivity()).getApplication();
        IngredientsFragmentViewModelFactory factory = new IngredientsFragmentViewModelFactory(application, mRecipeId);
        final IngredientsFragmentViewModel viewModel = new ViewModelProvider(this, factory)
                .get(IngredientsFragmentViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

}
