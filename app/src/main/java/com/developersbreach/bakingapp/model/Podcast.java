package com.developersbreach.bakingapp.model;

import com.developersbreach.bakingapp.bindingAdapter.PodcastListBindingAdapter;
import com.developersbreach.bakingapp.bindingAdapter.RecipeListBindingAdapter;
import com.developersbreach.bakingapp.network.JsonUtils;

import java.util.List;

/**
 * Data class for getting Podcasts, which returns objects of podcast recipe properties.
 * We pass each property into ArrayList of Podcasts from {@link JsonUtils#fetchPodcastJsonData(String)}
 * and these properties will be accessed by binding objects as static fields.
 * <p>
 * The list of properties are set to RecyclerView in class {@link PodcastListBindingAdapter}
 */
public class Podcast {

    // Podcast property of type int has unique id for each podcast recipe.
    private final int mPodcastRecipeId;
    // Podcast property of type String nas name of podcast which is recipe name itself.
    private final String mPodcastRecipeName;
    // Podcast property of type String which has URl of video which loads thumbnail using Glide
    private final String mPodcastRecipeImage;
    // Podcast property which creates a list of strings URL's containing URL of videos. These each
    // string url is mapped to single recipe. When one recipe is selected there mapped objects
    // os string of videoUrl's are returned.
    private final List<String> mPodcastStepsUrlList;

    // Class constructor for making data class into reusable objects of item lengths.
    public Podcast(int podcastRecipeId, String podcastRecipeName, String podcastRecipeImage,
                   List<String> podcastStepsUrlList) {
        this.mPodcastRecipeId = podcastRecipeId;
        this.mPodcastRecipeName = podcastRecipeName;
        this.mPodcastRecipeImage = podcastRecipeImage;
        this.mPodcastStepsUrlList = podcastStepsUrlList;
    }

    /**
     * @return recipeId for podcast of type int for passing as an argument to load specific podcast.
     */
    public int getPodcastRecipeId() {
        return mPodcastRecipeId;
    }

    /**
     * @return recipe name for podcast of type String for selected podcast.
     */
    public String getPodcastRecipeName() {
        return mPodcastRecipeName;
    }

    /**
     * @return recipe thumbnail for podcast of type String of URL for selected podcast.
     */
    public String getPodcastRecipeImage() {
        return mPodcastRecipeImage;
    }

    /**
     * @return list of String Url's of steps for selected podcast with list of steps.
     */
    public List<String> getPodcastStepsUrlList() {
        return mPodcastStepsUrlList;
    }
}
