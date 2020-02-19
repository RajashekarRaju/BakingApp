package com.developersbreach.bakingapp.step;

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
import com.developersbreach.bakingapp.model.Steps;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    // Context to access our resources
    private final Context mContext;
    // List of sandwich objects, create and return the elements
    private final List<Steps> mStepsList;
    // Declaring custom listener for all click events
    private final StepsAdapterListener mListener;
    // ViewModel to get all data from class
    private final StepsFragmentViewModel mViewModel;


    /**
     * Constructor for adapter class
     */
    StepsAdapter(Context context, List<Steps> stepsList, StepsAdapterListener listener, StepsFragmentViewModel viewModel) {
        this.mContext = context;
        this.mStepsList = stepsList;
        this.mListener = listener;
        this.mViewModel = viewModel;
    }

    /**
     * The interface that receives onClick listener.
     */
    public interface StepsAdapterListener {
        void onStepSelected(Steps steps, String recipeName, View view);
    }

    /**
     * Children views for sandwich data
     */
    class StepsViewHolder extends RecyclerView.ViewHolder {

        // Views which are visible as single item in recycler view
        final TextView mStepShortDescriptionItemTextView;
        final ImageView mStepsVideoPlayArrowItemImageView;
        final TextView mVideoSizeTextView;

        private StepsViewHolder(@NonNull final View itemView) {
            super(itemView);
            mStepShortDescriptionItemTextView = itemView.findViewById(R.id.step_shortDescription_item_text_view);
            mStepsVideoPlayArrowItemImageView = itemView.findViewById(R.id.step_play_arrow_item_image_view);
            mVideoSizeTextView = itemView.findViewById(R.id.video_size);
        }
    }

    /**
     * Called when RecyclerView needs a new {@link StepsViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_step, parent, false);
        return new StepsViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link StepsViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final StepsViewHolder holder, final int position) {
        final Steps steps = mStepsList.get(position);

        // Running a executor on main thread and load data from ViewModel of recipe properties.
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.loadStepsData(mContext, steps, holder);
            }
        });

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.loadVideoDurationInBackground(steps, holder);
            }
        });

        // Set listener using itemView and call onSandwichSelected from declared custom interface
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeName = mViewModel.getMutableRecipeName();
                mListener.onStepSelected(steps, recipeName, view);
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
        return mStepsList.size();
    }
}
