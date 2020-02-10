package com.developersbreach.bakingapp.ingredient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Ingredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    // Context to access our resources
    private final Context mContext;
    // List of sandwich objects, create and return the elements
    private final List<Ingredients> mIngredientsList;

    private final IngredientsFragmentViewModel mViewModel;


    /**
     * Constructor for adapter class
     */
    IngredientsAdapter(Context context, List<Ingredients> ingredientsList, IngredientsFragmentViewModel viewModel) {
        this.mContext = context;
        this.mIngredientsList = ingredientsList;
        this.mViewModel = viewModel;
    }

    /**
     * Children views for sandwich data
     */
    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        // Views which are visible as single item in recycler view
        final View mIngredientIndicatorItemView;
        final TextView mIngredientQuantityItemTextView;
        final TextView mIngredientMeasureItemTextView;
        final TextView mIngredientNameItemTextView;

        private IngredientsViewHolder(@NonNull final View itemView) {
            super(itemView);

            mIngredientIndicatorItemView = itemView.findViewById(R.id.ingredient_indicator_item_view);
            mIngredientQuantityItemTextView = itemView.findViewById(R.id.ingredient_quantity_item_text_view);
            mIngredientMeasureItemTextView = itemView.findViewById(R.id.ingredient_measure_item_text_view);
            mIngredientNameItemTextView = itemView.findViewById(R.id.ingredient_name_item_text_view);
        }
    }

    /**
     * Called when RecyclerView needs a new {@link IngredientsAdapter.IngredientsViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientsViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link IngredientsViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final IngredientsViewHolder holder, final int position) {
        final Ingredients ingredients = mIngredientsList.get(position);

        // Running a executor on main thread and load data from ViewModel of recipe properties.
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.loadIngredientsData(mContext, ingredients, holder);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }
}
