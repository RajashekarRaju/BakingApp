package com.developersbreach.bakingapp.recipeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    // Context to access our resources
    private final Context mContext;
    // List of sandwich objects, create and return the elements
    private final List<Recipe> mRecipeList;
    // Declaring custom listener for all click events
    private final RecipeAdapterListener mListener;
    // ViewModel to get all data from class
    private final RecipeListFragmentViewModel mViewModel;

    /**
     * Constructor for adapter class
     */
    RecipeAdapter(Context context, List<Recipe> recipeList, RecipeAdapterListener listener,
                  RecipeListFragmentViewModel viewModel) {
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mListener = listener;
        this.mViewModel = viewModel;
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

        // Views which are visible as single item in recycler view
        final ImageView mRecipeImageItemView;
        final TextView mRecipeNameTextItemView;
        final View mRecipeTextBackGroundItemView;

        private RecipeViewHolder(@NonNull final View itemView) {
            super(itemView);
            mRecipeImageItemView = itemView.findViewById(R.id.recipe_image_item_view);
            mRecipeNameTextItemView = itemView.findViewById(R.id.recipe_name_item_text_view);
            mRecipeTextBackGroundItemView = itemView.findViewById(R.id.recipe_textName_background_view);
        }
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
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

        // Running a executor on main thread and load data from ViewModel of recipe properties.
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.loadRecipeProperties(mContext, recipe, holder);
            }
        });

        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.loadRecipeThumbnails(mContext, recipe, holder);
            }
        });

        // Set listener using itemView and call onSandwichSelected from declared custom interface
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecipeSelected(recipe, view);
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
        return mRecipeList.size();
    }
}
