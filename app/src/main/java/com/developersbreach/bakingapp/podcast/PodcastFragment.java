package com.developersbreach.bakingapp.podcast;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Podcast;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class PodcastFragment extends Fragment {


    private PodcastFragmentViewModel mViewModel;
    private RecyclerView mPodcastRecyclerView;
    private SimpleExoPlayer mExoPlayer;
    private boolean mPlayWhenReady = true;

    private PlayerControlView mBottomPlayerControlView;
    private BottomNavigationView mBottomNavigationPlayerView;
    private ImageView mBottomPlayerAlbumImageView;
    private TextView mBottomPlayerPodcastTitleTextView;
    private ImageView mBottomPlayerPlayImageView;

    private PlayerControlView mMainPlayerControlView;
    private ConstraintLayout mMainPlayerParentView;
    private ImageView mMainPlayerAlbumImageView;
    private ImageView mMainPlayerMinimizeImageView;
    private TextView mMainPlayerPodcastTitleTextView;
    private TextView mMainPlayerTrackInfoTextView;
    private ImageView mMainPlayerPlayImageView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast, container, false);

        setHasOptionsMenu(true);
        findFragmentViews(view);
        return view;
    }

    private void findFragmentViews(View view) {
        Toolbar toolBar = view.findViewById(R.id.podcast_toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolBar);
        mPodcastRecyclerView = view.findViewById(R.id.podcasts_recycler_view);
        findBottomPlayerViews(view);
        findMainPlayerViews(view);
    }

    private void findMainPlayerViews(View view) {
        mMainPlayerControlView = view.findViewById(R.id.main_player_control_view_podcast);
        mMainPlayerParentView = view.findViewById(R.id.main_player_parent_view_podcast);
        mMainPlayerAlbumImageView = view.findViewById(R.id.main_player_album_view_podcast);
        mMainPlayerMinimizeImageView = view.findViewById(R.id.main_player_minimize_image_view);
        mMainPlayerPodcastTitleTextView = view.findViewById(R.id.main_player_podcast_title_text_view);
        mMainPlayerTrackInfoTextView = view.findViewById(R.id.main_player_track_info_text_view_podcast);
        mMainPlayerPlayImageView = view.findViewById(R.id.main_player_play_image_view_podcast);
    }

    private void findBottomPlayerViews(View view) {
        mBottomPlayerControlView = view.findViewById(R.id.podcast_player_control_view);
        mBottomNavigationPlayerView = view.findViewById(R.id.bottom_navigation_player_view_podcast);
        mBottomPlayerAlbumImageView = view.findViewById(R.id.bottom_player_album_image_view_podcast);
        mBottomPlayerPodcastTitleTextView = view.findViewById(R.id.bottom_player_podcast_title_text_view);
        mBottomPlayerPlayImageView = view.findViewById(R.id.bottom_player_play_image_view_podcast);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(PodcastFragmentViewModel.class);
        mViewModel.podcastList().observe(getViewLifecycleOwner(), podcastList -> {
            PodcastAdapter adapter = new PodcastAdapter(podcastList, new PodcastListener());
            mPodcastRecyclerView.setAdapter(adapter);
        });

        mBottomNavigationPlayerView.setOnClickListener(new BottomNavigationViewListener());
        mMainPlayerMinimizeImageView.setOnClickListener(new MainPlayerMinimizeListener());
        mBottomPlayerPlayImageView.setOnClickListener(new BottomPlayerPlayListener());
        mMainPlayerPlayImageView.setOnClickListener(new MainPlayerPlayListener());
    }

    private class PodcastListener implements PodcastAdapter.PodcastAdapterListener {
        @Override
        public void onPodcastSelected(Podcast podcast, View view) {

            // Show hidden BottomNavigationView first
            mBottomNavigationPlayerView.setVisibility(View.VISIBLE);
            List<String> removedEmptyUrlsFromList = mViewModel.removeEmptyUrls(podcast);
            setSelectedPodcastBottomPlayerViews(podcast, mBottomPlayerPodcastTitleTextView,
                    mBottomPlayerAlbumImageView);
            setSelectedPodcastMainPlayerViews(podcast, mMainPlayerPodcastTitleTextView,
                    removedEmptyUrlsFromList.size(), mMainPlayerTrackInfoTextView,
                    mMainPlayerAlbumImageView);

            // Stop previous track, if playing. Reset state of icon to show pause because player is active
            resetExoPlayerControllerViews();
            initializePlayer(removedEmptyUrlsFromList, getContext());
        }
    }

    private void resetExoPlayerControllerViews() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
            mBottomPlayerPlayImageView.setImageResource(R.drawable.ic_pause_player);
            mMainPlayerPlayImageView.setImageResource(R.drawable.ic_pause_player);
        }
    }

    private void setSelectedPodcastBottomPlayerViews(Podcast podcast, TextView title, ImageView album) {
        title.setText(podcast.getPodcastRecipeName());
        Glide.with(Objects.requireNonNull(getContext()))
                .load(podcast.getPodcastRecipeImage())
                .centerCrop()
                .into(album);
    }

    private void setSelectedPodcastMainPlayerViews(Podcast podcast, TextView title, int size,
                                                   TextView trackInfo, ImageView album) {
        title.setText(podcast.getPodcastRecipeName());
        String podcastInfo = "Playing " + size + " tracks from " + podcast.getPodcastRecipeName();
        trackInfo.setText(podcastInfo);
        Glide.with(Objects.requireNonNull(getContext()))
                .load(podcast.getPodcastRecipeImage())
                .centerCrop()
                .placeholder(R.drawable.ic_recipe_icon)
                .into(album);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mViewModel.setPlayBackPosition(mExoPlayer.getCurrentPosition());
            mViewModel.setCurrentWindow(mExoPlayer.getCurrentWindowIndex());
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void initializePlayer(List<String> removedEmptyUrlList, Context context) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        mBottomPlayerControlView.setPlayer(mExoPlayer);
        mMainPlayerControlView.setPlayer(mExoPlayer);

        // Prepare the MediaSource.
        MediaSource[] mediaSources = new MediaSource[removedEmptyUrlList.size()];
        for (int i = 0; i < mediaSources.length; i++) {
            Uri uri = Uri.parse(removedEmptyUrlList.get(i));
            mediaSources[i] = buildMediaSource(uri);
        }

        MediaSource mediaSource;
        if (mediaSources.length == 1) {
            mediaSource = mediaSources[0];
        } else {
            mediaSource = new ConcatenatingMediaSource(mediaSources);
        }

        mExoPlayer.prepare(mediaSource, false, false);
        mExoPlayer.seekTo(mViewModel.getCurrentWindow(), mViewModel.getPlayBackPosition());
        mExoPlayer.setPlayWhenReady(mPlayWhenReady);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()), this.getClass().getSimpleName());
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
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

    private class BottomNavigationViewListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mPodcastRecyclerView.setVisibility(View.INVISIBLE);
            mMainPlayerParentView.setVisibility(View.VISIBLE);
            mBottomNavigationPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    private class MainPlayerMinimizeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mPodcastRecyclerView.setVisibility(View.VISIBLE);
            mMainPlayerParentView.setVisibility(View.INVISIBLE);
            mBottomNavigationPlayerView.setVisibility(View.VISIBLE);
        }
    }

    private class BottomPlayerPlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mExoPlayer != null) {
                updatePlayPauseViews();
            }
        }
    }

    private class MainPlayerPlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mExoPlayer != null) {
                updatePlayPauseViews();
            }
        }
    }

    private void updatePlayPauseViews() {
        if (mExoPlayer.isPlaying()) {
            mExoPlayer.setPlayWhenReady(false);
            mBottomPlayerPlayImageView.setImageResource(R.drawable.ic_play_player);
            mMainPlayerPlayImageView.setImageResource(R.drawable.ic_play_player);
        } else {
            mExoPlayer.setPlayWhenReady(true);
            mBottomPlayerPlayImageView.setImageResource(R.drawable.ic_pause_player);
            mMainPlayerPlayImageView.setImageResource(R.drawable.ic_pause_player);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
