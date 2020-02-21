package com.developersbreach.bakingapp.step;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemStepBinding;
import com.developersbreach.bakingapp.model.Steps;

import java.util.List;

public class StepsAdapter extends ListAdapter<Steps, StepsAdapter.StepsViewHolder> {

    // List of sandwich objects, create and return the elements
    private final List<Steps> mStepsList;
    // Declaring custom listener for all click events
    private final StepsAdapterListener mListener;

    private final StepsFragmentViewModel mViewModel;

    /**
     * Constructor for adapter class
     */
    StepsAdapter(List<Steps> stepsList, StepsAdapterListener listener, StepsFragmentViewModel viewModel) {
        super(DIFF_ITEM_CALLBACK);
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

        private final ItemStepBinding mBinding;

        private StepsViewHolder(ItemStepBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        void bind(final Steps steps) {
            mBinding.setStep(steps);
            mBinding.executePendingBindings();
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_step, parent, false);
        return new StepsViewHolder(binding);
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
        holder.bind(steps);

        // Set listener using itemView and call onSandwichSelected from declared custom interface
        holder.itemView.setOnClickListener(view -> {
            String recipeName = mViewModel.getMutableRecipeName();
            mListener.onStepSelected(steps, recipeName, view);
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

    private static final DiffUtil.ItemCallback<Steps> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<Steps>() {

        @Override
        public boolean areItemsTheSame(@NonNull Steps oldItem, @NonNull Steps newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Steps oldItem, @NonNull Steps newItem) {
            return oldItem.getStepsId().equals(newItem.getStepsId());
        }
    };
}
