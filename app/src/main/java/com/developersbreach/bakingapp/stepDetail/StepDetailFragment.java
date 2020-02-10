package com.developersbreach.bakingapp.stepDetail;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Steps;
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
    private TextView mPlayerShortDescriptionTextView;
    private TextView mPlayerDescriptionTextView;
    private Toolbar mStepDetailToolBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mRecipePlayerView = view.findViewById(R.id.recipe_player_view);
        mPlayerShortDescriptionTextView = view.findViewById(R.id.step_shortDescription_player_text_view);
        mPlayerDescriptionTextView = view.findViewById(R.id.step_description_player_text_view);
        mStepDetailToolBar = view.findViewById(R.id.step_detail_toolbar);
        setHasOptionsMenu(true);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mStepDetailToolBar);

        int deviceState = getResources().getConfiguration().orientation;
        if (deviceState == Configuration.ORIENTATION_LANDSCAPE) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        setNavButton();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Application application = Objects.requireNonNull(getActivity()).getApplication();
        Steps steps = StepDetailFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getStepDetailArgs();
        String name = StepDetailFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getRecipeNameStepDetailArgs();
        StepDetailFragmentViewModelFactory factory = new StepDetailFragmentViewModelFactory(application, steps, name);
        mViewModel = new ViewModelProvider(this, factory).get(StepDetailFragmentViewModel.class);

        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mStepDetailToolBar.setTitle(mViewModel.getRecipeName());
                mViewModel.getStepDetailData().observe(getViewLifecycleOwner(), new Observer<Steps>() {
                    @Override
                    public void onChanged(Steps steps) {
                        mPlayerShortDescriptionTextView.setText(steps.getStepsShortDescription());
                        mPlayerDescriptionTextView.setText(steps.getStepsDescription());
                    }
                });
            }
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
        mStepDetailToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || mExoPlayer == null)) {
            mViewModel.getStepDetailData().observe(getViewLifecycleOwner(), new Observer<Steps>() {
                @Override
                public void onChanged(Steps steps) {
                    initializePlayer(steps.getVideoUrl());
                }
            });
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
