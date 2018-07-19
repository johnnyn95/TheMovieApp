package com.example.jnguyen.themovieapp.Utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TheMovieDbJSONUtils {

    private static final String RESULTS = "results";
    private static final String RESULTS_COUNT = "total_results";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_SCORE = "vote_average";
    private static final String MOVIE_IMAGE_PATH = "poster_path";

    public static String getMovieTitleKey(){
        return MOVIE_TITLE;
    }
    public static String getMovieScoreKey(){
        return MOVIE_SCORE;
    }
    public static String getMovieImagePathKey(){
        return MOVIE_IMAGE_PATH;
    }

    public static ContentValues[] getMoviesContentValuesFromJson(String jsonResult)
            throws JSONException {
        JSONObject moviesJson = new JSONObject(jsonResult);

        //if(moviesJson.has(RESULTS_COUNT) && moviesJson.getInt(RESULTS_COUNT) == 0){
        //    //no results found
        //}
        JSONArray moviesJsonArray = moviesJson.getJSONArray(RESULTS);
        ContentValues[] movieContentValues = new ContentValues[moviesJsonArray.length()];

        for (int i = 0; i < moviesJsonArray.length();i++){
            JSONObject movieJson = moviesJsonArray.getJSONObject(i);

            String movieTitle = movieJson.getString(MOVIE_TITLE);
            Double movieScore = movieJson.getDouble(MOVIE_SCORE);
            String movieImagePath = movieJson.getString(MOVIE_IMAGE_PATH);

            ContentValues movieValues = new ContentValues();
            movieValues.put(MOVIE_TITLE,movieTitle);
            movieValues.put(MOVIE_SCORE,movieScore);
            movieValues.put(MOVIE_IMAGE_PATH,movieImagePath);
            movieContentValues[i] = movieValues;
        }
        return movieContentValues;
    }
}
