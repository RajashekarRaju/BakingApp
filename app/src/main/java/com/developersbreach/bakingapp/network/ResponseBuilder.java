package com.developersbreach.bakingapp.network;

import android.net.Uri;
import android.util.Log;

import com.developersbreach.bakingapp.viewModel.RecipeListFragmentViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * This class builds standard response by building a URI for starting a response. Build URI is
 * returned by {@link #uriBuilder()}
 */
public class ResponseBuilder {

    // Strings which hold path to create a standard URI object.
    private static final String SCHEME_AUTHORITY = "https://d17h27t6h515a5.cloudfront.net";
    private static final String APPEND_PATH_TOPHER = "topher";
    private static final String APPEND_PATH_YEAR = "2017";
    private static final String APPEND_PATH_MONTH = "May";
    private static final String APPEND_PATH_ID = "59121517_baking";
    private static final String APPEND_PATH_TYPE = "baking.json";

    /**
     * @return returns a string URL created to make JSON request in background see class for example
     * implementation {@link RecipeListFragmentViewModel}.
     * @throws IOException if an error is thrown when executing any of the statements.
     */
    public static String startResponse() throws IOException {
        String uriString = uriBuilder();
        URL requestUrl = createUrl(uriString);
        return getResponseFromHttpUrl(requestUrl);
    }

    /**
     * Builds Uri used to fetch recipe data from the server.
     *
     * @return The String to use to query the recipe data from the server.
     * <p>
     * https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
     */
    private static String uriBuilder() {

        Uri baseUri = Uri.parse(SCHEME_AUTHORITY);
        // Constructs a new Builder.
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder
                .appendPath(APPEND_PATH_TOPHER)
                .appendPath(APPEND_PATH_YEAR)
                .appendPath(APPEND_PATH_MONTH)
                .appendPath(APPEND_PATH_ID)
                .appendPath(APPEND_PATH_TYPE);
        // Returns a string representation of the object.
        return uriBuilder.build().toString();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("createUrl", "Problem building the URL ", e);
        }
        return url;
    }
}
