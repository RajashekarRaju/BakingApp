package com.developersbreach.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.network.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.RecipeWidgetConfigureBinding;
import com.developersbreach.bakingapp.network.JsonUtils;
import com.developersbreach.bakingapp.network.ResponseBuilder;

import java.io.IOException;
import java.util.List;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 * <p>
 * When the user tries to add our app widget to the home screen, this activity triggers between.
 * This activity shows list data of type {@link WidgetItem}.
 * <p>
 * This fragment shows list data using {@link WidgetItemAdapter} {@link RecyclerView} with
 * * {@link WidgetItem} and are bind using {@link DataBindingUtil}
 */
public class RecipeWidgetConfigureActivity extends Activity {

    /**
     * Declare string variable to store shared preferences recipe id.
     */
    private static final String PREFS_NAME_RECIPE_ID = "com.developersbreach.bakingapp.widget.RecipeId";

    /**
     * Declare string variable to store shared preferences recipe name.
     */
    private static final String PREFS_NAME_RECIPE_NAME = "com.developersbreach.bakingapp.widget.RecipeName";

    /**
     * Declare string variable to store shared preferences key for recipe id.
     */
    private static final String PREF_PREFIX_KEY_ID = "appwidget_recipe_id";

    /**
     * Declare string variable to store shared preferences key for recipe name.
     */
    private static final String PREF_PREFIX_KEY_NAME = "appwidget_recipe_name";

    /**
     * Declare int variable for getting this app widget with ID.
     */
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    // Empty constructor
    public RecipeWidgetConfigureActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the result for this activity will return to its caller.
        setResult(RESULT_CANCELED);
        // Inflating fragment layout with DataBinding.
        RecipeWidgetConfigureBinding binding = DataBindingUtil.setContentView(this, R.layout.recipe_widget_configure);
        //  Get reference and bind recyclerView.
        RecyclerView recyclerView = binding.widgetRecyclerView;
        // Start a new background thread an execute a new task to fetch required data.
        AppExecutors.getInstance().backgroundThread().execute(() -> {
            try {
                // Try to start a response to get response string from ResponseBuilder.
                String responseString = ResponseBuilder.startResponse();
                // Get JSON data which returns list of widget items objects with responseString.
                List<WidgetItem> widgetItemList = JsonUtils.fetchWidgetJsonData(responseString);
                // After completing the query, start a nwe main thread to set views for fragment.
                runOnMainThread(widgetItemList, recyclerView);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("WidgetConfigureActivity", "Problem fetching WidgetItems", e);
            }
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    /**
     * @param widgetItemList contains the list data of type {@link WidgetItem}.
     * @param recyclerView   show the list with {@link RecyclerView}.
     */
    private void runOnMainThread(List<WidgetItem> widgetItemList, RecyclerView recyclerView) {
        // Start the main thread and execute the task to set this fragment views.
        AppExecutors.getInstance().mainThread().execute(() -> {
            // Pass data to adapter and create new listener for each items in steps list.
            WidgetItemAdapter adapter = new WidgetItemAdapter(widgetItemList, new WidgetItemListener());
            // Set adapter to this fragments RecyclerView.
            recyclerView.setAdapter(adapter);
        });
    }

    /**
     * Create listener for items in widget list by calling interface from {@link WidgetItemAdapter}.
     */
    private class WidgetItemListener implements WidgetItemAdapter.WidgetItemAdapterListener {

        @Override
        public void onWidgetItemSelected(WidgetItem widgetItem, int position) {
            // Save preference for selected widget prefix, name and id.
            saveIdPref(getApplicationContext(), mAppWidgetId, position);
            saveTitlePref(getApplicationContext(), mAppWidgetId, widgetItem.getRecipeName());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            RecipeWidget.updateAppWidget(getApplicationContext(), appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

    /**
     * Save the recipe id in shared preferences.
     * Write the prefix to the SharedPreferences object for this widget.
     */
    private static void saveIdPref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefsRecipeId = context.getSharedPreferences(PREFS_NAME_RECIPE_ID, 0).edit();
        prefsRecipeId.putInt(PREF_PREFIX_KEY_ID + appWidgetId, recipeId);
        prefsRecipeId.apply();
    }

    /**
     * Save the recipe name in shared preferences.
     * Write the prefix to the SharedPreferences object for this widget.
     */
    private static void saveTitlePref(Context context, int appWidgetId, String recipeName) {
        SharedPreferences.Editor prefsRecipeName = context.getSharedPreferences(PREFS_NAME_RECIPE_NAME, 0).edit();
        prefsRecipeName.putString(PREF_PREFIX_KEY_NAME + appWidgetId, recipeName);
        prefsRecipeName.apply();
    }

    /**
     * Read the prefix from the SharedPreferences object for this widget.
     * If there is no preference saved, get the default from a resource.
     * Load recipe id which is saved earlier in preference id.
     */
    static int loadIdPref(Context context, int appWidgetId) {
        SharedPreferences prefsId = context.getSharedPreferences(PREFS_NAME_RECIPE_ID, 0);
        return prefsId.getInt(PREF_PREFIX_KEY_ID + appWidgetId, 0);
    }

    /**
     * Read the prefix from the SharedPreferences object for this widget.
     * If there is no preference saved, get the default from a resource.
     * Load recipe name which is saved earlier in preference id.
     */
    static String loadNamePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_RECIPE_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY_NAME + appWidgetId, null);
    }

    /**
     * When widget is removed or deleted from home screen, delete the preferences too which contains
     * ID for our recipe from sharedPreferences.
     */
    static void deleteIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefsId = context.getSharedPreferences(PREFS_NAME_RECIPE_ID, 0).edit();
        prefsId.remove(PREF_PREFIX_KEY_ID + appWidgetId);
        prefsId.apply();
    }

    /**
     * When widget is removed or deleted from home screen, delete the preferences too which contains
     * name for our recipe from sharedPreferences.
     */
    static void deleteNamePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefsName = context.getSharedPreferences(PREFS_NAME_RECIPE_NAME, 0).edit();
        prefsName.remove(PREF_PREFIX_KEY_NAME + appWidgetId);
        prefsName.apply();
    }
}
