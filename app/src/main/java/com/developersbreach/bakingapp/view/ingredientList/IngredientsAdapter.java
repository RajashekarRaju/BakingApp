package com.developersbreach.bakingapp.view.ingredientList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemIngredientBinding;
import com.developersbreach.bakingapp.model.Ingredients;

import static com.developersbreach.bakingapp.view.ingredientList.IngredientsAdapter.*;

/**
 * This class implements a {@link RecyclerView} {@link ListAdapter} which uses Data Binding to
 * present list data, including computing diffs between lists.
 * <p>
 * {@link Recipe} type of list this adapter will receive.
 * {@link IngredientsViewHolder} class that extends ViewHolder that will be used by the adapter.
 */
public class IngredientsAdapter extends ListAdapter<Ingredients, IngredientsViewHolder> {

    /**
     * Constructor for adapter class
     */
    public IngredientsAdapter() {
        super(DIFF_ITEM_CALLBACK);
    }

    /**
     * IngredientsViewHolder class creates child view Ingredient properties.
     */
    static class IngredientsViewHolder extends RecyclerView.ViewHolder {

        // Get access to binding the views in layout
        private final ItemIngredientBinding mBinding;

        /**
         * @param binding binds each properties in {@link Ingredients} list
         */
        private IngredientsViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        /**
         * @param ingredients pass object to set ingredient for binding. This binding is accessed
         *                    from layout xml {@link R.layout#item_ingredient}
         */
        void bind(final Ingredients ingredients) {
            mBinding.setIngredients(ingredients);
            // Force DataBinding to execute binding views immediately.
            mBinding.executePendingBindings();
        }
    }

    /**
     * Called when RecyclerView needs a new {@link IngredientsViewHolder} of the given type to represent
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Allow DataBinding to inflate the layout.
        ItemIngredientBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_ingredient,
                parent, false);
        return new IngredientsViewHolder(binding);
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
        final Ingredients ingredients = getItem(position);
        holder.bind(ingredients);
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the list of {@link Recipe}
     * has been updated.
     */
    private static final DiffUtil.ItemCallback<Ingredients> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<Ingredients>() {

        @Override
        public boolean areItemsTheSame(@NonNull Ingredients oldItem, @NonNull Ingredients newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ingredients oldItem, @NonNull Ingredients newItem) {
            return oldItem.getIngredientsName().equals(newItem.getIngredientsName());
        }
    };
}
