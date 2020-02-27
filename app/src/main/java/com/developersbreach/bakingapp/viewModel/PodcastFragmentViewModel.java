package com.developersbreach.bakingapp.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;
import com.developersbreach.bakingapp.view.podcast.PodcastFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ViewModel responsible for preparing and managing the data for fragment class {@link PodcastFragment}
 * This ViewModel expose the data RecipeList via {@link LiveData} and fragment observes the changes.
 * <p>
 * This helps us save fragment data and keeps UI simple.
 * This class performs background operations to fetch json data from internet.
 */
public class PodcastFragmentViewModel extends ViewModel {

    /**
     * This field is encapsulated, we used MutableLiveData because when the data is being changed
     * we will be updating Podcast with new values. And any externally exposed LiveData can observe.
     */
    private MutableLiveData<List<Podcast>> _mMutablePodcastList;

    /**
     * Get position of the ExoPlayer Bar to start from the same position.
     */
    private long mPlayBackPosition;

    /**
     * Get ExoPlayer state to manage player from current window.
     */
    private int mCurrentWindow;

    /**
     * @return Exposed {@link LiveData} object of List of {@link Podcast} externally allowing fragment
     * to observe changes. Data is observed once changes will be done internally.
     */
    public LiveData<List<Podcast>> podcastList() {
        getMutablePodcastList();
        return _mMutablePodcastList;
    }

    /**
     * Create a new {@link MutableLiveData} after checking if is is empty, otherwise no need make
     * changes by adding new values. If is empty start getting new values from JSON in background
     * thread.
     */
    private void getMutablePodcastList() {
        if (_mMutablePodcastList == null) {
            _mMutablePodcastList = new MutableLiveData<>();
            // Call this method to get new values.
            getMutablePodcastData();
        }
    }

    /**
     * Start this operation in new background thread.
     * First get single instance of {@link AppExecutors} and start the background thread to execute.
     *
     * @see JsonUtils#fetchPodcastJsonData(String)
     */
    public void getMutablePodcastData() {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            // Try to start a response to get response string from ResponseBuilder.
            try {
                String uriBuilder = ResponseBuilder.startResponse();
                // Get JSON data which returns list of podcast objects with responseString.
                List<Podcast> podcastList = JsonUtils.fetchPodcastJsonData(uriBuilder);
                // Add list to internally exposed data of podcast list by calling postValue.
                _mMutablePodcastList.postValue(podcastList);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("PodcastViewModel", "Problem fetching Podcasts", e);
            }
        });
    }

    /**
     * @param podcast get user selected list of podcast data to make changes and return.
     * @return return a list of string URL's by removing empty string URL's from steps videoURL.
     */
    public List<String> removeEmptyUrls(Podcast podcast) {
        // Create new Iterator of Strings and start iterating with list of string URl's available.
        Iterator<String> iterator = podcast.getPodcastStepsUrlList().iterator();
        // Create a new list to add non-null URL's after checking.
        List<String> updatedList = new ArrayList<>();
        // Loop into each URL using iterator.
        while (iterator.hasNext()) {
            String currentUrl = iterator.next();
            if (!currentUrl.equals("")) {
                // If current or any URL is not empty or valid we add those to new list.
                updatedList.add(currentUrl);
            } else {
                // If URL is empty or invalid we remove it.
                iterator.remove();
            }
        }
        // Return new valid list of string URL's from existing String URL's.
        return updatedList;
    }

    /**
     * @return gets the ExoPLayer state of the current window.
     */
    public int getCurrentWindow() {
        return mCurrentWindow;
    }

    /**
     * @return position of ExoPlayer Bar position.
     */
    public long getPlayBackPosition() {
        return mPlayBackPosition;
    }

    /**
     * @param currentWindow get state of current window and save window to new state.
     */
    public void setCurrentWindow(int currentWindow) {
        this.mCurrentWindow = currentWindow;
    }

    /**
     * @param playBackPosition get the last position and save position to new position
     */
    public void setPlayBackPosition(long playBackPosition) {
        this.mPlayBackPosition = playBackPosition;
    }
}
