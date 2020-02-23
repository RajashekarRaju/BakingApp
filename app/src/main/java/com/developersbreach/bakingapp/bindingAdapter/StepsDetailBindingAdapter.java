package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class StepsDetailBindingAdapter {

    @BindingAdapter("stepDetailShortDescription")
    public static void bindStepDetailShortDescription(TextView textView, String detailShortDescription) {
        textView.setText(detailShortDescription);
    }

    @BindingAdapter("stepDetailDescription")
    public static void bindStepDetailDescription(TextView textView, String detailDescription) {
        textView.setText(detailDescription);
    }
}
