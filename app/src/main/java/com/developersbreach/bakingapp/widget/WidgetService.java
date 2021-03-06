package com.developersbreach.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The service to be connected to for a remote adapter to request RemoteViews.  Users should
 * extend the RemoteViewsService to provide the appropriate RemoteViewsFactory's used to
 * populate the remote collection view (ListView, GridView, etc).
 */
public class WidgetService extends RemoteViewsService {

    /**
     * To be implemented by the derived service to generate appropriate factories for the data.
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    /**
     * Declare new list of data of type {@link Ingredients}
     */
    private List<Ingredients> mIngredientsList = new ArrayList<>();

    /**
     * Need a context for this class to get access for resources.
     */
    private final Context mContext;

    /**
     * Declare Id for app widget.
     */
    private final int mAppWidgetId;

    /**
     * Declare an id for getting the type of ingredient from selected recipe.
     */
    private int mRecipeWidgetId;

    WidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    /**
     * Called when your factory is first constructed. The same factory may be shared across
     * multiple RemoteViewAdapters depending on the intent passed.
     */
    @Override
    public void onCreate() {
        mRecipeWidgetId = RecipeWidgetConfigureActivity.loadIdPref(mContext, mAppWidgetId);
    }

    /**
     * Called when notifyDataSetChanged() is triggered on the remote adapter. This allows a
     * RemoteViewsFactory to respond to data changes by updating any internal references.
     * <p>
     * Note: expensive tasks can be safely performed synchronously within this method. In the
     * interim, the old data will be displayed within the widget.
     *
     * @see AppWidgetManager#notifyAppWidgetViewDataChanged(int[], int)
     */
    @Override
    public void onDataSetChanged() {
        try {
            // Try to start a response to get response string from ResponseBuilder.
            String responseString = ResponseBuilder.startResponse();
            // Get JSON data which returns list of ingredients objects with responseString.
            mIngredientsList = JsonUtils.fetchIngredientsJsonData(responseString, mRecipeWidgetId);
        } catch (IOException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("WidgetService", "Problem fetching WidgetIngredients", e);
        }
    }

    /**
     * Called when the last RemoteViewsAdapter that is associated with this factory is
     * unbound.
     */
    @Override
    public void onDestroy() {
        mIngredientsList.clear();
    }

    /**
     * See @link Adapter#getCount()}
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mIngredientsList.size();
    }

    /**
     * See @link Adapter#getView(int, View, ViewGroup)}.
     * <p>
     * Note: expensive tasks can be safely performed synchronously within this method, and a
     * loading view will be displayed in the interim. See {@link #getLoadingView()}.
     *
     * @param position The position of the item within the Factory's data set of the item whose
     *                 view we want.
     * @return A RemoteViews object corresponding to the data at the specified position.
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_ingredient);
        Ingredients ingredients = mIngredientsList.get(position);
        remoteViews.setTextViewText(R.id.ingredient_name_widget_item_text, ingredients.getIngredientsName());
        return remoteViews;
    }

    /**
     * This allows for the use of a custom loading view which appears between the time that
     * {@link #getViewAt(int)} is called and returns. If null is returned, a default loading
     * view will be used.
     *
     * @return The RemoteViews representing the desired loading view.
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /**
     * See @link Adapter#getViewTypeCount()}.
     *
     * @return The number of types of Views that will be returned by this factory.
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * See @link Adapter#getItemId(int)}.
     *
     * @param position The position of the item within the data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * See @link Adapter#hasStableIds()}.
     *
     * @return True if the same id always refers to the same object.
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }
}
