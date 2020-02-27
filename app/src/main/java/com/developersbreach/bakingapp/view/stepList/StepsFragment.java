package com.developersbreach.bakingapp.view.stepList;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentStepsBinding;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.view.recipeDetail.ChildFragmentPagerAdapter;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;
import com.developersbreach.bakingapp.view.stepDetail.StepDetailFragment;
import com.developersbreach.bakingapp.view.stepDetail.StepDetailFragmentDirections;
import com.developersbreach.bakingapp.viewModel.StepsFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.factory.StepDetailFragmentViewModelFactory;
import com.developersbreach.bakingapp.viewModel.factory.StepsFragmentViewModelFactory;

import java.util.Objects;

/**
 * This fragment is placed in {@link RecipeDetailFragment} class inside TabLayout with name
 * "WalkThrough" and receives arguments from {@link RecipeListFragment} class which is it's ID.
 * <p>
 * This fragment observes data changes from {@link StepsFragmentViewModel} class and it's
 * instance is created by factory {@link StepDetailFragmentViewModelFactory} class. Also helps
 * to keep this class clean and no operations are handled here. Also helps to preserve data of the
 * fragment without loosing it's data when user navigates between different destinations or changing
 * orientation of the device.
 * <p>
 * This fragment shows list data using {@link StepsAdapter} {@link RecyclerView} with {@link Steps}
 * and are bind using {@link DataBindingUtil}
 */
public class StepsFragment extends Fragment {

    /**
     * This contains ID received from arguments with class new instance creation.
     * This ID for Recipe selected by user is necessary to get steps of type selected by user.
     */
    private int mRecipeId;

    /**
     * This contains name of the recipe from arguments with class new instance creation.
     * This name for Recipe selected by user is necessary to get recipe of type selected by user.
     * This will be passed as argument again to {@link StepDetailFragment} ActionBar Title.
     */
    private String mRecipeName;

    /**
     * Creates list data of type {@link Steps} with {@link RecyclerView}.
     */
    private RecyclerView mStepsRecyclerView;

    /**
     * Adapter for Recipe set to {@link RecyclerView} receives external {@link LiveData} from
     * {@link StepsFragmentViewModel}
     */
    private StepsAdapter mStepsAdapter;

    /**
     * This string variable has recipe ID value which receives inside
     * {@link StepsFragment#newInstance(int, String)} of this fragment.
     */
    private static final String EXTRA_RECIPE_STEPS_ID = "RECIPE_STEPS_ID";

    /**
     * This string variable has recipe name value which receives inside
     * {@link StepsFragment#newInstance(int, String)} of this fragment.
     */
    private static final String EXTRA_RECIPE_STEPS_NAME = "RECIPE_STEPS_NAME";

    /**
     * @param recipeId   we need ID for selected recipe by user and this new instance is created from
     *                   adapter {@link ChildFragmentPagerAdapter} class.
     * @param recipeName we need name to set this Title in ActionBar in {@link StepDetailFragment}
     *                   class
     * @return new fragment with arguments.
     */
    public static StepsFragment newInstance(int recipeId, String recipeName) {
        Bundle args = new Bundle();
        // Put arguments of type int which is ID and use in onCreate.
        args.putInt(EXTRA_RECIPE_STEPS_ID, recipeId);
        // Put arguments of type String which is name and use in onCreate.
        args.putString(EXTRA_RECIPE_STEPS_NAME, recipeName);
        // Create a new fragment.
        StepsFragment fragment = new StepsFragment();
        // Set arguments to the fragment before creating.
        fragment.setArguments(args);
        // Return new fragment with arguments.
        return fragment;
    }

    /**
     * It is important to receive arguments for this fragment from newly created instance before
     * calling onCreateView and onActivityCreated.
     * By this approach we make sure we never start query for Steps data without proper ID.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check is args are not null to prevent crash.
        if (getArguments() != null) {
            // Assign ID value of recipe to created variable.
            mRecipeId = getArguments().getInt(EXTRA_RECIPE_STEPS_ID);
            // Assign name value of recipe to created variable.
            mRecipeName = getArguments().getString(EXTRA_RECIPE_STEPS_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflating fragment layout with DataBinding.
        FragmentStepsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps,
                container, false);
        // Get recyclerView reference and bind
        mStepsRecyclerView = binding.stepsRecyclerView;
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get application context to pass inside ViewModelFactory for this fragment class.
        Application application = Objects.requireNonNull(getActivity()).getApplication();
        // Call factory for creating new instance of ViewModel for this fragment to observe data.
        // Pass application context recipe ID and name to the factory.
        StepsFragmentViewModelFactory factory =
                new StepsFragmentViewModelFactory(application, mRecipeId, mRecipeName);
        // Initialize and get class ViewModel and pass fragment owner and factory to create instance
        // by calling ViewModelProviders.
        final StepsFragmentViewModel viewModel =
                new ViewModelProvider(this, factory).get(StepsFragmentViewModel.class);
        // With variable viewModel start observing LiveData to this fragment by setting this class
        // owner and get new Observer for list of steps data.
        // stepsList() is only externally exposed LiveData object in ViewModel class to observe data.
        viewModel.stepsList().observe(getViewLifecycleOwner(), stepsList -> {
            // Pass data to adapter and create new listener for each items in steps list.
            mStepsAdapter = new StepsAdapter(stepsList, new StepClickListener(), viewModel);
            // Set adapter to this fragments RecyclerView.
            mStepsRecyclerView.setAdapter(mStepsAdapter);
        });
    }

    /**
     * Create listener for items in steps list by calling interface from {@link StepsAdapter}.
     */
    private static class StepClickListener implements StepsAdapter.StepsAdapterListener {
        /**
         * @param steps      get step data by position clicked to return step details.
         * @param recipeName get name of the recipe which user currently viewing.
         * @param view       using view to find navigation controller for navigating.
         */
        @Override
        public void onStepSelected(Steps steps, String recipeName, View view) {
            // Get directions to navigate to or from fragment using Actions which mapped in
            // NavGraph. This methods are auto-generated by NavigationComponent library only
            // after successful gradle build.
            // Since this actions takes recipe as argument, pass step and recipe name with directions.
            NavDirections directions =
                    StepDetailFragmentDirections.actionGlobalStepDetailFragment(steps, recipeName);
            // Find NavController with view and navigate to destination using directions.
            Navigation.findNavController(view).navigate(directions);
        }
    }
}
