package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.utils.FormatUtils;

public class StepsListBindingAapter {

    @BindingAdapter("stepThumbnail")
    public static void bindStepThumbnail(ImageView imageView, String thumbnailUrl) {
        Glide.with(imageView.getContext())
                .load(thumbnailUrl)
                .thumbnail(Glide.with(imageView.getContext()).load(thumbnailUrl).override(50, 50))
                .placeholder(R.drawable.ic_thumbnail_available)
                .error(R.drawable.ic_thumbnail_not_available)
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter("stepShortDescription")
    public static void bindStepShortDescription(TextView textView, String shortDescription) {
        textView.setText(shortDescription);
    }

    @BindingAdapter("stepVideoDuration")
    public static void bindStepVideoDuration(TextView textView, String videoUrl) {

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!"".equals(videoUrl)) {
                    long duration = FormatUtils.thumbnailRetriever(videoUrl);
                    runOnMainThread(duration);
                }
            }

            private void runOnMainThread(long duration) {
                AppExecutors.getInstance().mainThread().execute(() -> {
                    String formatDuration = FormatUtils.getStringTimeFormat(duration);
                    textView.setText(formatDuration);
                });
            }
        });
    }
}
