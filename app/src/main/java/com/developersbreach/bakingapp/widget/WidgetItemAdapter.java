package com.developersbreach.bakingapp.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.ItemRecipeWidgetBinding;

import java.util.List;

import static com.developersbreach.bakingapp.widget.WidgetItemAdapter.WidgetItemViewHolder;

/**
 * This class implements a {@link RecyclerView} which uses Data Binding to present list data,
 * including computing diffs between lists.
 * <p>
 * {@link WidgetItem} type of list this adapter will receive.
 * {@link WidgetItemViewHolder} class that extends ViewHolder that will be used by the adapter.
 */
public class WidgetItemAdapter extends RecyclerView.Adapter<WidgetItemViewHolder> {

    /**
     * Declare a new list of recipes for WidgetItems to return with data.
     */
    private final List<WidgetItem> mWidgetItemList;

    /**
     * The interface that receives onClick listener.
     */
    private final WidgetItemAdapterListener mListener;

    /**
     * @param widgetItemList creates list of widget items.
     * @param listener       create click listener on itemView.
     */
    WidgetItemAdapter(List<WidgetItem> widgetItemList, WidgetItemAdapterListener listener) {
        this.mWidgetItemList = widgetItemList;
        this.mListener = listener;
    }

    /**
     * The interface that receives onClick listener.
     */
    public interface WidgetItemAdapterListener {
        /**
         * @param widgetItem get recipes from selected list of recipes.
         * @param position   needs position to receive click item feedback.
         */
        void onWidgetItemSelected(WidgetItem widgetItem, int position);
    }

    /**
     * WidgetItemViewHolder class creates child view WidgetItem properties.
     */
    static class WidgetItemViewHolder extends RecyclerView.ViewHolder {

        // Get access to binding the views in layout
        private final ItemRecipeWidgetBinding mBinding;

        /**
         * @param binding binds each properties in {@link WidgetItem} list
         */
        private WidgetItemViewHolder(final ItemRecipeWidgetBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        /**
         * @param widgetItem pass object to set recipe for binding. This binding is accessed from layout
         *                   xml {@link R.layout#item_recipe_widget}
         */
        void bind(final WidgetItem widgetItem) {
            mBinding.setWidgetItem(widgetItem);
            // Force DataBinding to execute binding views immediately.
            mBinding.executePendingBindings();
        }
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

        // Set listener using itemView and call onWidgetItemSelected passing widgetItem and position.
        holder.itemView.setOnClickListener(view -> mListener.onWidgetItemSelected(widgetItem, position));
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
        // Allow DataBinding to inflate the layout.
        ItemRecipeWidgetBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_recipe_widget,
                parent, false);
        return new WidgetItemViewHolder(binding);
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
