package com.developersbreach.bakingapp.network;


import android.util.Log;

import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.utils.FormatUtils;
import com.developersbreach.bakingapp.view.recipeDetail.RecipeDetailFragment;
import com.developersbreach.bakingapp.viewModel.IngredientsFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.PodcastFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.RecipeListFragmentViewModel;
import com.developersbreach.bakingapp.viewModel.StepsFragmentViewModel;
import com.developersbreach.bakingapp.widget.RecipeWidgetConfigureActivity;
import com.developersbreach.bakingapp.widget.WidgetItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all methods which needs JSON data to show.
 * This code implements the parsing from URL below
 * https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
 * <p>
 * We perform this parsing operations in background thread using executors.
 */
public class JsonUtils {

    // These string values are parsed and declared here to re-use without changes.
    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";

    private static final String INGREDIENTS_ARRAY = "ingredients";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";

    private static final String STEPS_ARRAY = "steps";
    private static final String STEPS_ID = "id";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";

    /**
     * @param json takes as a String parameter.
     * @return list of recipes {@link Recipe} that has been built up from parsing a JSON response in
     * background see {@link RecipeListFragmentViewModel#fetchRecipeJsonData()}
     */
    public static List<Recipe> fetchRecipeJsonData(String json) {

        // Create a new ArrayList for adding recipes into list.
        List<Recipe> recipeList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONArray from the json response string
            JSONArray baseJsonArray = new JSONArray(json);
            // Loop inside each objects of array
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject jsonObject = baseJsonArray.getJSONObject(i);

                // Extract the value for the key called "id"
                int recipeId = 0;
                if (jsonObject.has(RECIPE_ID)) {
                    recipeId = jsonObject.getInt(RECIPE_ID);
                }

                // Extract the value for the key called "name"
                String recipeName = null;
                if (jsonObject.has(RECIPE_NAME)) {
                    recipeName = jsonObject.getString(RECIPE_NAME);
                }

                // Create new JSONArray for getting Array of objects with JsonObject.
                JSONArray jsonArray = jsonObject.getJSONArray(STEPS_ARRAY);
                // Get last object from JSONArray which parses a thumbnail from last VideoUrl.
                JSONObject stepsObject = jsonArray.getJSONObject(jsonArray.length() - 1);

                // Extract the value for the key called "videoURL"
                String recipeImage = null;
                if (stepsObject.has(VIDEO_URL)) {
                    recipeImage = stepsObject.getString(VIDEO_URL);
                }

                // Create new Recipe object with recipeId, recipeName, recipeImage from response.
                Recipe recipe = new Recipe(recipeId, recipeName, recipeImage);
                // Add each object to created ArrayList of Recipe.
                recipeList.add(recipe);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JsonUtils", "Problem parsing fetchRecipeJsonData results", e);
        }

        // Return the list of recipes.
        return recipeList;
    }

    /**
     * @param json     takes as a String parameter for response.
     * @param recipeId to only get response on selected recipe of ingredients.
     * @return list of ingredients {@link Ingredients} that has been built up from parsing a JSON
     * response in background see {@link IngredientsFragmentViewModel#getIngredientsData(int)}
     */
    public static List<Ingredients> fetchIngredientsJsonData(String json, int recipeId) {

        // Create a new ArrayList for adding ingredients into list.
        List<Ingredients> ingredientsList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONArray from the json response string
            JSONArray baseJsonArray = new JSONArray(json);
            // Create a new JSONObject from JSONArray to get list of Array of objects with selected
            // recipe id.
            JSONObject baseJsonObject = baseJsonArray.getJSONObject(recipeId);
            // With created JSONObject parse required JSONArray of type "ingredients".
            JSONArray jsonArray = baseJsonObject.getJSONArray(INGREDIENTS_ARRAY);
            // Loop inside the each object to get data.
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject ingredientsObject = jsonArray.getJSONObject(j);

                // Extract the value for the key called "quantity"
                String ingredientsQuantity = null;
                if (ingredientsObject.has(QUANTITY)) {
                    ingredientsQuantity = ingredientsObject.getString(QUANTITY);
                }

                // Extract the value for the key called "measure"
                String ingredientsMeasure = null;
                if (ingredientsObject.has(MEASURE)) {
                    ingredientsMeasure = ingredientsObject.getString(MEASURE);
                }

                // Extract the value for the key called "ingredient"
                String formatIngredient = null;
                if (ingredientsObject.has(INGREDIENT)) {
                    String ingredientsIngredient = ingredientsObject.getString(INGREDIENT);
                    // Format each string to show capital first letter.
                    formatIngredient = FormatUtils.capitalize(ingredientsIngredient);
                }

                // Create a new Ingredient object with ingredientsQuantity, ingredientsMeasure, formatIngredient.
                Ingredients ingredients = new Ingredients(ingredientsQuantity, ingredientsMeasure, formatIngredient);
                // Add each object to created list of ingredients.
                ingredientsList.add(ingredients);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JsonUtils", "Problem parsing fetchIngredientsJsonData results", e);
        }

        // Return the list of ingredients.
        return ingredientsList;
    }

    /**
     * @param json     takes as a String parameter for response.
     * @param recipeId to only get response on selected recipe of steps.
     * @return list of steps {@link Steps} that has been built up from parsing a JSON
     * response in background see {@link StepsFragmentViewModel#getStepsData(int)}
     */
    public static List<Steps> fetchStepsJsonData(String json, int recipeId) {

        // Create a new ArrayList for adding steps into list.
        List<Steps> stepsList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONArray from the json response string
            JSONArray baseJsonArray = new JSONArray(json);
            // Create a new JSONObject from JSONArray to get list of Array of objects with selected
            // recipe id.
            JSONObject jsonObject = baseJsonArray.getJSONObject(recipeId);
            // With created JSONObject parse required JSONArray of type "steps".
            JSONArray jsonArray = jsonObject.getJSONArray(STEPS_ARRAY);
            // Loop inside the each object to get data.
            for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject stepsObject = jsonArray.getJSONObject(k);

                // Extract the value for the key called "id"
                int stepsId = 0;
                if (stepsObject.has(STEPS_ID)) {
                    stepsId = stepsObject.getInt(STEPS_ID);
                }

                // Extract the value for the key called "shortDescription"
                String shortDescription = null;
                if (stepsObject.has(SHORT_DESCRIPTION)) {
                    shortDescription = stepsObject.getString(SHORT_DESCRIPTION);
                }

                // Extract the value for the key called "description"
                String description = null;
                if (stepsObject.has(DESCRIPTION)) {
                    description = stepsObject.getString(DESCRIPTION);
                }

                // Extract the value for the key called "videoURL"
                String videoUrl = null;
                if (stepsObject.has(VIDEO_URL)) {
                    videoUrl = stepsObject.getString(VIDEO_URL);
                }

                // Extract the value for the key called "thumbnailURL"
                String thumbnailUrl = "";
                if (stepsObject.has(THUMBNAIL_URL)) {
                    thumbnailUrl = stepsObject.getString(THUMBNAIL_URL);
                }

                // If thumbnailURL is empty we assign to use vale of videoURL which
                // has thumbnail loaded from video.
                if (thumbnailUrl.equals("")) {
                    thumbnailUrl = videoUrl;
                }

                // Create a new Steps object with stepsId, shortDescription, description, videoUrl,
                // thumbnailUrl.
                Steps steps = new Steps(stepsId, shortDescription, description, videoUrl, thumbnailUrl);
                // Add each object to created list of steps.
                stepsList.add(steps);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JsonUtils", "Problem parsing fetchStepsJsonData results", e);
        }

        // Return list of steps for selected recipe.
        return stepsList;
    }

    /**
     * @param json     takes as a String parameter for response.
     * @param recipeId to only get response on selected recipe details.
     * @return list of count for Ingredients {@link Ingredients} and Steps {@link Steps} that has
     * been built up from parsing a JSON
     * response in background see {@link RecipeDetailFragment#applyBadgeDrawables(ItemLength, TabLayout)}
     */
    public static ItemLength fetchTotalNumberJsonData(String json, int recipeId) {

        // Create a new ArrayList for adding ItemLength count in Ingredients and Steps.
        ItemLength itemLengthList = null;

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONArray from the json response string
            JSONArray baseJsonArray = new JSONArray(json);
            // Create a new JSONObject from JSONArray to get list of Array of objects with selected
            // recipe id.
            JSONObject jsonObject = baseJsonArray.getJSONObject(recipeId);
            // With created JSONObject parse required JSONArray of type "ingredients".
            JSONArray jsonArray = jsonObject.getJSONArray(INGREDIENTS_ARRAY);
            // With created JSONObject parse required JSONArray of type "steps".
            JSONArray stepsArray = jsonObject.getJSONArray(STEPS_ARRAY);
            // Extract the value for total count on Ingredients by getting length of JSONArray
            int totalNumOfIngredients = jsonArray.length();
            // Extract the value for total count on Steps by getting length of JSONArray
            int totalNumOfSteps = stepsArray.length();
            // Add each object to created list of ItemLength.
            itemLengthList = new ItemLength(totalNumOfIngredients, totalNumOfSteps);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JsonUtils", "Problem parsing fetchTotalNumberJsonData results", e);
        }

        // Return list of itemLength for selected recipe.
        return itemLengthList;
    }

    /**
     * @param json takes as a String parameter.
     * @return list of recipes {@link Podcast} that has been built up from parsing a JSON response in
     * background see {@link PodcastFragmentViewModel#fetchJsonData()}
     */
    public static List<Podcast> fetchPodcastJsonData(String json) {

        // Create a new ArrayList for adding podcasts into list.
        List<Podcast> podcastList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONArray from the json response string
            JSONArray baseJsonArray = new JSONArray(json);
            // Loop inside each objects of array
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject jsonObject = baseJsonArray.getJSONObject(i);

                // Extract the value for the key called "id"
                int recipeId = 0;
                if (jsonObject.has(RECIPE_ID)) {
                    recipeId = jsonObject.getInt(RECIPE_ID);
                }

                // Extract the value for the key called "name"
                String recipeName = null;
                if (jsonObject.has(RECIPE_NAME)) {
                    recipeName = jsonObject.getString(RECIPE_NAME);
                }

                // Create new JSONArray for getting Array of objects with JsonObject.
                JSONArray stepsArray = jsonObject.getJSONArray(STEPS_ARRAY);
                // Get last object from JSONArray which parses a thumbnail from last VideoUrl.
                JSONObject stepsJsonObject = stepsArray.getJSONObject(stepsArray.length() - 1);

                // Extract the value for the key called "videoURL"
                String recipeImage = null;
                if (stepsJsonObject.has(VIDEO_URL)) {
                    recipeImage = stepsJsonObject.getString(VIDEO_URL);
                }

                // Create new ArrayList of String containing String of URL's
                List<String> stepsList = new ArrayList<>();
                for (int k = 0; k < stepsArray.length(); k++) {
                    JSONObject stepsObject = stepsArray.getJSONObject(k);

                    // Extract the value for the key called "videoURL"
                    String videoUrl = null;
                    if (stepsObject.has(VIDEO_URL)) {
                        videoUrl = stepsObject.getString(VIDEO_URL);
                    }

                    // Assign single URL to new string steps
                    String steps = videoUrl;
                    // Add single step to create a list of String URL's
                    stepsList.add(steps);
                }

                // Create a new Podcast object with recipeId, recipeName, recipeImage, stepsList
                Podcast podcast = new Podcast(recipeId, recipeName, recipeImage, stepsList);
                // Add each object to created list of podcasts.
                podcastList.add(podcast);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JsonUtils", "Problem parsing fetchPodcastJsonData results", e);
        }

        // Return list of podcasts for selected recipe.
        return podcastList;
    }

    /**
     * @param json takes as a String parameter.
     * @return list of widgetItems {@link WidgetItem} that has been built up from parsing a JSON
     * response in background see {@link RecipeWidgetConfigureActivity} class
     */
    public static List<WidgetItem> fetchWidgetJsonData(String json) {

        // Create a new ArrayList for adding widgetItems into list.
        List<WidgetItem> widgetItemList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONArray from the json response string
            JSONArray baseJsonArray = new JSONArray(json);
            // Loop inside each objects of array
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject jsonObject = baseJsonArray.getJSONObject(i);

                // Extract the value for the key called "name"
                String recipeName = null;
                if (jsonObject.has(RECIPE_NAME)) {
                    recipeName = jsonObject.getString(RECIPE_NAME);
                }

                //Create a new WidgetItem object with recipeName.
                WidgetItem widgetItem = new WidgetItem(recipeName);
                // Add each object to created list of WidgetItems.
                widgetItemList.add(widgetItem);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JsonUtils", "Problem parsing fetchWidgetJsonData results", e);
        }

        // Return list of widget items.
        return widgetItemList;
    }
}
