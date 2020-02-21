package com.developersbreach.bakingapp.podcast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.ResponseBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PodcastFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Podcast>> mMutablePodcastList;
    private long mPlayBackPosition;
    private int mCurrentWindow;

    MutableLiveData<List<Podcast>> getMutablePodcastList() {
        if (mMutablePodcastList == null) {
            mMutablePodcastList = new MutableLiveData<>();
            fetchJsonData();
        }
        return mMutablePodcastList;
    }


    private void fetchJsonData() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String responseString = ResponseBuilder.startResponse();
                List<Podcast> recipeList = JsonUtils.fetchPodcastJsonData(responseString);
                mMutablePodcastList.postValue(recipeList);
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
