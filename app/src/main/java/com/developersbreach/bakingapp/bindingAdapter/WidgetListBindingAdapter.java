package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.developersbreach.bakingapp.widget.RecipeWidgetConfigureActivity;
import com.developersbreach.bakingapp.widget.WidgetItem;

/**
 * BindingAdapter for Activity class {@link RecipeWidgetConfigureActivity} with model
 * {@link WidgetItem}
 */
public class WidgetListBindingAdapter {

    /**
     * When widgetRecipeName is used on TextView, the method bindWidgetRecipeName is called.
     *
     * @param textView   a view which we use to set a String value to it.
     * @param recipeName contains String value to be set to TextView.
     */
    @BindingAdapter("widgetRecipeName")
    public static void bindWidgetRecipeName(TextView textView, String recipeName) {
        textView.setText(recipeName);
    }
}
