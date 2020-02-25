package com.developersbreach.bakingapp.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemRecipeWidgetBinding;

import java.util.List;

public class WidgetItemAdapter extends RecyclerView.Adapter<WidgetItemAdapter.WidgetItemViewHolder> {


    // List of sandwich objects, create and return the elements
    private final List<WidgetItem> mWidgetItemList;
    // Declaring custom listener for all click events
    private final WidgetItemAdapterListener mListener;

    /**
     * Constructor for adapter class
     */
    WidgetItemAdapter(List<WidgetItem> widgetItemList, WidgetItemAdapterListener listener) {
        this.mWidgetItemList = widgetItemList;
        this.mListener = listener;
    }

    /**
     * The interface that receives onClick listener.
     */
    public interface WidgetItemAdapterListener {
        void onWidgetItemSelected(WidgetItem widgetItem, int position);
    }

    /**
     * Children views for sandwich data
     */
    static class WidgetItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecipeWidgetBinding mBinding;

        private WidgetItemViewHolder(final ItemRecipeWidgetBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        void bind(final WidgetItem widgetItem) {
            AppExecutors.getInstance().mainThread().execute(() -> {
                mBinding.setWidgetItem(widgetItem);
                mBinding.executePendingBindings();
            });
        }
    }

    /**
     * Called when RecyclerView needs a new {@link WidgetItemViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public WidgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRecipeWidgetBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_recipe_widget, parent, false);
        return new WidgetItemViewHolder(binding);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link WidgetItemViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final WidgetItemViewHolder holder, final int position) {
        final WidgetItem widgetItem = mWidgetItemList.get(position);
        holder.bind(widgetItem);

        // Set listener using itemView and call onSandwichSelected from declared custom interface
        holder.itemView.setOnClickListener(view -> mListener.onWidgetItemSelected(widgetItem, position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mWidgetItemList.size();
    }
}
