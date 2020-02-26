package com.developersbreach.bakingapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.bindingAdapter.IngredientsListBindingAdapter;
import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;


/**
 * An ItemDecoration allows the application to add a special drawing and layout offset to specific
 * item views from the adapter's data set. This can be useful for drawing dividers between items,
 * highlights, visual grouping boundaries and more.
 * <p>
 * In our app we are using in {@link IngredientsFragment} written in {@link IngredientsListBindingAdapter}
 */
public class DividerItemDecorator extends RecyclerView.ItemDecoration {

    // Our view horizontal divider is type of drawable.
    private final Drawable mDivider;

    /**
     * @param context need to pass as argument for getting drawable.
     */
    public DividerItemDecorator(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.list_divider);
    }

    /**
     * @param canvas       used to draw our drawable divider line.
     * @param recyclerView needs divider after getting view size type of bounds.
     * @param state        contains useful information about the current RecyclerView state like
     *                     target scroll position or view focus.
     */
    @Override
    public void onDrawOver(@NonNull Canvas canvas, RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        // Spacing to the left of RecyclerView
        int left = 120;
        // Get equal spacing between left and right to RecyclerView
        int right = recyclerView.getWidth() - left;

        // Get number of items in RecyclerView to add calculate number of list drawables.
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            // Set final bounds for view
            mDivider.setBounds(left, top, right, bottom);
            // With canvas draw final view
            mDivider.draw(canvas);
        }
    }
}
