package com.developersbreach.bakingapp.view.ingredientList;

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
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentIngredientsBinding;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.view.recipeDetail.ChildFragmentPagerAdapter;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;
import com.developersbreach.bakingapp.viewModel.IngredientsFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.factory.IngredientsFragmentViewModelFactory;

import java.util.Objects;

/**
 * This fragment is placed in {@link RecipeDetailFragment} class inside TabLayout with name
 * "Ingredients" and receives arguments from {@link RecipeListFragment} class which is it's ID.
 * <p>
 * This fragment observes data changes from {@link IngredientsFragmentViewModel} class and it's
 * instance is created by factory {@link IngredientsFragmentViewModelFactory} class. Also helps
 * to keep this class clean and no operations are handled here. Also helps to preserve data of the
 * fragment without loosing it's data when user navigates between different destinations or changing
 * orientation of the device.
 * <p>
 * This fragment shows list data using {@link IngredientsAdapter} {@link RecyclerView} with
 * {@link Ingredients} and are bind using {@link DataBindingUtil}
 */
public class IngredientsFragment extends Fragment {

    /**
     * This contains ID received from arguments with class new instance creation.
     * This ID for Recipe selected by user is necessary to get ingredients of type selected by user.
     */
    private int mRecipeId;

    /**
     * This string variable has recipe ID value which receives inside
     * {@link IngredientsFragment#newInstance(int)} of this fragment.
     */
    private static final String EXTRA_RECIPE_ID = "RECIPE_ID";

    /**
     * @param recipeId we need ID for selected recipe by user and this new instance is created from
     *                 adapter {@link ChildFragmentPagerAdapter} class.
     * @return new fragment with arguments.
     */
    public static IngredientsFragment newInstance(int recipeId) {
        Bundle args = new Bundle();
        // Put arguments of type int which is ID and use in onCreate.
        args.putInt(EXTRA_RECIPE_ID, recipeId);
        // Create a new fragment.
        IngredientsFragment fragment = new IngredientsFragment();
        // Set arguments to the fragment before creating.
        fragment.setArguments(args);
        // Return new fragment with arguments.
        return fragment;
    }

    /**
     * It is important to receive arguments for this fragment from newly created instance before
     * calling onCreateView and onActivityCreated.
     * By this approach we make sure we never start query for Ingredients data without proper ID.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check is args are not null to prevent crash.
        if (getArguments() != null) {
            // Assign ID value of recipe to created variable.
            mRecipeId = getArguments().getInt(EXTRA_RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflating fragment layout with DataBinding.
        FragmentIngredientsBinding binding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_ingredients, container, false);

        // Get application context to pass inside ViewModelFactory for this fragment class.
        Application application = Objects.requireNonNull(getActivity()).getApplication();
        // Call factory for creating new instance of ViewModel for this fragment to observe data.
        // Pass application context and recipe ID to the factory.
        IngredientsFragmentViewModelFactory factory =
                new IngredientsFragmentViewModelFactory(application, mRecipeId);
        // Initialize and get class ViewModel and pass fragment owner and factory to create instance by
        // calling ViewModelProviders.
        final IngredientsFragmentViewModel viewModel = new ViewModelProvider(this, factory)
                .get(IngredientsFragmentViewModel.class);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);
        // Giving the binding access to the IngredientsFragmentViewModel
        binding.setViewModel(viewModel);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        return binding.getRoot();
    }
}
