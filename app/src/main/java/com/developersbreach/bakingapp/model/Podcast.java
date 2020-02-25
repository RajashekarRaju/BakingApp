package com.developersbreach.bakingapp.model;

import java.util.List;

public class Podcast {

    private final int mPodcastRecipeId;
    private final String mPodcastRecipeName;
    private final String mPodcastRecipeImage;
    private final List<String> mPodcastStepsUrlList;

    public Podcast(int podcastRecipeId, String podcastRecipeName, String podcastRecipeImage,
                   List<String> podcastStepsUrlList) {
        this.mPodcastRecipeId = podcastRecipeId;
        this.mPodcastRecipeName = podcastRecipeName;
        this.mPodcastRecipeImage = podcastRecipeImage;
        this.mPodcastStepsUrlList = podcastStepsUrlList;
    }

    public int getPodcastRecipeId() {
        return mPodcastRecipeId;
    }

    public String getPodcastRecipeName() {
        return mPodcastRecipeName;
    }

    public String getPodcastRecipeImage() {
        return mPodcastRecipeImage;
    }

    public List<String> getPodcastStepsUrlList() {
        return mPodcastStepsUrlList;
    }
}
