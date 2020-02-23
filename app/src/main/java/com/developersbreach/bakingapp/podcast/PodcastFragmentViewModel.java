package com.developersbreach.bakingapp.podcast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.ResponseBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PodcastFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Podcast>> _mMutablePodcastList;
    private long mPlayBackPosition;
    private int mCurrentWindow;

    MutableLiveData<List<Podcast>> podcastList() {
        getMutablePodcastList();
        return _mMutablePodcastList;
    }

    private void getMutablePodcastList() {
        if (_mMutablePodcastList == null) {
            _mMutablePodcastList = new MutableLiveData<>();
            fetchJsonData();
        }
    }

    private void fetchJsonData() {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String uriBuilder = ResponseBuilder.startResponse();
                List<Podcast> recipeList = JsonUtils.fetchPodcastJsonData(uriBuilder);
                _mMutablePodcastList.postValue(recipeList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    List<String> removeEmptyUrls(Podcast podcast) {
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

    int getCurrentWindow() {
        return mCurrentWindow;
    }

    long getPlayBackPosition() {
        return mPlayBackPosition;
    }

    void setCurrentWindow(int currentWindow) {
        this.mCurrentWindow = currentWindow;
    }

    void setPlayBackPosition(long playBackPosition) {
        this.mPlayBackPosition = playBackPosition;
    }
}
