package com.developersbreach.bakingapp.podcast;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.podcast.PodcastAdapter.PodcastViewHolder;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.StringUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PodcastFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Podcast>> mMutablePodcastList;

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

    void loadPodcastProperties(Context context, Podcast podcast, PodcastViewHolder holder) {
        holder.mPodcastRecipeNameTextItemView.setText(podcast.getPodcastRecipeName());

        Glide.with(context)
                .load(podcast.getPodcastRecipeImage())
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mPodcastRecipeImageView);
    }

    void loadPodcastSteps(Podcast podcast, PodcastViewHolder holder) {
        List<Long> durationList = new ArrayList<>();
        for (String urlList : podcast.getPodcastStepsUrlList()) {
            if (!urlList.equals("")) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(urlList, new HashMap<String, String>());
                long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                retriever.release();
                durationList.add(duration);
            }
        }

        long totalDuration = 0;
        for (int i = 0; i < durationList.size(); i++) {
            long currentDuration = durationList.get(i);
            totalDuration = totalDuration + currentDuration;
        }

        loadDurationInMainThread(totalDuration, holder);
    }

    private void loadDurationInMainThread(final long totalDuration, final PodcastViewHolder holder) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                String formatDuration = StringUtils.getStringTimeFormat(totalDuration);
                holder.mPodcastRecipeDurationTextItemView.setText(formatDuration);
            }
        });
    }
}
