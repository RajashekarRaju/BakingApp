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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentStepDetailBinding;
import com.developersbreach.bakingapp.model.Steps;
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

public class StepDetailFragment extends Fragment {

    private StepDetailFragmentViewModel mViewModel;
    private PlayerView mRecipePlayerView;
    private SimpleExoPlayer mExoPlayer;
    private boolean playWhenReady = true;

    private Toolbar mStepDetailToolBar;
    private FragmentStepDetailBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        mRecipePlayerView = mBinding.recipePlayerView;
        mStepDetailToolBar = mBinding.stepDetailToolbar;
        setHasOptionsMenu(true);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mStepDetailToolBar);

        int deviceState = getResources().getConfiguration().orientation;
        if (deviceState == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        mBinding.setLifecycleOwner(this);

        setNavButton();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = Objects.requireNonNull(getArguments());
        Activity activity = Objects.requireNonNull(getActivity());
        Application application = activity.getApplication();
        Steps stepsArgs = StepDetailFragmentArgs.fromBundle(args).getStepDetailArgs();
        String nameArgs = StepDetailFragmentArgs.fromBundle(args).getRecipeNameStepDetailArgs();
        StepDetailFragmentViewModelFactory factory = new StepDetailFragmentViewModelFactory(application, stepsArgs, nameArgs);
        mViewModel = new ViewModelProvider(this, factory).get(StepDetailFragmentViewModel.class);

        AppExecutors.getInstance().mainThread().execute(() -> {

            mStepDetailToolBar.setTitle(mViewModel.getRecipeName());
            mViewModel.stepDetails().observe(getViewLifecycleOwner(), steps -> {
                mBinding.setStepDetail(steps);
                mBinding.executePendingBindings();

                if (steps.getVideoUrl().equals("")) {
                    mRecipePlayerView.setCustomErrorMessage(getResources().getString(R.string.exo_player_video_error));
                    mRecipePlayerView.setUseController(false);
                }
            });
        });
    }

    private void initializePlayer(String videoUrl) {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(getContext()));
        mRecipePlayerView.setPlayer(mExoPlayer);

        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);

        mExoPlayer.setPlayWhenReady(playWhenReady);
        mExoPlayer.seekTo(mViewModel.getCurrentWindow(), mViewModel.getPlayBackPosition());
        mExoPlayer.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory factory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()), "RecipeSteps");
        return new ProgressiveMediaSource.Factory(factory).createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mViewModel.setPlayBackPosition(mExoPlayer.getCurrentPosition());
            mViewModel.setCurrentWindow(mExoPlayer.getCurrentWindowIndex());
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(
                Objects.requireNonNull(this.getActivity()), R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    private void setNavButton() {
        mStepDetailToolBar.setNavigationOnClickListener(
                view -> Navigation.findNavController(view).navigateUp());
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || mExoPlayer == null)) {
            mViewModel.stepDetails().observe(getViewLifecycleOwner(),
                    steps -> initializePlayer(steps.getVideoUrl()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}
