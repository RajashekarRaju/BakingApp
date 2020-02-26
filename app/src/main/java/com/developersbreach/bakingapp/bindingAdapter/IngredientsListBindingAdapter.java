package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.view.ingredientList.IngredientsAdapter;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.utils.DividerItemDecorator;
import com.developersbreach.bakingapp.view.ingredientList.IngredientsFragment;
import com.developersbreach.bakingapp.viewModel.IngredientsFragmentViewModel;

import java.util.List;

/**
 * BindingAdapter for fragment class {@link IngredientsFragment} with model {@link Ingredients} and
 * ViewModel class {@link IngredientsFragmentViewModel}
 */
public class IngredientsListBindingAdapter {

    /**
     * Submit a new list of ingredients calling submitList.
     * Set custom list divider for RecyclerView using class {@link DividerItemDecorator}
     *
     * @param recyclerView    set with {@link IngredientsAdapter} which shows list of ingredients.
     * @param ingredientsList contains LiveData objects from {@link IngredientsFragmentViewModel}.
     */
    @BindingAdapter("ingredientsList")
    public static void bindIngredientsList(final RecyclerView recyclerView, final List<Ingredients> ingredientsList) {
        IngredientsAdapter adapter = new IngredientsAdapter();
        adapter.submitList(ingredientsList);
        DividerItemDecorator decorator = new DividerItemDecorator(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decorator);
    }

    /**
     * When ingredientName is used on TextView, the method bindIngredientName is called.
     *
     * @param textView a view which we use to set a String value to it.
     * @param name     contains String value to be set to TextView.
     */
    @BindingAdapter("ingredientName")
    public static void bindIngredientName(TextView textView, String name) {
        textView.setText(name);
    }

    /**
     * When ingredientQuantity is used on TextView, the method bindIngredientQuantity is called.
     * Inside this method we do perform string formatting by reading and changing their values.
     *
     * @param textView a view which we use to set a String value to it.
     * @param quantity contains String value to be set to TextView.
     */
    @BindingAdapter("ingredientQuantity")
    public static void bindIngredientQuantity(TextView textView, String quantity) {
        if ("0.5".equals(quantity)) {
            textView.setText(R.string.ingredient_quantity_half_value);
        } else if ("1.5".equals(quantity)) {
            textView.setText(R.string.ingredient_quantity_one_half_value);
        } else {
            textView.setText(quantity);
        }
    }

    /**
     * When ingredientMeasure is used on TextView, the method bindIngredientMeasure is called.
     * Inside this method we do perform string formatting by reading and changing their values.
     *
     * @param textView a view which we use to set a String value to it.
     * @param measure  contains String value to be set to TextView.*
     */
    @BindingAdapter("ingredientMeasure")
    public static void bindIngredientMeasure(TextView textView, String measure) {
        if ("G".equals(measure)) {
            textView.setText(R.string.measure_grams);
        } else if ("CUP".equals(measure)) {
            textView.setText(R.string.measure_cup);
        } else if ("TBLSP".equals(measure)) {
            textView.setText(R.string.measure_table_spoon);
        } else if ("K".equals(measure)) {
            textView.setText(R.string.measure_kilogram);
        } else if ("OZ".equals(measure)) {
            textView.setText(R.string.measure_ounce);
        } else if ("TSP".equals(measure)) {
            textView.setText(R.string.measure_tea_spoon);
        } else if ("UNIT".equals(measure)) {
            textView.setText(R.string.measure_no_units);
        } else {
            textView.setText(measure);
        }
    }
}
