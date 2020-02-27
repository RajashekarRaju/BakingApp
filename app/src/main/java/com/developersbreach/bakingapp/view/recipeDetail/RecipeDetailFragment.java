package com.developersbreach.bakingapp.view.recipeDetail;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;
import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;
import com.developersbreach.bakingapp.viewModel.RecipeDetailFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.factory.RecipeDetailFragmentViewModelFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.Objects;


/**
 * This fragment is second destination in default NavGraph opens when user selects a recipe from
 * {@link RecipeListFragment} class with list.
 * <p>
 * This supports MasterDetailFragment behaviour which shows list of recipes {@link RecipeListFragment}
 * class to the left and Details of the recipe with {@link RecipeDetailFragment} class to right.
 * <p>
 * This fragment observes data changes from {@link RecipeDetailFragmentViewModel} class and it's
 * instance is created by factory {@link RecipeDetailFragmentViewModelFactory} class. Also helps
 * to keep this class clean and no operations are handled here. Also helps to preserve data of the
 * fragment without loosing it's data when user navigates between different destinations or changing
 * orientation of the device.
 * <p>
 * This fragment shows details of recipe data using a {@link ViewPager2} with two {@link TabLayout}
 * which are fragments with titles "Ingredients" and "WalkThrough".
 * position 1 --> {@link IngredientsFragment}
 * position 2 --> {@link StepsFragment}
 */
public class RecipeDetailFragment extends Fragment {

    /**
     * Declare binding variable to give access for DataBinding to bind layout and views.
     */
    private FragmentRecipeDetailBinding mBinding;

    /**
     * Declare {@link ViewModel} for this class to get observe data which is externally exposed.
     */
    private RecipeDetailFragmentViewModel mViewModel;

    /**
     * Check if the device type is tablet or mobile using boolean value.
     * If this fragment opened in tablet device boolean value inside {@link R.bool#isTablet} sw600dp
     * is triggered. Then we decide to show fragment in tablet.
     * <p>
     * Fragment triggers inside {@link R.bool#isTablet} sw600dp ---> Tablet device, mTwoPane = true.
     * Fragment triggers inside {@link R.bool#isTablet} ---> Mobile device, mTwoPane = false.
     */
    private boolean mTwoPane;

    /**
     * This is used only when arguments are being passed for tablet devices.
     * If it is Mobile device, the arguments will be received from ViewModel and factory itself.
     * <p>
     * This contains Recipe details received as arguments from string ARG_PARCEL_RECIPE_TWO_PANE.
     * This data for Recipe selected by user to show.
     */
    private Recipe mRecipeTwoPane;

    /**
     * This string variable has arguments recipe value which we will receive in this class onCreate.
     */
    public static final String ARG_PARCEL_RECIPE_TWO_PANE = "ParcelRecipeTwoPane";

    /**
     * Declare a toolbar, this fragment uses separate AppBarLayout to show custom ToolBar.
     */
    private Toolbar mToolbar;

    /**
     * Declare collapsingToolbar to show ImageView in background which collapse while scrolling.
     */
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    /**
     * Declare a appBarLayout which handles functionality of this fragments CollapsingToolbar and
     * Toolbar with background recipe ImageView.
     */
    private AppBarLayout mAppBarLayout;

    /**
     * Declare {@link ViewPager2} to support Detail fragment to show TabLayout with swipable
     * fragments with titles.
     * <p>
     * {@link ViewPager} has been deprecated.
     */
    private ViewPager2 mDetailViewPager;

    /**
     * Declare {@link TabLayout} which syncs with {@link ViewPager2}.
     * Make use of {@link TabLayoutMediator} object to show tab icons and titles.
     */
    private TabLayout mTabLayout;

    /**
     * It is important to receive arguments for this fragment from newly created instance before
     * calling onCreateView and onActivityCreated.
     * By this approach we make sure we never start query for Recipe data without proper arguments.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if args are not null to prevent crash.
        if (getArguments() != null && getArguments().containsKey(ARG_PARCEL_RECIPE_TWO_PANE)) {
            // Assign parcelable value of recipe to created object recipe.
            mRecipeTwoPane = getArguments().getParcelable(ARG_PARCEL_RECIPE_TWO_PANE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflating fragment layout with DataBinding.
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container,
                false);
        // Check for device type. If true --> device is Tablet.
        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
        }
        // Get reference to all views from layout for binding.
        bindFragmentViews(mBinding);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        mBinding.setLifecycleOwner(this);
        // Set up-button in ActionBar in onCreate to navigate back.
        setNavButton();
        // Return root binding view.
        return mBinding.getRoot();
    }

    /**
     * @param binding variable which can bind all views to this fragment.
     */
    private void bindFragmentViews(FragmentRecipeDetailBinding binding) {
        mToolbar = binding.detailToolbar;
        mCollapsingToolbarLayout = binding.collapsingToolbarLayout;
        mAppBarLayout = binding.recipeDetailAppbarLayout;
        mTabLayout = binding.recipeDetailTabLayout;
        mDetailViewPager = binding.recipeDetailViewPager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // First check device type for the type of arguments from where to receive.
        // If tablet device, get data fom argument mRecipeTwoPane object.
        // If Mobile device, get data from ViewModel and factory itself.
        if (!mTwoPane) {
            // Create a nwe bundle to receive arguments.
            Bundle args = Objects.requireNonNull(getArguments());
            // Get this activity reference as non-null.
            Activity activity = Objects.requireNonNull(getActivity());
            // Get application context for this class for factory to create instance of ViewModel.
            Application application = activity.getApplication();
            // After passing arguments with name and type of value in NavGraph, it is necessary to
            // perform a gradle build for android studio to auto-generate all required classes and
            // objects for this class using NavigationComponent library.
            // get arguments with name and receive arguments from bundle.
            Recipe recipeArgs = RecipeDetailFragmentArgs.fromBundle(args).getRecipeDetailArgs();
            // Call factory for creating new instance of ViewModel for this fragment to observe data.
            // Pass application context and recipe object to the factory.
            RecipeDetailFragmentViewModelFactory factory =
                    new RecipeDetailFragmentViewModelFactory(application, recipeArgs);
            // Assign and get class ViewModel and pass fragment owner and factory to create instance
            // by calling ViewModelProviders.
            mViewModel = new ViewModelProvider(this, factory)
                    .get(RecipeDetailFragmentViewModel.class);
            // With variable viewModel start observing LiveData to this fragment by setting this
            // class owner and get new Observer for recipe details data.
            // selectedRecipe() is only externally exposed LiveData object in ViewModel class to
            // observe data.
            mViewModel.selectedRecipe().observe(getViewLifecycleOwner(), recipe ->
                    // Get all required data for mobile data with arguments we pass to methods below.
                    setSinglePaneData(recipe, mToolbar, mBinding, mDetailViewPager, mTabLayout));

        } else {
            // Since device is tablet get arguments from mRecipeTwoPane object and set layouts.
            setDualPaneData(mRecipeTwoPane, mToolbar, mDetailViewPager, mTabLayout);
        }
    }

    /**
     * For Mobile devices.
     *
     * @param recipe    variable which has access to all data necessary for this fragment class.
     * @param toolbar   set toolbar with custom behaviour.
     * @param binding   get access from variable to bind or get views from binding views.
     * @param viewPager attach viewpager with {@link ChildFragmentPagerAdapter} and create fragment
     *                  instances with arguments for {@link IngredientsFragment} {@link StepsFragment}
     * @param tabLayout add title and icons using {@link TabLayoutMediator}
     */
    private void setSinglePaneData(Recipe recipe, Toolbar toolbar, FragmentRecipeDetailBinding binding,
                                   ViewPager2 viewPager, TabLayout tabLayout) {
        // Variable with name of the recipe.
        String recipeName = recipe.getRecipeName();
        // Variable with recipe id and force to start from required index.
        int recipeId = recipe.getRecipeId() - 1;
        // get reference to binding item views from layout and pass object for recipe data.
        binding.setRecipeDetail(recipe);
        // Force binding to execute immediately all views.
        binding.executePendingBindings();
        // Method call which sets toolbar for this fragment with passing required arguments.
        setToolbarConfigure(toolbar, recipeName, getResources().getConfiguration().orientation);
        // Method call which sets AppBarLayout for this fragment with passing required arguments.
        setAppBarLayout(recipeName, mCollapsingToolbarLayout, mAppBarLayout);
        // Method call which creates ViewPager2 with tabs for this fragment with passing required
        // arguments.
        createViewPagerWithTabs(viewPager, recipeId, recipeName);
        // Create BadgeDrawables for TabLayouts which shows number of Ingredients and Steps.
        setBadgeDrawablesSinglePane(tabLayout, recipeId);
    }

    /**
     * For Tablet devices.
     *
     * @param recipe    variable which has access to all data necessary for this fragment class.
     * @param toolbar   set toolbar with custom behaviour.
     * @param viewPager attach viewpager with {@link ChildFragmentPagerAdapter} and create fragment
     *                  instances with arguments for {@link IngredientsFragment} {@link StepsFragment}
     * @param tabLayout add title and icons using {@link TabLayoutMediator}
     */
    private void setDualPaneData(Recipe recipe, Toolbar toolbar, ViewPager2 viewPager,
                                 TabLayout tabLayout) {
        // Variable with name of the recipe.
        String recipeName = recipe.getRecipeName();
        // Variable with recipe id and force to start from required index.
        int recipeId = recipe.getRecipeId() - 1;
        // Set toolbar to show appropriate title for respective recipe user selected.
        toolbar.setTitle(recipeName);
        // Method call which sets AppBarLayout for this fragment with passing required arguments.
        setAppBarLayout(recipeName, mCollapsingToolbarLayout, mAppBarLayout);
        // Method call which creates ViewPager2 with tabs for this fragment with passing required
        // arguments.
        createViewPagerWithTabs(viewPager, recipeId, recipeName);
        // Create BadgeDrawables for TabLayouts which shows number of Ingredients and Steps.
        setBadgeDrawablesDualPane(tabLayout, recipeId);
    }

    /**
     * For Mobile Devices.
     *
     * @param toolbar     changes behaviour depending on mobile orientation.
     *                    If orientation = Portrait --> show title of recipe.
     *                    If orientation = Landscape --> Don't show title.
     * @param recipeName  sets the name of the recipe for toolbar.
     * @param orientation get type of device orientation.
     */
    private void setToolbarConfigure(Toolbar toolbar, String recipeName, int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toolbar.setTitle(recipeName);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            toolbar.setTitle(getResources().getString(R.string.empty_toolbar_title));
        }
    }

    /**
     * For Mobile device types. Because collapse behaviour is removed for tablet device.
     *
     * @param recipeName              sets the name of the recipe for CollapsingToolbarLayout.
     * @param collapsingToolbarLayout changes behaviour depending on scroll position.
     *                                If position = 0 --> Show Title, hide ImageView.
     *                                If position != 0 --> Hide Title, show ImageView.
     * @param appBarLayout            with listener get state of collapse behaviour to set different values.
     */
    private void setAppBarLayout(String recipeName, CollapsingToolbarLayout collapsingToolbarLayout,
                                 AppBarLayout appBarLayout) {
        // Using a listener to get state of CollapsingToolbar and Toolbar to set properties.
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    // Show title until layout won't collapse.
                    if (mTwoPane) {
                        collapsingToolbarLayout.setTitle(recipeName);
                        isShow = true;
                    } else {
                        collapsingToolbarLayout.setTitle(recipeName);
                        isShow = true;
                    }
                } else if (isShow) {
                    // When completely scrolled remove title.
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.empty_toolbar_title));
                    isShow = false;
                }
            }
        });
    }

    /**
     * @param viewPager  attach viewpager with {@link ChildFragmentPagerAdapter} and pass arguments
     *                   for {@link IngredientsFragment} {@link StepsFragment}
     * @param recipeId   pass Id to adapter for creating ViewPagerFragments instance with arguments.
     * @param recipeName pass name to adapter for creating ViewPagerFragments instance with arguments.
     */
    private void createViewPagerWithTabs(ViewPager2 viewPager, int recipeId, String recipeName) {
        // Get adapter and pass required arguments.
        ChildFragmentPagerAdapter adapter = new ChildFragmentPagerAdapter(
                getActivity(), recipeId, recipeName);
        // Set ViewPager to use this adapter.
        viewPager.setAdapter(adapter);
        // Object which helps configure our TabLayout with title and icons. And uses ViewPager2.
        TabLayoutMediator mediator = new TabLayoutMediator(mTabLayout, viewPager, new TabConfiguration());
        mediator.attach();
    }

    /**
     * For Mobile devices.
     *
     * @param tabLayout get BadgeDrawables to tabs.
     * @param recipeId  get recipe details by recipe ID.
     */
    private void setBadgeDrawablesSinglePane(final TabLayout tabLayout, int recipeId) {
        // From viewModel observe externally exposed data for this class
        mViewModel.totalIngredientsAndStepsNumber(recipeId).observe(getViewLifecycleOwner(),
                itemLength -> applyBadgeDrawables(itemLength, tabLayout));
    }

    /**
     * For Tablet devices.
     * <p>
     * We start getting data differently for tablets because we are not used ViewModel.
     *
     * @param tabLayout get BadgeDrawables to tabs.
     * @param recipeId  get recipe details by recipe ID.
     */
    private void setBadgeDrawablesDualPane(final TabLayout tabLayout, final int recipeId) {

        // Start a new background thread and execute task to query json data.
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            try {
                // Try to start a response to get response string from ResponseBuilder.
                String responseString = ResponseBuilder.startResponse();
                // Get JSON data which returns list of itemLength objects with responseString, ID.
                ItemLength itemLength = JsonUtils.fetchTotalNumberJsonData(responseString, recipeId);
                // After query for results start a new MainThread to set views for fragment layout.
                runOnMainThread(itemLength, tabLayout);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("RecipeDetailFragment", "Problem fetching Numbers", e);
            }
        });
    }

    /**
     * For Tablet devices.
     *
     * @param itemLength has length of ArrayList of objects in {@link Ingredients} {@link Steps}.
     * @param tabLayout  apply values to tabLayouts BadgeDrawables.
     */
    private void runOnMainThread(final ItemLength itemLength, TabLayout tabLayout) {
        AppExecutors.getInstance().mainThread().execute(
                () -> applyBadgeDrawables(itemLength, tabLayout));
    }

    /**
     * @param itemLength has length of ArrayList of objects in {@link Ingredients} {@link Steps}.
     * @param tabLayout  apply values to tabLayouts BadgeDrawables.
     */
    public void applyBadgeDrawables(ItemLength itemLength, TabLayout tabLayout) {
        // Get length of Ingredients ArrayList.
        int ingredientsSize = itemLength.getIngredientsSize();
        // Get length of Steps ArrayList.
        int stepSize = itemLength.getStepsSize();

        // Create new BadgeDrawable for ingredients tab in layout and set value with visibility.
        BadgeDrawable ingredientsBadge = Objects.requireNonNull(tabLayout.getTabAt(0))
                .getOrCreateBadge();
        ingredientsBadge.setVisible(true);
        ingredientsBadge.setNumber(ingredientsSize);
        // Create new BadgeDrawable for Steps tab in layout and set value with visibility.
        BadgeDrawable walkThroughBadge = Objects.requireNonNull(tabLayout.getTabAt(1))
                .getOrCreateBadge();
        walkThroughBadge.setVisible(true);
        walkThroughBadge.setNumber(stepSize);
    }

    /**
     * Set up-button for this fragment to navigate back to {@link RecipeListFragment}.
     */
    private void setNavButton() {
        mToolbar.setNavigationOnClickListener(
                view -> Navigation.findNavController(view).navigateUp());
    }

    /**
     * Extend TabLayoutMediator for configuring view and items.
     */
    private class TabConfiguration implements TabLayoutMediator.TabConfigurationStrategy {

        /**
         * @param tab      get single tab for configure and set properties.
         * @param position get tab by position to set properties.
         */
        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            // Create new String Array of titles for tabs.
            String[] tabNames = new String[]{
                    getResources().getString(R.string.tab_title_ingredients),
                    getResources().getString(R.string.tab_title_steps)
            };
            // Create new String Array of icons for tabs.
            int[] tabIcons = new int[]{
                    R.drawable.ic_ingredients,
                    R.drawable.ic_walkthrough
            };
            // Set titles and icons for tabs with position.
            tab.setText(tabNames[position]).setIcon(tabIcons[position]);
        }
    }
}
