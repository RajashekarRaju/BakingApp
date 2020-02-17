package com.developersbreach.bakingapp.step;

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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentStepsBinding;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.stepDetail.StepDetailFragmentDirections;

import java.util.List;
import java.util.Objects;

public class StepsFragment extends Fragment {

    private static final String EXTRA_RECIPE_STEPS_ID = "RECIPE_STEPS_ID";
    private static final String EXTRA_RECIPE_STEPS_NAME = "RECIPE_STEPS_NAME";
    private RecyclerView mStepsRecyclerView;
    private StepsAdapter mStepsAdapter;
    private int mRecipeId;
    private String mRecipeName;


    public static StepsFragment newInstance(int recipeId, String recipeName) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_STEPS_ID, recipeId);
        args.putString(EXTRA_RECIPE_STEPS_NAME, recipeName);
        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentStepsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
        mStepsRecyclerView = binding.stepsRecyclerView;
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(EXTRA_RECIPE_STEPS_ID);
            mRecipeName = getArguments().getString(EXTRA_RECIPE_STEPS_NAME);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Application application = Objects.requireNonNull(getActivity()).getApplication();
        StepsFragmentViewModelFactory factory = new StepsFragmentViewModelFactory(application, mRecipeId, mRecipeName);
        final StepsFragmentViewModel viewModel = new ViewModelProvider(this, factory).get(StepsFragmentViewModel.class);

        viewModel.getMutableStepsList().observe(getViewLifecycleOwner(), new Observer<List<Steps>>() {
            @Override
            public void onChanged(List<Steps> stepsList) {
                mStepsAdapter = new StepsAdapter(getContext(), stepsList, new StepClickListener(), viewModel);
                mStepsRecyclerView.setAdapter(mStepsAdapter);
            }
        });
    }

    private class StepClickListener implements StepsAdapter.StepsAdapterListener {

        @Override
        public void onStepSelected(Steps steps, String recipeName, View view) {
            NavDirections directions = StepDetailFragmentDirections.actionGlobalStepDetailFragment(steps, recipeName);
            Navigation.findNavController(view).navigate(directions);
        }
    }
}
