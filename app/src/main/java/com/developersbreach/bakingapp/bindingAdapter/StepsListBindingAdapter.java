package com.developersbreach.bakingapp.bindingAdapter;

import android.media.MediaMetadataRetriever;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.utils.FormatUtils;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;
import com.developersbreach.bakingapp.viewModel.StepsFragmentViewModel;

/**
 * BindingAdapter for fragment class {@link StepsFragment} with model {@link Steps} and
 * ViewModel class {@link StepsFragmentViewModel}
 */
public class StepsListBindingAdapter {

    /**
     * When stepThumbnail is used on ImageView, the method bindStepThumbnail is called and glide
     * loads the image using URL.
     * We Use placeholders, error to be visible to user until image loads in background.
     *
     * @param imageView    using Glide library we set an image which needs a URL.
     * @param thumbnailUrl contains URL to load an image
     */
    @BindingAdapter("stepThumbnail")
    public static void bindStepThumbnail(ImageView imageView, String thumbnailUrl) {
        Glide.with(imageView.getContext())
                .load(thumbnailUrl)
                .thumbnail(Glide.with(imageView.getContext())
                        .load(thumbnailUrl)
                        .override(50, 50))
                .placeholder(R.drawable.ic_thumbnail_available)
                .error(R.drawable.ic_thumbnail_not_available)
                .centerCrop()
                .into(imageView);
    }

    /**
     * When stepShortDescription is used on TextView, the method bindStepShortDescription is called.
     *
     * @param textView         a view which we use to set a String value to it.
     * @param shortDescription contains String value to be set to TextView.
     */
    @BindingAdapter("stepShortDescription")
    public static void bindStepShortDescription(TextView textView, String shortDescription) {
        textView.setText(shortDescription);
    }

    /**
     * When stepVideoDuration is used on TextView, the method bindStepVideoDuration is called.
     * We start a new background thread to query duration of each video using {@link MediaMetadataRetriever}
     * and call with key METADATA_KEY_DURATION to get duration from video URL.
     * We only perform operations when value id non-null.
     * <p>
     * Once query is finished we start a new main thread to bind those views to the TextView after
     * performing string formatting which shows time in simple SS::MM::HH format.
     *
     * @param textView a view which we use to set a String value to it.
     * @param videoUrl contains URL which loads duration to be set to TextView.
     */
    @BindingAdapter("stepVideoDuration")
    public static void bindStepVideoDuration(TextView textView, String videoUrl) {

        // Start a new background thread to query duration data.
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                // To avoid NullPointerException. Only perform is query string is not null.
                if (!"".equals(videoUrl)) {
                    long duration = FormatUtils.durationRetriever(videoUrl);
                    // Call a MainThread to bind views. Only do here to touch views.
                    runOnMainThread(duration);
                }
            }

            /**
             * Start of new MainThread.
             * @param duration has value of type long which needs to be formatted.
             */
            private void runOnMainThread(long duration) {
                AppExecutors.getInstance().mainThread().execute(() -> {
                    String formatDuration = FormatUtils.getStringTimeFormat(duration);
                    textView.setText(formatDuration);
                });
            }
        });
    }
}
