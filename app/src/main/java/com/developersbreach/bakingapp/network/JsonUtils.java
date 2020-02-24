package com.developersbreach.bakingapp.network;


import com.developersbreach.bakingapp.model.Ingredients;
import com.developersbreach.bakingapp.model.ItemLength;
import com.developersbreach.bakingapp.model.Podcast;
import com.developersbreach.bakingapp.model.Recipe;
import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.utils.FormatUtils;
import com.developersbreach.bakingapp.widget.WidgetItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

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

    public static List<Recipe> fetchRecipeJsonData(String json) {

        List<Recipe> recipeList = new ArrayList<>();

        try {

            JSONArray baseJsonArray = new JSONArray(json);
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject jsonObject = baseJsonArray.getJSONObject(i);

                int recipeId = 0;
                if (jsonObject.has(RECIPE_ID)) {
                    recipeId = jsonObject.getInt(RECIPE_ID);
                }

                String recipeName = null;
                if (jsonObject.has(RECIPE_NAME)) {
                    recipeName = jsonObject.getString(RECIPE_NAME);
                }

                JSONArray jsonArray = jsonObject.getJSONArray(STEPS_ARRAY);
                JSONObject stepsObject = jsonArray.getJSONObject(jsonArray.length() - 1);

                String recipeImage = null;
                if (stepsObject.has(VIDEO_URL)) {
                    recipeImage = stepsObject.getString(VIDEO_URL);
                }

                Recipe recipe = new Recipe(recipeId, recipeName, recipeImage);
                recipeList.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    public static List<Ingredients> fetchIngredients(String json, int recipeId) {

        List<Ingredients> ingredientsList = new ArrayList<>();

        try {

            JSONArray baseJsonArray = new JSONArray(json);
            JSONObject jsonObject = baseJsonArray.getJSONObject(recipeId);
            JSONArray jsonArray = jsonObject.getJSONArray(INGREDIENTS_ARRAY);
            for (int j = 0; j < jsonArray.length(); j++) {

                JSONObject ingredientsObject = jsonArray.getJSONObject(j);

                String ingredientsQuantity = null;
                if (ingredientsObject.has(QUANTITY)) {
                    ingredientsQuantity = ingredientsObject.getString(QUANTITY);
                }

                String ingredientsMeasure = null;
                if (ingredientsObject.has(MEASURE)) {
                    ingredientsMeasure = ingredientsObject.getString(MEASURE);
                }

                String formatIngredient = null;
                if (ingredientsObject.has(INGREDIENT)) {
                    String ingredientsIngredient = ingredientsObject.getString(INGREDIENT);
                    formatIngredient = FormatUtils.capitalize(ingredientsIngredient);
                }

                Ingredients ingredients = new Ingredients(ingredientsQuantity, ingredientsMeasure, formatIngredient);
                ingredientsList.add(ingredients);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredientsList;
    }

    public static List<Steps> fetchSteps(String json, int recipeId) {

        List<Steps> stepsList = new ArrayList<>();

        try {

            JSONArray baseJsonArray = new JSONArray(json);
            JSONObject jsonObject = baseJsonArray.getJSONObject(recipeId);
            JSONArray jsonArray = jsonObject.getJSONArray(STEPS_ARRAY);
            for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject stepsObject = jsonArray.getJSONObject(k);

                Integer stepsId = null;
                if (stepsObject.has(STEPS_ID)) {
                    stepsId = stepsObject.getInt(STEPS_ID);
                }
                String formatStepsIdString = String.valueOf(stepsId);

                String shortDescription = null;
                if (stepsObject.has(SHORT_DESCRIPTION)) {
                    shortDescription = stepsObject.getString(SHORT_DESCRIPTION);
                }

                String description = null;
                if (stepsObject.has(DESCRIPTION)) {
                    description = stepsObject.getString(DESCRIPTION);
                }

                String videoUrl = null;
                if (stepsObject.has(VIDEO_URL)) {
                    videoUrl = stepsObject.getString(VIDEO_URL);
                }

                String thumbnailUrl = "";
                if (stepsObject.has(THUMBNAIL_URL)) {
                    thumbnailUrl = stepsObject.getString(THUMBNAIL_URL);
                }

                if (thumbnailUrl.equals("")) {
                    thumbnailUrl = videoUrl;
                }

                Steps steps = new Steps(formatStepsIdString, shortDescription, description, videoUrl, thumbnailUrl);
                stepsList.add(steps);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stepsList;
    }

    public static ItemLength findTotalNumber(String json, int recipeId) {

        ItemLength itemLengthList = null;

        try {
            JSONArray baseJsonArray = new JSONArray(json);
            JSONObject jsonObject = baseJsonArray.getJSONObject(recipeId);
            JSONArray jsonArray = jsonObject.getJSONArray(INGREDIENTS_ARRAY);
            JSONArray stepsArray = jsonObject.getJSONArray(STEPS_ARRAY);

            int totalNumOfIngredients = jsonArray.length();
            int totalNumOfSteps = stepsArray.length();

            itemLengthList = new ItemLength(totalNumOfIngredients, totalNumOfSteps);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemLengthList;
    }

    public static List<Podcast> fetchPodcastJsonData(String responseString) {

        List<Podcast> podcastList = new ArrayList<>();

        try {

            JSONArray baseJsonArray = new JSONArray(responseString);
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject jsonObject = baseJsonArray.getJSONObject(i);

                int recipeId = 0;
                if (jsonObject.has(RECIPE_ID)) {
                    recipeId = jsonObject.getInt(RECIPE_ID);
                }

                String recipeName = null;
                if (jsonObject.has(RECIPE_NAME)) {
                    recipeName = jsonObject.getString(RECIPE_NAME);
                }

                JSONArray stepsArray = jsonObject.getJSONArray(STEPS_ARRAY);

                JSONObject stepsJsonObject = stepsArray.getJSONObject(stepsArray.length() - 1);
                String recipeImage = null;
                if (stepsJsonObject.has(VIDEO_URL)) {
                    recipeImage = stepsJsonObject.getString(VIDEO_URL);
                }

                List<String> stepsList = new ArrayList<>();
                for (int k = 0; k < stepsArray.length(); k++) {
                    JSONObject stepsObject = stepsArray.getJSONObject(k);

                    String videoUrl = null;
                    if (stepsObject.has(VIDEO_URL)) {
                        videoUrl = stepsObject.getString(VIDEO_URL);
                    }

                    String steps = videoUrl;
                    stepsList.add(steps);
                }

                Podcast podcast = new Podcast(recipeId, recipeName, recipeImage, stepsList);
                podcastList.add(podcast);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return podcastList;
    }

    public static List<WidgetItem> fetchWidgetJsonData(String json) {

        List<WidgetItem> widgetItemList = new ArrayList<>();

        try {

            JSONArray baseJsonArray = new JSONArray(json);
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject jsonObject = baseJsonArray.getJSONObject(i);

                String recipeName = null;
                if (jsonObject.has(RECIPE_NAME)) {
                    recipeName = jsonObject.getString(RECIPE_NAME);
                }

                WidgetItem widgetItem = new WidgetItem(recipeName);
                widgetItemList.add(widgetItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return widgetItemList;
    }
}
