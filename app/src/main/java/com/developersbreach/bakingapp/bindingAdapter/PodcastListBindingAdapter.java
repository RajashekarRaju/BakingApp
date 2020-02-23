package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class PodcastListBindingAdapter {

    @BindingAdapter("podcastRecipeName")
    public static void bindPodcastRecipeName(TextView textView, String recipeName) {
        textView.setText(recipeName);
    }
}
