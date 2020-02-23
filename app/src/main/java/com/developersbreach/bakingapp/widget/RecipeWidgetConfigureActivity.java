package com.developersbreach.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.developersbreach.bakingapp.AppExecutors;
import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.RecipeWidgetConfigureBinding;
import com.developersbreach.bakingapp.utils.JsonUtils;
import com.developersbreach.bakingapp.utils.ResponseBuilder;

import java.io.IOException;
import java.util.List;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME_RECIPE_ID = "com.developersbreach.bakingapp.widget.RecipeId";
    private static final String PREFS_NAME_RECIPE_NAME = "com.developersbreach.bakingapp.widget.RecipeName";
    private static final String PREF_PREFIX_KEY_ID = "appwidget_recipe_id";
    private static final String PREF_PREFIX_KEY_NAME = "appwidget_recipe_name";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public RecipeWidgetConfigureActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.recipe_widget_configure);

        RecipeWidgetConfigureBinding binding = DataBindingUtil.setContentView(this, R.layout.recipe_widget_configure);

        RecyclerView recyclerView = binding.widgetRecyclerView;

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseString = ResponseBuilder.startResponse();
                    List<WidgetItem> widgetItemList = JsonUtils.fetchWidgetJsonData(responseString);
                    runOnMainThread(widgetItemList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void runOnMainThread(List<WidgetItem> widgetItemList) {
                AppExecutors.getInstance().mainThread().execute(() -> {
                    WidgetItemAdapter adapter = new WidgetItemAdapter(widgetItemList,
                            new WidgetItemListener());
                    recyclerView.setAdapter(adapter);
                });
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

    static void saveIdPref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefsRecipeId = context.getSharedPreferences(PREFS_NAME_RECIPE_ID, 0).edit();
        prefsRecipeId.putInt(PREF_PREFIX_KEY_ID + appWidgetId, recipeId);
        prefsRecipeId.apply();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String recipeName) {
        SharedPreferences.Editor prefsRecipeName = context.getSharedPreferences(PREFS_NAME_RECIPE_NAME, 0).edit();
        prefsRecipeName.putString(PREF_PREFIX_KEY_NAME + appWidgetId, recipeName);
        prefsRecipeName.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadIdPref(Context context, int appWidgetId) {
        SharedPreferences prefsId = context.getSharedPreferences(PREFS_NAME_RECIPE_ID, 0);
        return prefsId.getInt(PREF_PREFIX_KEY_ID + appWidgetId, 0);
    }

    static String loadNamePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_RECIPE_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY_NAME + appWidgetId, null);
    }

    static void deleteIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefsId = context.getSharedPreferences(PREFS_NAME_RECIPE_ID, 0).edit();
        prefsId.remove(PREF_PREFIX_KEY_ID + appWidgetId);
        prefsId.apply();
    }

    static void deleteNamePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefsName = context.getSharedPreferences(PREFS_NAME_RECIPE_NAME, 0).edit();
        prefsName.remove(PREF_PREFIX_KEY_NAME + appWidgetId);
        prefsName.apply();
    }

    private class WidgetItemListener implements WidgetItemAdapter.WidgetItemAdapterListener {

        @Override
        public void onWidgetItemSelected(WidgetItem widgetItem, View view, int position) {
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
}

