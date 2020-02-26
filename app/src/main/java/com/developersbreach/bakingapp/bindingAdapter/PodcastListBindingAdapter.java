package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.view.podcast.PodcastFragment;
import com.developersbreach.bakingapp.viewModel.PodcastFragmentViewModel;

/**
 * BindingAdapter for fragment class {@link PodcastFragment} with model {@link Podcast} and
 * ViewModel class {@link PodcastFragmentViewModel}
 */
public class PodcastListBindingAdapter {

    /**
     * When podcastRecipeName is used on TextView, the method bindPodcastRecipeName is called.
     *
     * @param textView a view which we use to set a String value to it.
     * @param recipeName     contains String value to be set to TextView.
     */
    @BindingAdapter("podcastRecipeName")
    public static void bindPodcastRecipeName(TextView textView, String recipeName) {
        textView.setText(recipeName);
    }
}
