package com.developersbreach.bakingapp.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.R;

import java.util.List;

public class WidgetItemAdapter extends RecyclerView.Adapter<WidgetItemAdapter.WidgetItemViewHolder> {

    // Context to access our resources
    private final Context mContext;
    // List of sandwich objects, create and return the elements
    private final List<WidgetItem> mWidgetItemList;
    // Declaring custom listener for all click events
    private final WidgetItemAdapterListener mListener;

    /**
     * Constructor for adapter class
     */
    WidgetItemAdapter(Context context, List<WidgetItem> widgetItemList, WidgetItemAdapterListener listener) {
        this.mContext = context;
        this.mWidgetItemList = widgetItemList;
        this.mListener = listener;
    }

    /**
     * The interface that receives onClick listener.
     */
    public interface WidgetItemAdapterListener {
        void onWidgetItemSelected(WidgetItem widgetItem, View view, int position);
    }

    /**
     * Children views for sandwich data
     */
    class WidgetItemViewHolder extends RecyclerView.ViewHolder {

        // Views which are visible as single item in recycler view
        final TextView mWidgetItemTextItemView;

        private WidgetItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            mWidgetItemTextItemView = itemView.findViewById(R.id.widget_item_text_view);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_widget, parent, false);
        return new WidgetItemViewHolder(view);
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
        holder.mWidgetItemTextItemView.setText(widgetItem.getRecipeName());

        // Set listener using itemView and call onSandwichSelected from declared custom interface
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onWidgetItemSelected(widgetItem, view, position);
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
        return mWidgetItemList.size();
    }
}
