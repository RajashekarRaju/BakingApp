package com.developersbreach.bakingapp.view.recipeList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemRecipeBinding;
import com.developersbreach.bakingapp.model.Recipe;

import java.util.List;

import static com.developersbreach.bakingapp.view.recipeList.RecipeAdapter.RecipeViewHolder;


/**
 * This class implements a {@link RecyclerView} {@link ListAdapter} which uses Data Binding to
 * present list data, including computing diffs between lists.
 * <p>
 * {@link Recipe} type of list this adapter will receive.
 * {@link RecipeViewHolder} class that extends ViewHolder that will be used by the adapter.
 */
public class RecipeAdapter extends ListAdapter<Recipe, RecipeViewHolder> {

    /**
     * Declare a new list of recipes to return with data.
     */
    private final List<Recipe> mRecipeList;

    /**
     * The interface that receives onClick listener.
     */
    private final RecipeAdapterListener mListener;

    /**
     * @param recipeList creates list of recipes.
     * @param listener   create click listener on itemView.
     */
    RecipeAdapter(List<Recipe> recipeList, RecipeAdapterListener listener) {
        super(DIFF_ITEM_CALLBACK);
        this.mRecipeList = recipeList;
        this.mListener = listener;
    }

    /**
     * The interface that receives onClick listener.
     */
    public interface RecipeAdapterListener {
        /**
         * @param recipe get recipes from selected list of recipes.
         * @param view   used to create navigation with controller, which needs view.
         */
        void onRecipeSelected(Recipe recipe, View view);
    }

    /**
     * RecipeViewHolder class creates child view Recipe properties.
     */
    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        // Get access to binding the views in layout
        private final ItemRecipeBinding mBinding;

        /**
         * @param binding binds each properties in {@link Recipe} list
         */
        private RecipeViewHolder(final ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        /**
         * @param recipe pass object to set recipe for binding. This binding is accessed from layout
         *               xml {@link R.layout#item_recipe}
         */
        void bind(final Recipe recipe) {
            mBinding.setRecipe(recipe);
            // Force DataBinding to execute binding views immediately.
            mBinding.executePendingBindings();
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position. DataBinding should
     * update the contents of the {@link RecipeViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {
        final Recipe recipe = mRecipeList.get(position);
        holder.bind(recipe);

        // Set click listener on itemView and pass arguments recipe, view for selected recipe.
        holder.itemView.setOnClickListener(
                view -> mListener.onRecipeSelected(recipe, view));
    }

    /**
     * Called when RecyclerView needs a new {@link RecipeViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Allow DataBinding to inflate the layout.
        ItemRecipeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(binding);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the list of {@link Recipe}
     * has been updated.
     */
    private static final DiffUtil.ItemCallback<Recipe> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<Recipe>() {

        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.getRecipeId() == newItem.getRecipeId();
        }
    };
}
