package com.developersbreach.bakingapp;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developersbreach.bakingapp.ingredient.IngredientsAdapter;
import com.developersbreach.bakingapp.model.Ingredients;

import java.util.List;

public class BindingAdapters {


    @BindingAdapter("recipeImageView")
    public static void bindRecipeImageView(ImageView imageView, String recipeUrl) {
        Glide.with(imageView.getContext())
                .load(recipeUrl)
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter("recipeName")
    public static void bindRecipeName(TextView textView, String recipeName) {
        if ("".equals(recipeName)) {
            // If value not available we set something appropriate to it.
            textView.setText(textView.getContext().getString(R.string.name_not_available));
        } else {
            // If value is valid, show it to user
            textView.setText(recipeName);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    @BindingAdapter("ingredientsList")
    public static void bindIngredientsList(final RecyclerView recyclerView, final List<Ingredients> ingredientsList) {
        IngredientsAdapter adapter = new IngredientsAdapter();
        adapter.submitList(ingredientsList);
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("ingredientName")
    public static void bindIngredientName(TextView textView, String name) {
        textView.setText(name);
    }

    @BindingAdapter("ingredientQuantity")
    public static void bindIngredientQuantity(TextView textView, String quantity) {
        if ("0.5".equals(quantity)) {
            textView.setText("1/2");
        } else if ("1.5".equals(quantity)) {
            textView.setText("1 1/2");
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
}
