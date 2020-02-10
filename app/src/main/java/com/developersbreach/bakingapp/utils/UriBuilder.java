package com.developersbreach.bakingapp.utils;

import android.net.Uri;

public class UriBuilder {

    private static final String SCHEME_AUTHORITY = "https://d17h27t6h515a5.cloudfront.net";
    private static final String APPEND_PATH_TOPHER = "topher";
    private static final String APPEND_PATH_YEAR = "2017";
    private static final String APPEND_PATH_MONTH = "May";
    private static final String APPEND_PATH_ID = "59121517_baking";
    private static final String APPEND_PATH_TYPE = "baking.json";

    /**
     * Builds Uri used to fetch movie data from the server. This data is based on the query
     * capabilities of the movie database provider that we are using.
     * API_KEY is used to query specific data from the server
     * @return The String to use to query the movie database server.
     *
     *  https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
     */
    public static String uriBuilder() {

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
}
