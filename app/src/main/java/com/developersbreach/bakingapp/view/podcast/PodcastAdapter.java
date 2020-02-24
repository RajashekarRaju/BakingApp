package com.developersbreach.bakingapp.view.podcast;

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
import com.developersbreach.bakingapp.databinding.ItemPodcastBinding;
import com.developersbreach.bakingapp.model.Podcast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class PodcastAdapter extends ListAdapter<Podcast, PodcastAdapter.PodcastViewHolder> {

    // List of sandwich objects, create and return the elements
    private final List<Podcast> mPodcastList;
    // Declaring custom listener for all click events
    private final PodcastAdapterListener mListener;

    /**
     * Constructor for adapter class
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
        void onPodcastSelected(Podcast podcast, View view);
    }

    /**
     * Children views for sandwich data
     */
    class PodcastViewHolder extends RecyclerView.ViewHolder {

        private final ItemPodcastBinding mBinding;

        private PodcastViewHolder(ItemPodcastBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;

            binding.getRoot().setOnClickListener(
                    view -> mListener.onPodcastSelected(mBinding.getPodcast(), view));

            binding.podcastOverflowMenuItemImageView.setOnClickListener(
                    view -> new MaterialAlertDialogBuilder(view.getContext())
                            .setView(R.layout.podcast_credit)
                            .show());
        }

        void bind(final Podcast podcast) {
            AppExecutors.getInstance().mainThread().execute(() -> {
                mBinding.setPodcast(podcast);
                mBinding.executePendingBindings();
            });
        }
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
        ItemPodcastBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_podcast, parent, false);
        return new PodcastViewHolder(binding);
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
