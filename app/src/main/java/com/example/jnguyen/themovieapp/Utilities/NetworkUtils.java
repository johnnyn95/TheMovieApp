package com.example.jnguyen.themovieapp.Utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    //http://image.tmdb.org/t/p/ + path
    private final static String AUTHENTICATION_KEY = "e5c29049ee97d4ff4783f528930be86e";
    final static String SEARCH_BASE_QUERY_URL = "https://api.themoviedb.org/3/search/movie";
    final static String IMAGE_BASE_URL = "http://image.tmdb.org";///t/p/pw300";
    final static String PARAM_API_KEY = "api_key";
    final static String PARAM_PAGE = "page";
    final static String PARAM_QUERY = "query";

    public static URL buildSearchQueryUrl(String searchQuery){
        Uri builtUri = Uri.parse(SEARCH_BASE_QUERY_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY,AUTHENTICATION_KEY)
                .appendQueryParameter(PARAM_PAGE,"1")
                .appendQueryParameter(PARAM_QUERY,searchQuery)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildImageUrl(String imagePath){
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .path(imagePath)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

public static Uri buildImageUri(String imagePath){
    Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
            .appendPath("t")
            .appendPath("p")
            .appendPath("w300")
            .appendEncodedPath(imagePath)
            .build();

    return builtUri;
}



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
