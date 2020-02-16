package com.developersbreach.bakingapp.podcast;

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
import androidx.lifecycle.Observer;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class PodcastFragment extends Fragment {

    private PodcastFragmentViewModel mViewModel;
    private RecyclerView mPodcastRecyclerView;
    private PodcastAdapter mPodcastAdapter;
    private SimpleExoPlayer mExoPlayer;

    private BottomNavigationView mPodcastBottomPlayerView;
    private ImageView mPodcastRecipeAlbumImageView;
    private TextView mPodcastRecipeTitleTextView;
    private TextView mPodcastChefTitleTextView;
    private ImageView mPodcastBottomViewPlayerPlayImageView;
    private PlayerControlView mBottomPlayerView;

    private ConstraintLayout mExpandedPodcastPlayerViewParent;
    private ImageView mExpandedPodcastRecipeImageView;
    private ImageView mExpandedPodcastMinimizeImageView;
    private TextView mExpandedPodcastRecipeTitleTextView;
    private TextView mExpandedPodcastChefTitleTextView;
    private PlayerControlView mMainPlayerView;

    private ImageView mPodcastMainPlayerPlayImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast, container, false);
        Toolbar podcastToolBar = view.findViewById(R.id.podcast_toolbar);
        mPodcastRecyclerView = view.findViewById(R.id.podcasts_Recycler_view);
        mBottomPlayerView = view.findViewById(R.id.podcast_player_view);
        mMainPlayerView = view.findViewById(R.id.podcast_main_player_view);
        setHasOptionsMenu(true);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(podcastToolBar);

        mPodcastBottomPlayerView = view.findViewById(R.id.podcast_bottom_navigation_view);
        mPodcastRecipeAlbumImageView = view.findViewById(R.id.podcast_bottom_view_player_album_image_view);
        mPodcastRecipeTitleTextView = view.findViewById(R.id.podcast_bottom_view_player_title_text_view);
        mPodcastChefTitleTextView = view.findViewById(R.id.podcast_bottom_view_player_chef_title_text_view);
        mPodcastBottomViewPlayerPlayImageView = view.findViewById(R.id.podcast_bottom_view_player_play_image_view);

        mExpandedPodcastPlayerViewParent = view.findViewById(R.id.expanded_podcast_player_view_parent);
        mExpandedPodcastRecipeImageView = view.findViewById(R.id.expanded_podcast_player_image_view);
        mExpandedPodcastMinimizeImageView = view.findViewById(R.id.expanded_player_minimize_image_view);
        mExpandedPodcastRecipeTitleTextView = view.findViewById(R.id.expanded_podcast_player_title_text_view);
        mExpandedPodcastChefTitleTextView = view.findViewById(R.id.expanded_podcast_player_chef_text_view);
        mPodcastMainPlayerPlayImageView = view.findViewById(R.id.podcast_main_player_play_image_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(PodcastFragmentViewModel.class);
        mViewModel.getMutablePodcastList().observe(getViewLifecycleOwner(), new Observer<List<Podcast>>() {
            @Override
            public void onChanged(List<Podcast> podcastList) {
                mPodcastAdapter = new PodcastAdapter(getContext(), podcastList, new PodcastListener(), mViewModel);
                mPodcastRecyclerView.setAdapter(mPodcastAdapter);
            }
        });

        mPodcastBottomPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPodcastRecyclerView.setVisibility(View.INVISIBLE);
                mExpandedPodcastPlayerViewParent.setVisibility(View.VISIBLE);
                mPodcastBottomPlayerView.setVisibility(View.INVISIBLE);
            }
        });

        mExpandedPodcastMinimizeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPodcastRecyclerView.setVisibility(View.VISIBLE);
                mExpandedPodcastPlayerViewParent.setVisibility(View.INVISIBLE);
                mPodcastBottomPlayerView.setVisibility(View.VISIBLE);
            }
        });

        mPodcastBottomViewPlayerPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExoPlayer != null) {
                    updatePlayPauseViews();
                }
            }
        });

        mPodcastMainPlayerPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExoPlayer != null) {
                    updatePlayPauseViews();
                }
            }
        });

    }

    private void updatePlayPauseViews() {
        if (mExoPlayer.isPlaying()) {
            mExoPlayer.setPlayWhenReady(false);
            mPodcastBottomViewPlayerPlayImageView.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
            mPodcastMainPlayerPlayImageView.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        } else {
            mExoPlayer.setPlayWhenReady(true);
            mPodcastBottomViewPlayerPlayImageView.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
            mPodcastMainPlayerPlayImageView.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        }
    }

    private class PodcastListener implements PodcastAdapter.PodcastAdapterListener {
        @Override
        public void onPodcastSelected(Podcast podcast, View view) {

            // Show hidden BottomNavigationView first
            mPodcastBottomPlayerView.setVisibility(View.VISIBLE);

            // Set views for bottom view player
            mPodcastRecipeTitleTextView.setText(podcast.getPodcastRecipeName());
            mPodcastChefTitleTextView.setText(getResources().getString(R.string.udacity_chef_name));
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(podcast.getPodcastRecipeImage())
                    .centerCrop()
                    .into(mPodcastRecipeAlbumImageView);

            // Show views for main player
            mExpandedPodcastRecipeTitleTextView.setText(podcast.getPodcastRecipeName());
            mExpandedPodcastChefTitleTextView.setText(getResources().getString(R.string.udacity_chef_name));
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(podcast.getPodcastRecipeImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_recipe_icon)
                    .into(mExpandedPodcastRecipeImageView);

            // Stop previous track, if playing. Reset state of icon to show pause because player is active
            if (mExoPlayer != null) {
                mExoPlayer.release();
                mExoPlayer = null;
                mPodcastBottomViewPlayerPlayImageView.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                mPodcastMainPlayerPlayImageView.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
            }

            List<String> updatedList = mViewModel.removeEmptyUrls(podcast);

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mBottomPlayerView.setPlayer(mExoPlayer);
            mMainPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            MediaSource[] mediaSources = new MediaSource[updatedList.size()];
            for (int i = 0; i < mediaSources.length; i++) {
                Uri uri = Uri.parse(updatedList.get(i));
                mediaSources[i] = buildMediaSource(uri);
            }

            MediaSource mediaSource;
            if (mediaSources.length == 1) {
                mediaSource = mediaSources[0];
            } else {
                mediaSource = new ConcatenatingMediaSource(mediaSources);
            }
            mExoPlayer.prepare(mediaSource, false, false);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()), this.getClass().getSimpleName());
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
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
}
