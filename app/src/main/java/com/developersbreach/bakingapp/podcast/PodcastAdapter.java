package com.developersbreach.bakingapp.podcast;

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
import com.developersbreach.bakingapp.model.Podcast;

import java.util.List;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {

    // Context to access our resources
    private final Context mContext;
    // List of sandwich objects, create and return the elements
    private final List<Podcast> mPodcastList;
    // Declaring custom listener for all click events
    private final PodcastAdapterListener mListener;
    // ViewModel to get all data from class
    private final PodcastFragmentViewModel mViewModel;

    /**
     * Constructor for adapter class
     */
    PodcastAdapter(Context context, List<Podcast> podcastList, PodcastAdapterListener listener,
                   PodcastFragmentViewModel viewModel) {
        this.mContext = context;
        this.mPodcastList = podcastList;
        this.mListener = listener;
        this.mViewModel = viewModel;
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

        // Views which are visible as single item in recycler view
        final ImageView mPodcastPlayImageItemView;
        final ImageView mPodcastRecipeImageView;
        final TextView mPodcastRecipeNameTextItemView;
        final TextView mPodcastRecipeDurationTextItemView;

        private PodcastViewHolder(@NonNull final View itemView) {
            super(itemView);
            mPodcastPlayImageItemView = itemView.findViewById(R.id.podcast_play_image_view);
            mPodcastRecipeImageView = itemView.findViewById(R.id.podcast_recipe_image_view);
            mPodcastRecipeNameTextItemView = itemView.findViewById(R.id.podcast_recipe_name);
            mPodcastRecipeDurationTextItemView = itemView.findViewById(R.id.podcast_recipe_total_duration);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_podcast, parent, false);
        return new PodcastViewHolder(view);
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

        // Running a executor on main thread and load data from ViewModel of recipe properties.
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.loadPodcastProperties(mContext, podcast, holder);
            }
        });

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.loadPodcastSteps(podcast, holder);
            }
        });

        // Set listener using itemView and call onSandwichSelected from declared custom interface
        holder.mPodcastPlayImageItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPodcastSelected(podcast, view);
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
        return mPodcastList.size();
    }
}
