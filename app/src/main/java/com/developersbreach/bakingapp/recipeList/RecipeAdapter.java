package com.developersbreach.bakingapp.recipeList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemRecipeBinding;
import com.developersbreach.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {


    private final List<Recipe> mRecipeList;
    private final RecipeAdapterListener mListener;

    RecipeAdapter(List<Recipe> recipeList, RecipeAdapterListener listener) {
        super(DIFF_ITEM_CALLBACK);
        this.mRecipeList = recipeList;
        this.mListener = listener;
    }

    /**
     * The interface that receives onClick listener.
     */
    public interface RecipeAdapterListener {
        void onRecipeSelected(Recipe recipe, View view);
    }

    /**
     * Children views for sandwich data
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecipeBinding mBinding;

        private RecipeViewHolder(final ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;

            binding.recipeImageItemView.setOnClickListener(
                    view -> mListener.onRecipeSelected(mBinding.getRecipe(), view));
        }

        void bind(final Recipe recipe) {
            AppExecutors.getInstance().mainThread().execute(() -> {
                mBinding.setRecipe(recipe);
                mBinding.executePendingBindings();
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {
        final Recipe recipe = mRecipeList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
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
        ItemRecipeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(binding);
    }

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
