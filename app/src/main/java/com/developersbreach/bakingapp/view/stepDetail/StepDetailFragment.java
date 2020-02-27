package com.developersbreach.bakingapp.view.stepDetail;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.net.Uri;
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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentStepDetailBinding;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.view.MainActivity;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;
import com.developersbreach.bakingapp.viewModel.StepDetailFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.factory.StepDetailFragmentViewModelFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

/**
 * This fragment is third destination in default NavGraph, opens when user selects a step from
 * {@link StepsFragment} class with list.
 * <p>
 * This fragment observes data changes from {@link StepDetailFragmentViewModel} class and it's
 * instance is created by factory {@link StepDetailFragmentViewModelFactory} class. Also helps
 * to keep this class clean and no operations are handled here. Also helps to preserve data of the
 * fragment without loosing it's data when user navigates between different destinations or changing
 * orientation of the device.
 */
public class StepDetailFragment extends Fragment {

    /**
     * Declare binding variable to give access for DataBinding to bind layout and views.
     */
    private FragmentStepDetailBinding mBinding;

    /**
     * Declare {@link ViewModel} for this class to get observe data which is externally exposed.
     */
    private StepDetailFragmentViewModel mViewModel;

    /**
     * Declare a toolbar, this fragment uses separate AppBarLayout to show custom ToolBar.
     */
    private Toolbar mToolBar;

    /**
     * Declare PlayerView to set with fragment ExoPlayer for playing a video.
     */
    private PlayerView mRecipePlayerView;

    /**
     * Declare a {@link SimpleExoPlayer} to play any video from string URL.
     */
    private SimpleExoPlayer mExoPlayer;

    /**
     * Boolean variable to set player state to play or pause.
     */
    private boolean mPlayWhenReady = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflating fragment layout with DataBinding.
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        // Get binding reference for playerView.
        mRecipePlayerView = mBinding.recipePlayerView;
        // Get binding reference for toolbar.
        mToolBar = mBinding.stepDetailToolbar;
        // Let the fragment know to inflate Menu in this fragment in onCreate.
        setHasOptionsMenu(true);
        // Set above declared ToolBar with ActionBar to show in ActionBar by passing it.
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolBar);
        // Set fragment to use full screen.
        setCustomSystemUi(getResources().getConfiguration().orientation);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        mBinding.setLifecycleOwner(this);
        // Set up-button in ActionBar in onCreate to navigate back.
        setNavButton();
        // Return root binding view.
        return mBinding.getRoot();
    }

    /**
     * @param orientation get state of UI orientation.
     */
    private void setCustomSystemUi(int orientation) {
        // For landscape customize the UI to use full screen while playing video.
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        Steps stepsArgs = StepDetailFragmentArgs.fromBundle(args).getStepDetailArgs();
        String nameArgs = StepDetailFragmentArgs.fromBundle(args).getRecipeNameStepDetailArgs();
        // Call factory for creating new instance of ViewModel for this fragment to observe data.
        // Pass application context, step and recipe name object to the factory.
        StepDetailFragmentViewModelFactory factory =
                new StepDetailFragmentViewModelFactory(application, stepsArgs, nameArgs);
        mViewModel = new ViewModelProvider(this, factory)
                .get(StepDetailFragmentViewModel.class);
        // Set toolbar to show appropriate title for respective recipe user selected in step fragment.
        mToolBar.setTitle(mViewModel.getRecipeName());
        // With variable viewModel start observing LiveData to this fragment by setting this
        // class owner and get new Observer for recipe details data.
        // stepDetails() is only externally exposed LiveData object in ViewModel class to
        // observe data.
        mViewModel.stepDetails().observe(getViewLifecycleOwner(), steps -> {
            // get reference to binding item views from layout and pass object for step data.
            mBinding.setStepDetail(steps);
            // Force binding to execute immediately all views.
            mBinding.executePendingBindings();
            // Few strings URL's are empty and won't play a video. So display a error message
            // for empty URL's by checking with empty string.
            if (steps.getVideoUrl().equals("")) {
                mRecipePlayerView.setCustomErrorMessage(getResources().getString(R.string.exo_player_video_error));
                // If there is no video available, don't show any controllers to user.
                mRecipePlayerView.setUseController(false);
            }
        });
    }

    /**
     * @param videoUrl of type string has URL to play media with ExoPlayer.
     */
    private void initializePlayer(String videoUrl) {
        // Create new instance of exoPlayer with context.
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(getContext()));
        // Attach exoPLayer with fragment playerView.
        mRecipePlayerView.setPlayer(mExoPlayer);
        // Each string URL parsed to URI.
        Uri uri = Uri.parse(videoUrl);
        // Build playable media source with uri we created frm URL's.
        MediaSource mediaSource = buildMediaSource(uri);
        // Save position and state of window by setting position to ViewModel.
        mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        // Save position and state of window by setting position to ViewModel.
        mExoPlayer.seekTo(mViewModel.getCurrentWindow(), mViewModel.getPlayBackPosition());
        // Prepare the ExoPlayer with playable source.
        mExoPlayer.prepare(mediaSource, false, false);
    }

    /**
     * @param uri to create media source with uri.
     * @return new uri from passed string URL's.
     */
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory factory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()), this.getClass().getSimpleName());
        return new ProgressiveMediaSource.Factory(factory).createMediaSource(uri);
    }

    /**
     * Release ExoPlayer
     */
    private void releasePlayer() {
        // Release only if there is a instance created vor ExoPlayer earlier.
        if (mExoPlayer != null) {
            // Get player position and state.
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mViewModel.setPlayBackPosition(mExoPlayer.getCurrentPosition());
            mViewModel.setCurrentWindow(mExoPlayer.getCurrentWindowIndex());
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * This fragment shows a menu to return {@link MainActivity}.
     *
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater inflate layout {@link R.menu#menu_home}
     * @see #setHasOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
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
        // Get NavController and find host for this fragment.
        NavController navController = Navigation.findNavController(
                Objects.requireNonNull(this.getActivity()), R.id.nav_host_fragment);
        // Return selected destination with NavController.
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    /**
     * Set up-button for this fragment to navigate back to {@link RecipeListFragment}.
     */
    private void setNavButton() {
        mToolBar.setNavigationOnClickListener(
                view -> Navigation.findNavController(view).navigateUp());
    }

    /**
     * Start player when fragment resumes.
     */
    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || mExoPlayer == null)) {
            // Initialize player with string URL.
            mViewModel.stepDetails().observe(getViewLifecycleOwner(),
                    steps -> initializePlayer(steps.getVideoUrl()));
        }
    }

    /**
     * Release player on pause.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    /**
     * Release player on stop.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}
