package com.developersbreach.bakingapp.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.view.ingredientList.IngredientsAdapter;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.utils.DividerItemDecorator;

import java.util.List;

public class IngredientsListBindingAdapter {

    @BindingAdapter("ingredientsList")
    public static void bindIngredientsList(final RecyclerView recyclerView, final List<Ingredients> ingredientsList) {
        IngredientsAdapter adapter = new IngredientsAdapter();
        adapter.submitList(ingredientsList);
        DividerItemDecorator decorator = new DividerItemDecorator(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decorator);
    }

    @BindingAdapter("ingredientName")
    public static void bindIngredientName(TextView textView, String name) {
        textView.setText(name);
    }

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
