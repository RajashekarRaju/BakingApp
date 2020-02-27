package com.developersbreach.bakingapp.view.stepList;

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
import com.developersbreach.bakingapp.viewModel.StepsFragmentViewModel;

import java.util.List;

import static com.developersbreach.bakingapp.view.stepList.StepsAdapter.StepsViewHolder;

/**
 * This class implements a {@link RecyclerView} {@link ListAdapter} which uses Data Binding to
 * present list data, including computing diffs between lists.
 * <p>
 * {@link Steps} type of list this adapter will receive.
 * {@link StepsViewHolder} class that extends ViewHolder that will be used by the adapter.
 */
public class StepsAdapter extends ListAdapter<Steps, StepsViewHolder> {

    /**
     * Declare a new list of steps to return with data.
     */
    private final List<Steps> mStepsList;

    /**
     * The interface that receives onClick listener.
     */
    private final StepsAdapterListener mListener;

    /**
     * Declare {@link StepsFragmentViewModel} to access externally exposed recipe data.
     */
    private final StepsFragmentViewModel mViewModel;

    /**
     * @param stepsList creates list of steps for selected recipe.
     * @param listener  create click listener on itemView.
     * @param viewModel get access to hte ViewModel.
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
        /**
         * @param steps      get step data by position clicked to return step details.
         * @param recipeName get name of the recipe which user currently viewing.
         * @param view       using view to find navigation controller for navigating.
         */
        void onStepSelected(Steps steps, String recipeName, View view);
    }

    /**
     * StepsViewHolder class creates child view Step properties.
     */
    static class StepsViewHolder extends RecyclerView.ViewHolder {

        // Get access to binding the views in layout
        private final ItemStepBinding mBinding;

        /**
         * @param binding binds each properties in {@link Steps} list
         */
        private StepsViewHolder(ItemStepBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        /**
         * @param steps pass object to set steps for binding. This binding is accessed from layout
         *              xml {@link R.layout#item_step}
         */
        void bind(final Steps steps) {
            mBinding.setStep(steps);
            // Force DataBinding to execute binding views immediately.
            mBinding.executePendingBindings();
        }
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

        // Set listener on itemView and call onStepSelected with passing steps, recipeName, view.
        holder.itemView.setOnClickListener(view -> {
            // Get selected recipe name in StepDetailsFragment from ViewModel
            String recipeName = mViewModel.recipeName();
            mListener.onStepSelected(steps, recipeName, view);
        });
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
        // Allow DataBinding to inflate the layout.
        ItemStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_step, parent,
                false);
        return new StepsViewHolder(binding);
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

    /**
     * Allows the RecyclerView to determine which items have changed when the list of {@link Steps}
     * has been updated.
     */
    private static final DiffUtil.ItemCallback<Steps> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<Steps>() {

        @Override
        public boolean areItemsTheSame(@NonNull Steps oldItem, @NonNull Steps newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Steps oldItem, @NonNull Steps newItem) {
            return oldItem.getStepsId() == newItem.getStepsId();
        }
    };
}
