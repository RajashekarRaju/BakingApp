package com.developersbreach.bakingapp.podcast;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Podcast;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PodcastFragment extends Fragment {

    private PodcastFragmentViewModel mViewModel;
    private RecyclerView mPodcastRecyclerView;
    private PodcastAdapter mPodcastAdapter;
    private SimpleExoPlayer mExoPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast, container, false);
        Toolbar podcastToolBar = view.findViewById(R.id.podcast_toolbar);
        mPodcastRecyclerView = view.findViewById(R.id.podcasts_Recycler_view);
        setHasOptionsMenu(true);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(podcastToolBar);
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

    private class PodcastListener implements PodcastAdapter.PodcastAdapterListener {
        @Override
        public void onPodcastSelected(Podcast podcast, View view) {

            if (mExoPlayer != null) {
                mExoPlayer.release();
                mExoPlayer = null;
            }

            Iterator<String> iterator = podcast.getPodcastStepsUrlList().iterator();
            List<String> updatedList = new ArrayList<>();
            while (iterator.hasNext()) {
                String currentUrl = iterator.next();
                if (!currentUrl.equals("")) {
                    updatedList.add(currentUrl);
                } else {
                    iterator.remove();
                }
            }

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(getContext()));
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

            mExoPlayer.setPlayWhenReady(true);
            //mExoPlayer.seekTo(mViewModel.getCurrentWindow(), mViewModel.getPlayBackPosition());
            mExoPlayer.prepare(mediaSource, false, false);

            Toast.makeText(getContext(), "Started", Toast.LENGTH_SHORT).show();
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()), "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }
}
