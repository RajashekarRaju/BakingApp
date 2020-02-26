package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.developersbreach.bakingapp.view.stepDetail.StepDetailFragment;
import com.developersbreach.bakingapp.viewModel.StepDetailFragmentViewModel;

/**
 * BindingAdapter for fragment class {@link StepDetailFragment} and ViewModel class
 * {@link StepDetailFragmentViewModel}
 */
public class StepsDetailBindingAdapter {

    /**
     * When stepDetailDescription is used on TextView, the method bindStepDetailDescription is called.
     *
     * @param textView          a view which we use to set a String value to it.
     * @param detailDescription contains String value to be set to TextView.
     */
    @BindingAdapter("stepDetailDescription")
    public static void bindStepDetailDescription(TextView textView, String detailDescription) {
        textView.setText(detailDescription);
    }
}
