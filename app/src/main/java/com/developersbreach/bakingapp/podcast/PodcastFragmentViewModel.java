package com.developersbreach.bakingapp.podcast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;
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
                try {
                    String uriBuilder = UriBuilder.uriBuilder();
                    URL requestUrl = QueryUtils.createUrl(uriBuilder);
                    String responseString = QueryUtils.getResponseFromHttpUrl(requestUrl);
                    List<Podcast> recipeList = JsonUtils.fetchPodcastJsonData(responseString);
                    mMutablePodcastList.postValue(recipeList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void setCurrentWindow(int currentWindow) {
        this.mCurrentWindow = currentWindow;
    }

    public void setPlayBackPosition(long playBackPosition) {
        this.mPlayBackPosition = playBackPosition;
    }
}
