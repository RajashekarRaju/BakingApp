package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class WidgetListBindingAdapter {

    @BindingAdapter("widgetRecipeName")
    public static void bindWidgetRecipeName(TextView textView, String recipeName) {
        textView.setText(recipeName);
    }
}
