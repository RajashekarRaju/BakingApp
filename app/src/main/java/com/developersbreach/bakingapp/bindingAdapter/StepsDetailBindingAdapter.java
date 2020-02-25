package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class StepsDetailBindingAdapter {

    @BindingAdapter("stepDetailDescription")
    public static void bindStepDetailDescription(TextView textView, String detailDescription) {
        textView.setText(detailDescription);
    }
}
