package com.developersbreach.bakingapp.view.podcast;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.view.MainActivity;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;
import com.developersbreach.bakingapp.viewModel.PodcastFragmentViewModel;
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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * This fragment is called when user clicks menu "PODCAST" in {@link RecipeListFragment}. This is
 * not a complete podcast player but able to play pause and select other tracks.
 * <p>
 * Podcast plays in bottom view player inside {@link BottomNavigationView} and also opens into
 * Main view player with details for current podcast playing.
 * <p>
 * THIS FRAGMENT DOES NOT SUPPORT DATA BINDING.
 * Cannot get reference for binding id to custom ExoPlayerController.
 * This fragment shows list of recipes to play podcasts. If any recipe is selected it plays
 * total number of steps available in audio with ExoPlayer.
 * <p>
 * This fragment observes data changes from {@link PodcastFragmentViewModel} class. Also helps
 * to keep this class clean and no operations are handled here. Also helps to preserve data of the
 * fragment without loosing it's data when user navigates between different destinations or changing
 * orientation of the device.
 */
public class PodcastFragment extends Fragment {

    /**
     * Declare {@link ViewModel} for this class to get observe data which is externally exposed.
     */
    private PodcastFragmentViewModel mViewModel;

    /**
     * Creates list data of type {@link Podcast} with {@link RecyclerView}.
     */
    private RecyclerView mPodcastRecyclerView;

    /**
     * Declare a {@link SimpleExoPlayer} to play any audio from list of string URL.
     */
    private SimpleExoPlayer mExoPlayer;

    /**
     * Boolean variable to set player state to play or pause.
     */
    private boolean mPlayWhenReady = true;

    /**
     * Customizing controls for bottom view player.
     */
    private PlayerControlView mBottomPlayerControlView;

    /**
     * View which has bottom view player for podcast.
     */
    private BottomNavigationView mBottomNavigationPlayerView;

    /**
     * Shows album cover for currently playing podcast in bottom player.
     */
    private ImageView mBottomPlayerAlbumImageView;

    /**
     * Shows podcast title which is playing in bottom view player.
     */
    private TextView mBottomPlayerPodcastTitleTextView;

    /**
     * ImageView to play and pause the current podcast.
     */
    private ImageView mBottomPlayerPlayImageView;

    /**
     * Customizing controls for main player view.
     */
    private PlayerControlView mMainPlayerControlView;

    /**
     * Parent view which contains all views for main player.
     * Based on functionality we will hiding this to show Bottom View Player, vice-versa.
     */
    private ConstraintLayout mMainPlayerParentView;

    /**
     * Album cover for Main Player View.
     */
    private ImageView mMainPlayerAlbumImageView;

    /**
     * A minimize button to hide the Main PLayer and show Bottom Player View.
     */
    private ImageView mMainPlayerMinimizeImageView;

    /**
     * Shows title of current podcast playing in Main Player View.
     */
    private TextView mMainPlayerPodcastTitleTextView;

    /**
     * This shows number of tracks available in each recipe selected.
     * For example recipe with name Nutella Pie has 5 tracks in total.
     */
    private TextView mMainPlayerTrackInfoTextView;

    /**
     * Show a play button in Main player View. And shows Pause button when clicked by visibility.
     */
    private ImageView mMainPlayerPlayImageView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_podcast, container, false);
        // Let the fragment know we have a menu to show in ToolBar by setting it to true.
        setHasOptionsMenu(true);
        // Get reference to all views in fragment.
        findFragmentViews(view);
        // Return root view of this fragment.
        return view;
    }

    /**
     * @param view used to find all views for this fragment with id's.
     */
    private void findFragmentViews(View view) {
        // Showing a separate toolbar for this fragment.
        Toolbar toolBar = view.findViewById(R.id.podcast_toolbar);
        // Set the above declared toolbar to ActionBar.
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolBar);
        // RecyclerView which populates list from list data class @Podcast.
        mPodcastRecyclerView = view.findViewById(R.id.podcasts_recycler_view);
        // Views for Bottom View Player inside BottomNavigationView.
        findBottomPlayerViews(view);
        // Views for Main Player View which opens into extended layout.
        findMainPlayerViews(view);
    }

    /**
     * @param view get reference to all main player views by finding it's id's.
     */
    private void findMainPlayerViews(View view) {
        mMainPlayerControlView = view.findViewById(R.id.main_player_control_view_podcast);
        mMainPlayerParentView = view.findViewById(R.id.main_player_parent_view_podcast);
        mMainPlayerAlbumImageView = view.findViewById(R.id.main_player_album_view_podcast);
        mMainPlayerMinimizeImageView = view.findViewById(R.id.main_player_minimize_image_view);
        mMainPlayerPodcastTitleTextView = view.findViewById(R.id.main_player_podcast_title_text_view);
        mMainPlayerTrackInfoTextView = view.findViewById(R.id.main_player_track_info_text_view_podcast);
        mMainPlayerPlayImageView = view.findViewById(R.id.main_player_play_image_view_podcast);
    }

    /**
     * @param view get reference to all bottom player views by finding it's id's.
     */
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
        // With variable viewModel start observing LiveData to this fragment by setting this
        // class owner and get new Observer for recipe details data.
        // podcastList() is only externally exposed LiveData object in ViewModel class to
        // observe data.
        mViewModel = new ViewModelProvider(this).get(PodcastFragmentViewModel.class);
        mViewModel.podcastList().observe(getViewLifecycleOwner(), podcastList -> {
            // Pass data to adapter and create new listener for each items in steps list.
            PodcastAdapter adapter = new PodcastAdapter(podcastList, new PodcastListener());
            // Set adapter to this fragments RecyclerView.
            mPodcastRecyclerView.setAdapter(adapter);
        });

        // Listener for Bottom navigation view which opens extended main player.
        mBottomNavigationPlayerView.setOnClickListener(new BottomNavigationViewListener());
        // Listener for Minimize button inside Main Player to hide the player view.
        mMainPlayerMinimizeImageView.setOnClickListener(new MainPlayerMinimizeListener());
        // Listener for Play button which trigers ExoPlayer to start the player.
        mBottomPlayerPlayImageView.setOnClickListener(new BottomPlayerPlayListener());
        // Listener for playing the podcast inside Main PLayer View.
        mMainPlayerPlayImageView.setOnClickListener(new MainPlayerPlayListener());
    }

    /**
     * Set listener for {@link Podcast} items with custom interface from {@link PodcastFragment}.
     */
    private class PodcastListener implements PodcastAdapter.PodcastAdapterListener {
        @Override
        public void onPodcastSelected(Podcast podcast) {
            // When a podcast is selected to play by user, show the current playing podcast
            // in BottomNavigationView.
            // This view is initially hidden because user never selected a podcast before start.
            mBottomNavigationPlayerView.setVisibility(View.VISIBLE);
            // When playing a podcast for each recipe which has multiple tracks also with empty
            // string URL's, when empty URL is being triggered ExoPlayer stops the audio.
            // So first remove empty URL's and add valid URL's to a new list of Strings.
            // This functionality is explained more clearly below in doc.
            List<String> removedEmptyUrlsFromList = mViewModel.removeEmptyUrls(podcast);
            // When a podcast is being selected we have to let the user know which podcast is
            // being playing in background. So get details of the recipe and set them to views.
            setSelectedPodcastBottomPlayerViews(podcast, mBottomPlayerPodcastTitleTextView,
                    mBottomPlayerAlbumImageView);
            // In Main Player View the selected podcast details will be shown.
            // When a podcast is being selected we have to let the user know which podcast is
            // being playing in background. So get details of the recipe and set them to views.
            setSelectedPodcastMainPlayerViews(podcast, mMainPlayerPodcastTitleTextView,
                    removedEmptyUrlsFromList.size(), mMainPlayerTrackInfoTextView,
                    mMainPlayerAlbumImageView);

            // Stop previous track and start new one, if playing. Reset state of play and pause
            // buttons to show proper imageViews.
            resetExoPlayerControllerViews();
            // Initializing the player will start the ExoPlayer to play the selected podcast
            // after removing the empty string URL's from the list.
            initializePlayer(removedEmptyUrlsFromList, getContext());
        }
    }

    /**
     * When a podcast is being played and user selects the another podcast to play we have to
     * reset the player control views.
     * If Nutella Pie is playing and when the user selects Brownies, first thing we need to do
     * is stop the player. Later release it. Then change the controls to defaults.
     */
    private void resetExoPlayerControllerViews() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
            mBottomPlayerPlayImageView.setImageResource(R.drawable.ic_pause_player);
            mMainPlayerPlayImageView.setImageResource(R.drawable.ic_pause_player);
        }
    }

    /**
     * First we will get number of steps available in each recipe. Then add them to a newly
     * created list if strings.
     * Now using {@link Iterator} remove empty strings from created list of strings by looping
     * through each string by performing null or empty string check.
     * If the string is Empty, simply remove it from list. Now from existing valid string URL's
     * create new updated list without empty URL's.
     * This will make sure podcast will not stop in middle while playing whole recipe steps
     * in order we want to.
     *
     * @param removedEmptyUrlList list which does not have empty URL's.
     * @param context             for creating a new instance of the ExoPlayer.
     * @see PodcastFragmentViewModel#removeEmptyUrls(Podcast) which does this operation.
     */
    private void initializePlayer(List<String> removedEmptyUrlList, Context context) {
        // To play audio version from videoURL's, declare a new TrackSelector
        TrackSelector trackSelector = new DefaultTrackSelector();
        // Declare LoadControl which controls the buffering of media.
        LoadControl loadControl = new DefaultLoadControl();
        // Create new instance for ExoPlayer with context, trackSelector and loadControl.
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        // Attach the bottom player with custom control views.
        mBottomPlayerControlView.setPlayer(mExoPlayer);
        // Attach the main player with custom control views.
        mMainPlayerControlView.setPlayer(mExoPlayer);

        // Prepare the MediaSource with updated list of string URL's and passing it's length.
        MediaSource[] mediaSources = new MediaSource[removedEmptyUrlList.size()];
        // Loop into each String inside the list f URL's to create and URI by parsing into it.
        for (int i = 0; i < mediaSources.length; i++) {
            Uri uri = Uri.parse(removedEmptyUrlList.get(i));
            mediaSources[i] = buildMediaSource(uri);
        }

        MediaSource mediaSource;
        // If mediaSource length is just 1, which means no need to call ConcatenatingMediaSource.
        if (mediaSources.length == 1) {
            mediaSource = mediaSources[0];
        } else {
            // If more than 1 is available, let's concatenate them using ConcatenatingMediaSource
            // with media source.
            mediaSource = new ConcatenatingMediaSource(mediaSources);
        }

        // Prepare the ExoPlayer with playable source.
        mExoPlayer.prepare(mediaSource, false, false);
        // Save position and state of window by setting position to ViewModel.
        mExoPlayer.seekTo(mViewModel.getCurrentWindow(), mViewModel.getPlayBackPosition());
        // Save position and state of window by setting position to ViewModel.
        mExoPlayer.setPlayWhenReady(mPlayWhenReady);
    }

    /**
     * @param uri to create media source with uri.
     * @return new uri from passed string URL's.
     */
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()), this.getClass().getSimpleName());
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    /**
     * Views for Bottom view player inside {@link BottomNavigationView}
     *
     * @param podcast has details for selected podcast.
     * @param title   set the title for currently playing podcast.
     * @param album   set cover album from podcast details.
     */
    private void setSelectedPodcastBottomPlayerViews(Podcast podcast, TextView title,
                                                     ImageView album) {
        title.setText(podcast.getPodcastRecipeName());
        Glide.with(Objects.requireNonNull(getContext()))
                .load(podcast.getPodcastRecipeImage())
                .centerCrop()
                .into(album);
    }

    /**
     * Views for Main Player View.
     *
     * @param podcast   has details for selected podcast.
     * @param title     set the title for currently playing podcast in main player.
     * @param size      number of tracks available in each podcast after removing empty string URL's.
     * @param trackInfo description text which shows available tracks in selected podcast.
     * @param album     set cover album from podcast in main player view.
     */
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

    /**
     * Release ExoPlayer
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            // start immediately.
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            // Set position and state of window
            mViewModel.setPlayBackPosition(mExoPlayer.getCurrentPosition());
            mViewModel.setCurrentWindow(mExoPlayer.getCurrentWindowIndex());
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * This fragment shows a menu to return {@link MainActivity}.
     * <p>
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
        NavController navController = Navigation.findNavController(
                Objects.requireNonNull(this.getActivity()), R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    /**
     * When bottom player view is clicked --> Hide podcastList, bottom player view.
     * show MainPlayer.
     */
    private class BottomNavigationViewListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mPodcastRecyclerView.setVisibility(View.INVISIBLE);
            mMainPlayerParentView.setVisibility(View.VISIBLE);
            mBottomNavigationPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * When main player minimize button is clicked --> Hide MainPlayer view.
     * Show podcastList and Bottom player view.
     */
    private class MainPlayerMinimizeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mPodcastRecyclerView.setVisibility(View.VISIBLE);
            mMainPlayerParentView.setVisibility(View.INVISIBLE);
            mBottomNavigationPlayerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Play button clicked in bottom player --> change play button to pause, vice-versa.
     */
    private class BottomPlayerPlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mExoPlayer != null) {
                updatePlayPauseViews();
            }
        }
    }

    /**
     * Play button clicked in main player --> change play button to pause, vice-versa.
     */
    private class MainPlayerPlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mExoPlayer != null) {
                updatePlayPauseViews();
            }
        }
    }

    /**
     * When play button is clicked, show pause button and vice-versa.
     * Only pause if the ExoPlayer is playing anf not null.
     */
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

    /**
     * Let this fragment only run in portrait mode. There are few fixes need to be done for
     * supporting this fragment to show in landscape mode.
     * 1. Needs separate fragment for Main player.
     * 2. Handle Configuration for bottom and main player.
     * 3. Not to use ExoPlayer and PlayerControlView for showing views.
     * 4. Use ViewModels for fragments and handle playback and state of ExoPLayer.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * When this fragment is being destroyed don't let the other fragments launch or resume
     * in unspecified state.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
