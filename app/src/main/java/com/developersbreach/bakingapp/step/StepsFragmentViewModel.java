package com.developersbreach.bakingapp.step;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.recipeList.RecipeAdapter;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.step.StepsAdapter.StepsViewHolder;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.QueryUtils;
import com.developersbreach.bakingapp.utils.UriBuilder;

import java.net.URL;
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

        RequestOptions decoder = new RequestOptions().frame(1000);

        Glide.with(context)
                .load(steps.getThumbnailUrl())
                .centerCrop()
                .apply(decoder)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(holder.mStepThumbnailItemImageView);
    }

    private class targetImageView extends BitmapImageViewTarget {

        private final StepsViewHolder mHolder;

        /**
         * @param holder is only parameter for our class constructor to have access over ImageView
         *               from {@link RecipeAdapter} holder.
         */
        targetImageView(StepsViewHolder holder) {
            super(holder.mStepThumbnailItemImageView);
            this.mHolder = holder;
        }

        /**
         * Sets the {@link Bitmap} on the view using {@link
         * ImageView#setImageBitmap(Bitmap)}.
         *
         * @param resource The bitmap to display.
         */
        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            super.onResourceReady(resource, transition);
            // Request to generate palette as async
            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                /**
                 * @param palette to extract colors from ImageView.
                 */
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    // Only apply if palette has resources generated successfully.

                }
            });
        }
    }
}
