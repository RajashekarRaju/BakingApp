package com.developersbreach.bakingapp.ingredient;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemIngredientBinding;
import com.developersbreach.bakingapp.model.Ingredients;

public class IngredientsAdapter extends ListAdapter<Ingredients, IngredientsAdapter.IngredientsViewHolder> {

    /**
     * Constructor for adapter class
     */
    public IngredientsAdapter() {
        super(DIFF_ITEM_CALLBACK);
    }

    /**
     * Children views for sandwich data
     */
    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        private final ItemIngredientBinding mBinding;

        private IngredientsViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        void bind(final Ingredients ingredients) {
            AppExecutors.getInstance().mainThread().execute(() -> {
                mBinding.setIngredients(ingredients);
                mBinding.executePendingBindings();
            });
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemIngredientBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_ingredient, parent, false);
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
