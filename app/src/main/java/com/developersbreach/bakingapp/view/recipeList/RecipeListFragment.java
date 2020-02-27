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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentRecipeListBinding;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.view.FragmentNoInternet;
import com.developersbreach.bakingapp.view.MainActivity;
import com.developersbreach.bakingapp.view.podcast.PodcastFragment;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.viewModel.RecipeListFragmentViewModel;

import java.util.Objects;


/**
 * First destination in default NavGraph triggers when active network is available.
 * If (NetworkAvailable) --> {@link R.layout#activity_main} --> {@link R.navigation#nav_graph_default}
 *  --> {@link RecipeListFragment}.
 * Else {@link FragmentNoInternet}.
 *
 * This fragment observes data changes from {@link RecipeListFragmentViewModel} class. Also helps
 * to keep this class clean and no operations are handled here. Also helps to preserve data of the
 * fragment without loosing it's data when user navigates between different destinations or changing
 * orientation of the device.
 *
 * Layout and views are bind using {@link DataBindingUtil}
 */
public class RecipeListFragment extends Fragment {

    /**
     * This string used to get boolean value from SharedPreferences by editor whether to show or
     * hide animation for user when this fragment opens.
     */
    private static final String COMPLETED_ANIMATION_PREF_NAME = "ANIMATION_LINEAR_OUT";

    /**
     * Using preferences with editor decide whether to change the default value in string
     * COMPLETED_ANIMATION_PREF_NAME to show or hide the animation.
     */
    private SharedPreferences mSharedPreferences;

    /**
     * This fragment will be initially visible for user which shows a list of recipes using
     * RecyclerView adapter {@link RecipeAdapter}
     */
    private RecyclerView mRecipeRecyclerView;

    /**
     * Adapter for Recipe set to {@link RecyclerView} receives external {@link LiveData} from
     * {@link RecipeListFragmentViewModel}
     */
    private RecipeAdapter mRecipeAdapter;

    /**
     * A parent ViewGroup inside this fragment layout which contains List of Recipes which helps
     * us animate views inside them {@link RecipeListFragment#startLinearAnimation()}
     */
    private ConstraintLayout mParentConstraintLayout;

    /**
     * Check if the device type is tablet or mobile using boolean value.
     * If this fragment opened in tablet device boolean value inside {@link R.bool#isTablet} sw600dp
     * is triggered. Then we decide to show fragment in tablet.
     *
     * Fragment triggers inside {@link R.bool#isTablet} sw600dp ---> Tablet device, mTwoPane = true.
     * Fragment triggers inside {@link R.bool#isTablet} ---> Mobile device, mTwoPane = false.
     */
    private boolean mTwoPane;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflating fragment layout with DataBinding.
        FragmentRecipeListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_list, container, false);

        // Check for device type. If true --> device is Tablet.
        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
        }

        // Get Toolbar and bind with object. This fragment uses separate AppBarLayout.
        Toolbar toolBar = binding.recipeToolbar;
        // Get parent constraint layout which contains views to animate.
        mParentConstraintLayout = binding.recipeListContainer;
        // Get RecyclerView for fragment and bind with variable.
        mRecipeRecyclerView = binding.recipeRecyclerView;
        // Set above declared ToolBar with ActionBar to show in ActionBar by passing it.
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolBar);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);
        // Let the fragment know to inflate Menu in this fragment in onCreate.
        setHasOptionsMenu(true);
        // Return root binding view.
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize this class ViewModel inside this only after onCreate.
        // Get reference to RecipeListFragmentViewModel class with ViewModelProvider by class owner.
        RecipeListFragmentViewModel viewModel =
                new ViewModelProvider(this).get(RecipeListFragmentViewModel.class);
        // With variable viewModel start observing LiveData to this fragment by setting this class
        // owner and get new Observer for list of recipe data.
        // recipeList() is only externally exposed LiveData object in ViewModel class to observe data.
        viewModel.recipeList().observe(getViewLifecycleOwner(), recipeList -> {
            // Pass data to adapter and create new listener for each items in recipe list.
            mRecipeAdapter = new RecipeAdapter(recipeList, new RecipeClickListener());
            // Set adapter to this fragments RecyclerView.
            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        });
    }

    /**
     * Our fragment shows a menu to open {@link PodcastFragment}. Don't add this in {@link MainActivity}
     * if you want to show this menu in this fragment only.
     *
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater inflate layout {@link R.menu#menu_podcasts}
     * @see #setHasOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_podcasts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal processing happen.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Get NavController and find host for this fragment to start a new fragment from here.
        NavController navController = Navigation.findNavController(
                Objects.requireNonNull(this.getActivity()), R.id.nav_host_fragment);
        // Return selected destination with NavController.
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    /**
     * Create listener for items in recipe list by calling interface from {@link RecipeAdapter}.
     */
    private class RecipeClickListener implements RecipeAdapter.RecipeAdapterListener {
        /**
         * @param recipe get recipes from selected list of recipes.
         * @param view   used to create navigation with controller, which needs view.
         */
        @Override
        public void onRecipeSelected(Recipe recipe, View view) {
            // If false, device is Mobile. Then start new destination by calling NavController.
            if (!mTwoPane) {
                // Get directions to navigate to or from fragment using Actions which mapped in
                // NavGraph. This methods are auto-generated by NavigationComponent library only
                // after successful gradle build.
                // Since this actions takes recipe as argument, pass recipe with directions.
                NavDirections direction = RecipeListFragmentDirections
                        .actionRecipeListFragmentToRecipeDetailFragment(recipe);
                // Find NavController with view and navigate to destination using directions.
                Navigation.findNavController(view).navigate(direction);
            } else {
                // If true, device is Tablet. Start FragmentTransaction without NavigationComponent.
                // Pass bundle for arguments, we pass Recipe objects.
                Bundle arguments = new Bundle();
                // Store bundle value with reference in String type.
                arguments.putParcelable(RecipeDetailFragment.ARG_PARCEL_RECIPE_TWO_PANE, recipe);
                // Start new Fragment.
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                // Set arguments for fragment we are adding.
                fragment.setArguments(arguments);
                // Begin fragment transaction and replace new fragment in container ad commit.
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            }
        }
    }

    /**
     * We don't want to show animation for user every time this fragment opens. So call method
     * inside onResume to avoid this behavior.
     */
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
            // Starts th animation with the properties we set.
            startLinearAnimation();
        }
    }

    /**
     * If boolean COMPLETED_ANIMATION_PREF_NAME value is true we start animation else false.
     */
    private void startLinearAnimation() {
        // Occurs transition from bottom to top at 800dp.
        mParentConstraintLayout.setTranslationY(800);
        // Animate with duration, height.
        mParentConstraintLayout.animate().translationY(0f).setDuration(1000L).start();
        // After finishing the animation,change boolean value inside String with editor.
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(COMPLETED_ANIMATION_PREF_NAME, true);
        mEditor.apply();
    }
}
