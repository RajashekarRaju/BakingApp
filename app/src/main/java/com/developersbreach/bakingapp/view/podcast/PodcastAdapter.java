package com.developersbreach.bakingapp.view.podcast;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemPodcastBinding;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.view.podcast.PodcastAdapter.PodcastViewHolder;
import com.developersbreach.bakingapp.view.recipeList.RecipeAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * This class implements a {@link RecyclerView} {@link ListAdapter} which uses Data Binding to
 * present list data, including computing diffs between lists.
 * <p>
 * {@link Podcast} type of list this adapter will receive.
 * {@link PodcastViewHolder} class that extends ViewHolder that will be used by the adapter.
 */
public class PodcastAdapter extends ListAdapter<Podcast, PodcastViewHolder> {

    /**
     * Declare a new list of podcast to return with data.
     */
    private final List<Podcast> mPodcastList;

    /**
     * The interface that receives onClick listener.
     */
    private final PodcastAdapterListener mListener;

    /**
     * @param podcastList creates list of podcasts.
     * @param listener    create click listener on itemView.
     */
    PodcastAdapter(List<Podcast> podcastList, PodcastAdapterListener listener) {
        super(DIFF_ITEM_CALLBACK);
        this.mPodcastList = podcastList;
        this.mListener = listener;
    }

    /**
     * The interface that receives onClick listener.
     */
    public interface PodcastAdapterListener {
        /**
         * @param podcast get recipes from selected list of podcast.
         */
        void onPodcastSelected(Podcast podcast);
    }

    /**
     * PodcastViewHolder class creates child view Podcast properties.
     */
    static class PodcastViewHolder extends RecyclerView.ViewHolder {

        // Get access to binding the views in layout
        private final ItemPodcastBinding mBinding;

        /**
         * @param binding binds each properties in {@link Recipe} list
         */
        private PodcastViewHolder(ItemPodcastBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        /**
         * @param podcast Pass object to set podcast binding. This binding is accessed from layout
         *                xml {@link R.layout#item_podcast}
         */
        void bind(final Podcast podcast) {
            mBinding.setPodcast(podcast);
            // Force DataBinding to execute binding views immediately.
            mBinding.executePendingBindings();
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link PodcastViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final PodcastViewHolder holder, final int position) {
        final Podcast podcast = mPodcastList.get(position);
        holder.bind(podcast);

        // Set click listener on itemView and pass arguments podcast for selected Podcasts.
        holder.itemView.setOnClickListener(
                view -> mListener.onPodcastSelected(podcast));

        // Set click listener on menu itemView.
        holder.mBinding.podcastOverflowMenuItemImageView.setOnClickListener(
                // Open new dialog when view clicked.
                view -> new MaterialAlertDialogBuilder(view.getContext())
                        .setView(R.layout.podcast_credit)
                        .show());
    }

    /**
     * Called when RecyclerView needs a new {@link PodcastViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Allow DataBinding to inflate the layout.
        ItemPodcastBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_podcast, parent,
                false);
        return new PodcastViewHolder(binding);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mPodcastList.size();
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the list of {@link Podcast}
     * has been updated.
     */
    private static final DiffUtil.ItemCallback<Podcast> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<Podcast>() {

        @Override
        public boolean areItemsTheSame(@NonNull Podcast oldItem, @NonNull Podcast newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Podcast oldItem, @NonNull Podcast newItem) {
            return oldItem.getPodcastRecipeId() == newItem.getPodcastRecipeId();
        }
    };
}
