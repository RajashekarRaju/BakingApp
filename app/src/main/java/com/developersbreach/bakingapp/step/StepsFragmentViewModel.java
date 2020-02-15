package com.developersbreach.bakingapp.step;

import android.app.Application;
import android.content.Context;
import android.media.MediaMetadataRetriever;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.step.StepsAdapter.StepsViewHolder;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.StringUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class StepsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<Steps>> mMutableStepsList;
    private int mMutableRecipeId;
    private String mMutableRecipeName;

    StepsFragmentViewModel(Application application, int recipeId, String recipeName) {
        super(application);
        this.mMutableRecipeId = recipeId;
        this.mMutableRecipeName = recipeName;
    }

    MutableLiveData<List<Steps>> getMutableStepsList() {
        mMutableStepsList = new MutableLiveData<>();
        getStepsData(mMutableRecipeId);
        return mMutableStepsList;
    }

    String getMutableRecipeName() {
        return mMutableRecipeName;
    }

    private void getStepsData(final int recipeId) {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String uriBuilder = UriBuilder.uriBuilder();
                    URL requestUrl = QueryUtils.createUrl(uriBuilder);
                    String responseString = QueryUtils.getResponseFromHttpUrl(requestUrl);
                    List<Steps> stepsList = JsonUtils.fetchSteps(responseString, recipeId);
                    mMutableStepsList.postValue(stepsList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void loadStepsData(Context context, Steps steps, StepsViewHolder holder) {
        holder.mStepShortDescriptionItemTextView.setText(steps.getStepsShortDescription());

        if (steps.getVideoUrl().equals("")) {
            holder.mStepsVideoPlayArrowItemImageView.setImageResource(R.drawable.ic_video_not_available);
        } else {
            holder.mStepsVideoPlayArrowItemImageView.setImageResource(R.drawable.ic_video_available);
        }
    }

    void loadVideoDurationInBackground(final Steps steps, final StepsViewHolder holder) {

        if (!steps.getVideoUrl().equals("")) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(steps.getVideoUrl(), new HashMap<String, String>());
            final long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            retriever.release();

            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    String formatDuration = StringUtils.getStringTimeFormat(duration);
                    holder.mVideoSizeTextView.setText(formatDuration);
                }
            });
        }
    }
}
