package com.developersbreach.bakingapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PodcastFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Podcast>> _mMutablePodcastList;
    private long mPlayBackPosition;
    private int mCurrentWindow;

    public MutableLiveData<List<Podcast>> podcastList() {
        getMutablePodcastList();
        return _mMutablePodcastList;
    }

    private void getMutablePodcastList() {
        if (_mMutablePodcastList == null) {
            _mMutablePodcastList = new MutableLiveData<>();
            fetchJsonData();
        }
    }

    public void fetchJsonData() {
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            try {
                String uriBuilder = ResponseBuilder.startResponse();
                List<Podcast> recipeList = JsonUtils.fetchPodcastJsonData(uriBuilder);
                _mMutablePodcastList.postValue(recipeList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<String> removeEmptyUrls(Podcast podcast) {
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
        return updatedList;
    }

    public int getCurrentWindow() {
        return mCurrentWindow;
    }

    public long getPlayBackPosition() {
        return mPlayBackPosition;
    }

    public void setCurrentWindow(int currentWindow) {
        this.mCurrentWindow = currentWindow;
    }

    public void setPlayBackPosition(long playBackPosition) {
        this.mPlayBackPosition = playBackPosition;
    }
}
